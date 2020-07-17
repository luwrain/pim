/*
   Copyright 2012-2020 Michael Pozhidaev <michael.pozhidaev@gmail.com>
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

package org.luwrain.pim.news.sql;

import java.util.concurrent.*;
import java.sql.*;

import org.luwrain.core.*;
import org.luwrain.pim.*;
import org.luwrain.pim.news.*;

public final class Storing implements NewsStoring
{
    private final Registry registry;
    private final Connection con;
    private final Articles articles;
    private final Groups groups;
    private final ExecQueues execQueues;
    private final boolean highPriority;

    public Storing(Registry registry, Connection con, ExecQueues execQueues, boolean highPriority)
    {
	NullCheck.notNull(registry, "registry");
	NullCheck.notNull(con, "con");
	NullCheck.notNull(execQueues, "execQueues");
	this.registry = registry;
	this.con = con;
	this.execQueues = execQueues;
	this.highPriority = highPriority;
	this.articles = new Articles(this, con);
	this.groups = new Groups(registry);
    }

    @Override public NewsArticles getArticles()
    {
	return articles;
    }

    @Override public NewsGroups getGroups()
    {
	return groups;
    }

    @Override public Object clone()
    {
	return null;
    }

    Object execInQueue(Callable callable) throws Exception
    {
	NullCheck.notNull(callable, "callable");
	return execQueues.exec(new FutureTask(callable), highPriority);
    }
}
