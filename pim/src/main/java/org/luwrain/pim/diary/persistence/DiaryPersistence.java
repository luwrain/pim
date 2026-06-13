// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.pim.diary.persistence;

import java.util.*;

import org.apache.logging.log4j.*;
import org.h2.mvstore.*;

import org.luwrain.pim.*;

import static java.util.Objects.*;
import static org.luwrain.pim.ExecQueues.*;

/**
 * Core storage engine for the diary. Manages MVStore maps for events
 * and to-do items, and provides implementations of {@link EventDAO} and
 * {@link TodoDAO} that perform all operations via {@link ExecQueues}
 * execution queues. Generation of new numeric identifiers for events
 * and to-do items is done independently through a shared key map.
 *
 * <p>Instances of this class are created by the
 * {@link org.luwrain.pim.diary.DiaryFactory} factory.</p>
 *
 * @see Event
 * @see EventDAO
 * @see Todo
 * @see TodoDAO
 * @see org.luwrain.pim.diary.DiaryFactory
 */
public final class DiaryPersistence
{
    static private final Logger log = LogManager.getLogger();

    /** Execution queues for asynchronous operations. */
    final ExecQueues queues;

    /** Current priority for storage operations. */
    private Priority priority = Priority.MEDIUM;

    /** Runner wrapping operations with the current priority. */
    private Runner runner = null;

    /** Event map: identifier → {@link Event}. */
    private final MVMap<Long, Event> eventsMap;

    /** To-do map: identifier → {@link Todo}. */
    private final MVMap<Long, Todo> todosMap;

    /**
     * Key map: fully qualified class name → last issued numeric
     * identifier. Used to generate new IDs via
     * {@link #getNewKey(Class)}.
     */
    private final MVMap<String, Long> keysMap;

    /**
     * Creates a diary storage instance.
     *
     * @param queues    execution queues for performing map operations
     * @param eventsMap MVStore map for storing events
     * @param todosMap  MVStore map for storing to-do items
     * @param keysMap   MVStore map for generating identifiers
     * @throws NullPointerException if any parameter is {@code null}
     */
    public DiaryPersistence(ExecQueues queues,
			    MVMap<Long, Event> eventsMap,
			    MVMap<Long, Todo> todosMap,
			    MVMap<String, Long> keysMap)
    {
	this.queues = requireNonNull(queues, "queues can't be null");
	this.eventsMap = requireNonNull(eventsMap, "eventsMap can't be null");
	this.todosMap = requireNonNull(todosMap, "todosMap can't be null");
	this.keysMap = requireNonNull(keysMap, "keysMap can't be null");
	this.runner = new Runner(queues, priority);
    }

    /**
     * Returns an {@link EventDAO} implementation for working with events.
     * All operations (add, delete, list, update) are executed
     * asynchronously through the execution queues with the current
     * priority.
     *
     * @return data access object for events
     */
    public EventDAO getEventDAO()
    {
	return new EventDAO(){
	    @Override public long add(Event event)
	    {
		requireNonNull(event, "event can't be null");
		return runner.run(() -> {
		    final long newId = getNewKey(Event.class).longValue();
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

    /**
     * Returns a {@link TodoDAO} implementation for working with to-do items.
     * All operations (add, delete, list, update) are executed
     * asynchronously through the execution queues with the current
     * priority.
     *
     * @return data access object for to-do items
     */
    public TodoDAO getTodoDAO()
    {
	return new TodoDAO(){
	    @Override public long add(Todo todo)
	    {
		requireNonNull(todo, "todo can't be null");
		return runner.run(() -> {
		    final long newId = getNewKey(Todo.class).longValue();
		    todo.setId(newId);
		    log.trace("Adding " + todo);
		    todosMap.put(Long.valueOf(newId), todo);
		    return Long.valueOf(newId);
		}).longValue();
	    }

	    @Override public void delete(Todo todo)
	    {
		requireNonNull(todo, "todo can't be null");
		if (todo.getId() < 0)
		    throw new IllegalArgumentException("A todo can't have negative ID");
		log.trace("Deleting " + todo);
		runner.run(() -> todosMap.remove(Long.valueOf(todo.getId())));
	    }

	    @Override public List<Todo> getAll()
	    {
		return runner.run(() -> todosMap.entrySet().stream()
				  .map(e -> e.getValue())
				  .toList());
	    }

	    @Override public void update(Todo todo)
	    {
		requireNonNull(todo, "todo can't be null");
		if (todo.getId() < 0)
		    throw new IllegalArgumentException("A todo can't have negative ID");
		log.trace("Updating " + todo);
		runner.run(() -> {
		    todosMap.put(Long.valueOf(todo.getId()), todo);
		    return null;
		});
	    }
	};
    }

    /**
     * Issues a new unique numeric identifier for the specified class.
     * Identifiers are independent per class: {@code Event} and
     * {@code Todo} each have their own sequences.
     *
     * @param c the class for which a new identifier is required
     * @return a new identifier, starting from 0 and incrementing by 1
     *         on each call
     */
    Long getNewKey(Class c)
    {
	final var res = keysMap.get(c.getName());
	if (res == null)
	{
	    keysMap.put(c.getName(), Long.valueOf(0));
	    return Long.valueOf(0);
	}
	final var newVal = Long.valueOf(res.longValue() + 1);
	keysMap.put(c.getName(), newVal);
	return newVal;
    }

    /**
     * Sets the execution priority for storage operations.
     * When the priority is changed, a new {@link Runner} is created,
     * so all subsequent operations will run with the new priority.
     *
     * @param priority the new priority; must not be {@code null}
     * @throws NullPointerException if {@code priority} is {@code null}
     */
    public void setPriority(Priority priority)
    {
	this.priority = requireNonNull(priority, "priority can't be null");
	this.runner = new Runner(queues, priority);
    }
}
