// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.pim.diary.persistence;

import java.util.*;

import org.apache.logging.log4j.*;
import org.h2.mvstore.*;

import org.luwrain.pim.*;

import static java.util.Objects.*;
import static org.luwrain.pim.ExecQueues.*;

public final class DiaryPersistence
{
    static private final Logger log = LogManager.getLogger();

    final ExecQueues queues;
    private Priority priority = Priority.MEDIUM;
    private Runner runner = null;
    private final MVMap<Long, Event> eventsMap;

    public DiaryPersistence(ExecQueues queues,
			    MVMap<Long, Event> eventsMap)
    {
	this.queues = requireNonNull(queues, "queues can't be null");
	this.eventsMap = requireNonNull(eventsMap, "eventsMap can't be null");
	this.runner = new Runner(queues, priority);
    }

    public EventDAO getEventDAO()
    {
	return new EventDAO(){
	    @Override public long add(Event event)
	    {
		requireNonNull(event, "event can't be null");
		return runner.run(() -> {
		    final var maxKey = eventsMap.floorKey(Long.MAX_VALUE);
		    final long newId = maxKey != null ? maxKey.longValue() + 1 : 1;
		    event.setId(newId);
		    log.trace("Adding " + event);
		    eventsMap.put(Long.valueOf(newId), event);
		    return Long.valueOf(newId);
		}).longValue();
	    }

	    @Override public void delete(Event event)
	    {
		requireNonNull(event, "event can't be null");
		if (event.getId() < 0)
		    throw new IllegalArgumentException("An event can't have negative ID");
		log.trace("Deleting " + event);
		runner.run(() -> eventsMap.remove(Long.valueOf(event.getId())));
	    }

	    @Override public List<Event> getAll()
	    {
		return runner.run(() -> eventsMap.entrySet().stream()
				  .map(e -> e.getValue())
				  .toList());
	    }

	    @Override public void update(Event event)
	    {
		requireNonNull(event, "event can't be null");
		if (event.getId() < 0)
		    throw new IllegalArgumentException("An event can't have negative ID");
		log.trace("Updating " + event);
		runner.run(() -> {
		    eventsMap.put(Long.valueOf(event.getId()), event);
		    return null;
		});
	    }
	};
    }

    public void setPriority(Priority priority)
    {
	this.priority = requireNonNull(priority, "priority can't be null");
	this.runner = new Runner(queues, priority);
    }
}
