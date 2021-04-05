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

import java.util.*;

import org.luwrain.core.*;
import org.luwrain.pim.*;
import org.luwrain.pim.news.*;

class Groups implements NewsGroups
{
    private final Registry registry;

    Groups(Registry registry)
    {
	NullCheck.notNull(registry, "registry");
	this.registry = registry;
    }

    @Override public StoredNewsGroup[] load() throws PimException
    {
	    final LinkedList<StoredNewsGroup> groups = new LinkedList<StoredNewsGroup>();
	    for(String s: registry.getDirectories(org.luwrain.pim.news.Settings.GROUPS_PATH))
	    {
		if (s.isEmpty())
		    continue;
		int id;
		try {
		    id = Integer.parseInt(s);
		}
		catch(NumberFormatException e)
		{
		    Log.warning("pim", "news group with bad registry directory:" + s);
		    continue;
		}
		final Group g = new Group(registry, id);
		g.load();
		    groups.add(g);
	    }
final StoredNewsGroup[] res = groups.toArray(new StoredNewsGroup[groups.size()]);
Arrays.sort(res);
return res;
    }

    @Override public StoredNewsGroup loadById(int id) throws PimException
    {
	final Group group = new Group(registry, id);
	group.load();
	return group;
    }

    @Override public void save(NewsGroup group) throws PimException
    {
	NullCheck.notNull(group, "group");
	final int id = Registry.nextFreeNum(registry, org.luwrain.pim.news.Settings.GROUPS_PATH);
	final String path = Registry.join(org.luwrain.pim.news.Settings.GROUPS_PATH, "" + id);
	registry.addDirectory(path);
	final org.luwrain.pim.news.Settings.Group settings = org.luwrain.pim.news.Settings.createGroup(registry, path);
	settings.setName(group.name);
	settings.setOrderIndex(group.orderIndex);
	settings.setExpireDays(group.expireAfterDays);
	settings.setMediaContentType(group.mediaContentType);
    }

    @Override public void delete(StoredNewsGroup group) throws PimException
    {
	if (!(group instanceof Group))
	    return;
	final Group groupReg = (Group)group;
	groupReg.delete();
    }

    @Override public Object clone()
    {
	return null;
    }
}
