/*
   Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>
   Copyright 2015 Roman Volovodov <gr.rPman@gmail.com>

   This file is part of LUWRAIN.

   LUWRAIN is free software; you can redistribute it and/or
   modify it under the terms of the GNU General Public
   License as published by the Free Software Foundation; either
   version 3 of the License, or (at your option) any later version.

   LUWRAIN is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
   General Public License for more details.
*/

package org.luwrain.pim.news.persistence;

import java.util.*;
import java.util.concurrent.*;
import org.h2.mvstore.*;

import org.luwrain.pim.storage.*;
import org.luwrain.pim.news.persistence.model.*;
import org.luwrain.pim.news.persistence.dao.*;

import static java.util.Objects.*;
import static org.luwrain.pim.storage.ExecQueues.*;

public final class NewsPersistence
{
        final ExecQueues queues = null;
    private Priority priority = Priority.MEDIUM;
    private Runner runner = null;
        private final MVMap<Integer, Group> groupsMap = null;
            private final MVMap<Integer, Article> articlesMap = null;
    private final MVMap<String, Long> keysMap = null;

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

	    @Override public List<Group> getAll()
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
