/*
   Copyright 2012-2021 Michael Pozhidaev <michael.pozhidaev@gmail.com>
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

package org.luwrain.pim.news.nitrite;

import java.util.concurrent.*;

import org.luwrain.core.*;
import org.luwrain.pim.*;
import org.luwrain.pim.news.*;
import org.luwrain.pim.storage.*;

public final class Storing implements NewsStoring
{
    private final Registry registry;
    final NitriteStorage<Article> storage;
    private final org.luwrain.pim.ExecQueues execQueues;
    private final boolean highPriority;
    private final Articles articles;
    private final Groups groups;

    public Storing(Registry registry, NitriteStorage<Article> storage, org.luwrain.pim.ExecQueues execQueues, boolean highPriority)
    {
	NullCheck.notNull(registry, "registry");
	NullCheck.notNull(storage, "storage");
	NullCheck.notNull(execQueues, "execQueues");
	this.registry = registry;
	this.storage = storage;
	this.execQueues = execQueues;
	this.highPriority = highPriority;
	this.articles = new Articles(this);
	this.groups = new Groups(registry);
    }

    @Override public NewsArticles getArticles() { return articles; }
    @Override public NewsGroups getGroups() { return groups; }

    <T> T execInQueue(Callable<T> callable) throws Exception
    {
	NullCheck.notNull(callable, "callable");
	return execQueues.exec(new FutureTask<T>(callable), highPriority);
    }
}
