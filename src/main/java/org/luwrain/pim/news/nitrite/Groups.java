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
        static private final Type _LIST_TYPE = new TypeToken<List<Group>>(){}.getType();

    private final Gson gson = new Gson();
    private final org.luwrain.pim.news.Settings sett;
    private List<Group> groups = null;

    Groups(Registry registry)
    {
	NullCheck.notNull(registry, "registry");
		this.sett = org.luwrain.pim.news.Settings.create(registry);registry = registry;

    }

    @Override public NewsGroup[] load()
    {
	return null;
    }

    @Override public NewsGroup loadById(int id)
    {
	return null;
    }

    @Override public void save(NewsGroup group) throws PimException
    {
	NullCheck.notNull(group, "group");
    }

    @Override public void delete(NewsGroup group)
    {
    }

}
