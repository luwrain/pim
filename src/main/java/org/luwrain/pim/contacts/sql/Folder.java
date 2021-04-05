/*
   Copyright 2012-2021 Michael Pozhidaev <msp@luwrain.org>
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

package org.luwrain.pim.contacts.sql;

import org.luwrain.core.*;
import org.luwrain.pim.*;
import org.luwrain.pim.contacts.*;

final class Folder extends ContactsFolder
{
    static private final String LOG_COMPONENT = "pim-contacts";
    
    private final Registry registry;
    private final org.luwrain.pim.contacts.Settings.Folder sett;

    final int id;
    int parentId; 

    Folder(Registry registry, int id)
    {
	NullCheck.notNull(registry, "registry");
	this.registry = registry;
	this.id = id;
	this.sett = org.luwrain.pim.contacts.Settings.createFolder(registry, Registry.join(org.luwrain.pim.contacts.Settings.FOLDERS_PATH, "" + id));
    }

    @Override public void setTitle(String title) throws PimException
    {
	NullCheck.notNull(title, "title");
	sett.setTitle(title);
	super.setTitle(title);
    }

    @Override public void setOrderIndex(int orderIndex) throws PimException
    {
	if (orderIndex < 0)
	    throw new IllegalArgumentException("orderIndex (" + String.valueOf(orderIndex) + ") may not be negative");
	sett.setOrderIndex(orderIndex);
	super.setOrderIndex(orderIndex);
}

@Override public boolean isRoot()
    {
	return id == parentId;
    }

    void setParentId(int value)
    {
	sett.setParentId(value);
	this.parentId = value;
    }

    boolean load()
    {
	try {
	    super.setTitle(sett.getTitle(""));
	    if (getTitle().isEmpty() || parentId < 0)
		return false;
	    final int orderIndex = sett.getOrderIndex(0);
	    if (orderIndex < 0)
	    {
		Log.warning(LOG_COMPONENT, "the contacts folder with ID=" + String.valueOf(id) + " has a negative order index (" + String.valueOf(orderIndex) + ")");
		return false;
	    }
	    super.setOrderIndex(orderIndex);
	    final int parentId = sett.getParentId(0);
	    if (parentId < 0)
	    {
		Log.warning(LOG_COMPONENT, "the contacts folder with ID=" + String.valueOf(id) + " has a negative parent ID (" + String.valueOf(parentId) + ")");
		return false;
	    }
	    return true;
	}
	catch(PimException e)
	{
	    Log.warning(LOG_COMPONENT, "unable to load the contacts folder with ID=" + String.valueOf(id) + ":" + e.getClass().getName() + ":" + e.getMessage());
	    return false;
	}
    }

    @Override public boolean equals(Object o)
    {
	if (o == null || !(o instanceof Folder))
	    return false;
	final Folder folder = (Folder)o;
	return id == folder.id;
    }
}
