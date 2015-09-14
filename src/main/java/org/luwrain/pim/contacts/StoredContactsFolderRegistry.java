/*
   Copyright 2012-2015 Michael Pozhidaev <michael.pozhidaev@gmail.com>
   Copyright 2015 Roman Volovodov <gr.rPman@gmail.com>

   This file is part of the LUWRAIN.

   LUWRAIN is free software; you can redistribute it and/or
   modify it under the terms of the GNU General Public
   License as published by the Free Software Foundation; either
   version 3 of the License, or (at your option) any later version.

   LUWRAIN is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
   General Public License for more details.
*/

package org.luwrain.pim.contacts;

import org.luwrain.core.Registry;

class StoredContactsFolderRegistry implements StoredContactsFolder, Comparable
{
    private Registry registry;

    public long id;
    public String title;
    public int orderIndex;
    public long parentId; 

    public StoredContactsFolderRegistry(Registry registry)
    {
	this.registry = registry;
	if (registry == null)
	    throw new NullPointerException("registry may not be null");
    }

    @Override public String getTitle() throws Exception
    {
	return title != null?title:"";
    }

    @Override public void setTitle(String value) throws Exception
    {
	//FIXME:
    }

    @Override public int getOrderIndex() throws Exception
    {
	return orderIndex;
    }

    @Override public void setOrderIndex(int value) throws Exception
    {
	//FIXME:
    }

    @Override public String toString()
    {
	return title != null?title:"";
    }

    @Override public boolean equals(Object o)
    {
	if (o == null || !(o instanceof StoredContactsFolderRegistry))
	    return false;
	final StoredContactsFolderRegistry folder = (StoredContactsFolderRegistry)o;
	return id == folder.id;
    }

    @Override public int compareTo(Object o)
    {
	if (o == null || !(o instanceof StoredContactsFolderRegistry))
	    return 0;
	final StoredContactsFolderRegistry folder = (StoredContactsFolderRegistry)o;
	if (orderIndex < folder.orderIndex)
	    return -1;
	if (orderIndex > folder.orderIndex)
	    return 1;
	return 0;
    }
}
