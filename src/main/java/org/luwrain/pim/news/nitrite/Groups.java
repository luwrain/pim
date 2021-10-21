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

import java.lang.reflect.*;
import java.util.*;

import com.google.gson.*;
import com.google.gson.reflect.*;

import org.luwrain.core.*;
import org.luwrain.pim.*;
import org.luwrain.pim.news.*;

final class Groups implements NewsGroups
{
        static private final Type LIST_TYPE = new TypeToken<List<Group>>(){}.getType();

    private final Gson gson = new Gson();
    private final org.luwrain.pim.news.Settings sett;
    private List<Group> groups = null;

    Groups(Registry registry)
    {
	NullCheck.notNull(registry, "registry");
		this.sett = org.luwrain.pim.news.Settings.create(registry);registry = registry;
    }

    @Override public synchronized Group[] load()
    {
	if (this.groups != null)
	    return this.groups.toArray(new Group[this.groups.size()]);
final List<Group> res = gson.fromJson(sett.getGroups(""), LIST_TYPE);
this.groups = new ArrayList<>();
if (res != null)
    for(Group g: res)
	if (g != null)
	    this.groups.add(g);
		    return this.groups.toArray(new Group[this.groups.size()]);
    }

    synchronized void save()
    {
	if (this.groups != null)
	    this.sett.setGroups(gson.toJson(this.groups));
    }

    @Override public synchronized NewsGroup loadById(int id)
    {
	for(Group g: load())
	    if (g.id == id)
		return g;
	return null;
    }

    @Override public synchronized void save(NewsGroup group) throws PimException
    {
	NullCheck.notNull(group, "group");
	load();
	final Group g = new Group();
	g.id = sett.getNextGroupId(0);
	sett.setNextGroupId(g.id + 1);
	g.groups = this;
	g.copyValues(group);
	this.groups.add(g);
	save();
	    }

    @Override public synchronized void delete(NewsGroup group)
    {
    }
}
