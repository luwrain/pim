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

class Group extends NewsGroup
{
    private final Registry registry;

    final int id;

    Group(Registry registry, int id)
    {
	if (id < 0)
	    throw new IllegalArgumentException("id (" + id + ") may not be negative");
	NullCheck.notNull(registry, "registry");
	this.registry = registry;
	this.id = (int)id;
    }

    long getId()
    {
	return id;
}

    @Override public void setName(String name)
    {
	NullCheck.notNull(name, "name");
	    super.setName(name);
    }

    @Override public void setMediaContentType(String value)
    {
	NullCheck.notNull(value, "value");
	    //	    this.mediaContentType = value;
    }

    @Override public void setOrderIndex(int index)
    {
	if (index < 0)
	    throw new IllegalArgumentException("orderIndex (" + String.valueOf(index) + ") may not be negative");
		//		this.orderIndex = index;
    }

    @Override public void setExpireAfterDays(int count)
    {
	if (count < 0)
	    throw new IllegalArgumentException("count (" + count + ") may not be negative");
		//		this.expireAfterDays = count;
    }

    @Override public boolean equals(Object o)
    {
	if (o == null || !(o instanceof Group))
	    return false;
	return id == ((Group)o).id;
    }


    void load()
    {
	/*
	final String path = getPath();
name = settings.getName("");
expireAfterDays = settings.getExpireDays(0);
orderIndex = settings.getOrderIndex(0);
mediaContentType = settings.getMediaContentType("");
	if (expireAfterDays < 0)
	    expireAfterDays = 0;
	if (orderIndex < 0)
	    orderIndex = 0;
	final String[] values = registry.getValues(path);
	final List<String> urls = new LinkedList();
	for(String s: values)
	{
	    if (s.indexOf("url") < 0 || registry.getTypeOf(Registry.join(path, s)) != Registry.STRING)
		continue;
	    final String value = registry.getString(path + "/" + s);
	    if (!value.trim().isEmpty())
		urls.add(value);
	}
	this.urls = urls.toArray(new String[urls.size()]);
	*/
    }

}
