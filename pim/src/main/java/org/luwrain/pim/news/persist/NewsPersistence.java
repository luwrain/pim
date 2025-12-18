// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>
// Copyright 2015 Roman Volovodov <gr.rPman@gmail.com>

package org.luwrain.pim.news.persist;

import java.util.*;
import java.util.concurrent.*;
import org.h2.mvstore.*;

import org.luwrain.pim.storage.*;

import static java.util.Objects.*;
import static org.luwrain.pim.storage.ExecQueues.*;

public final class NewsPersistence
{
    final ExecQueues queues;
    private Priority priority = Priority.MEDIUM;
    private Runner runner;
    private final MVMap<Integer, Group> groupsMap;
    private final MVMap<Long, Article> articlesMap;
    private final MVMap<String, Long> keysMap;
    
    public NewsPersistence(ExecQueues queues,
			   MVMap<Integer, Group> groupsMap,
			   MVMap<Long, Article> articlesMap,
			   MVMap<String, Long> keysMap)
    {
	this.queues = requireNonNull(queues, "queues can't be null");
	this.groupsMap = requireNonNull(groupsMap, "groupsMap can't be null");
	this.articlesMap = requireNonNull(articlesMap, "articlesMap can't be null");
	this.keysMap = requireNonNull(keysMap, "keysMap can't be null");
	this.runner = new Runner(queues, priority);
    }

public GroupDAO getGroupDAO()
    {
	return new GroupDAO(){
	    @Override public int add(Group group)
	    {
		requireNonNull(group, "group can't be null");
		return runner.run(new FutureTask<>( () -> {
			    final int newId = getNewKey(Group.class).intValue();
			    group.setId(newId);
			    groupsMap.put(Integer.valueOf(newId), group);
			    return Integer.valueOf(newId);
		})).intValue();
	    }

	    @Override public void delete(Group group)
	    {
		//FIXME:
	    }

	    @Override public List<Group> load()
	    {
		return runner.run(new FutureTask<>( () -> groupsMap.entrySet().stream()
						    .map( e -> e.getValue() )
						    .toList()));
	    }

	    @Override public void update(Group group)
	    {
		requireNonNull(group, "group can't be null");
		if (group.getId() < 0)
		    throw new IllegalArgumentException("A group can't have negative ID");
		runner.run(new FutureTask<Object>( () -> groupsMap.put(Integer.valueOf(group.getId()), group) , null));
	    }
	};
    }

    public ArticleDAO getArticleDAO()
    {
	return null;
    }


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

    public void setPriority(Priority priority)
    {
	this.priority = requireNonNull(priority, "priority can't be null");
	this.runner = new Runner(queues, priority);
    }
}
