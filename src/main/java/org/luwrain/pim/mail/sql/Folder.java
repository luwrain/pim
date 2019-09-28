/*
   Copyright 2012-2019 Michael Pozhidaev <msp@luwrain.org>
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

package org.luwrain.pim.mail.sql;

import java.io.*;
import java.util.*;

import org.luwrain.core.*;
import org.luwrain.pim.*;
import org.luwrain.pim.mail.*;

final class Folder extends MailFolder
{
    static private final String LOG_COMPONENT = Storing.LOG_COMPONENT;

    private final Registry registry;
    private final org.luwrain.pim.mail.Settings.Folder sett;

    final int id;
    int parentId = 0;

    Folder(Registry registry, int id)
    {
	NullCheck.notNull(registry, "registry");
	this.registry = registry;
	this.id = id;
	if (id < 0)
	    throw new IllegalArgumentException("id (" + id + ") may not be negative");
	this.sett = org.luwrain.pim.mail.Settings.createFolder(registry, Registry.join(org.luwrain.pim.mail.Settings.FOLDERS_PATH, "" + id));
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
	    throw new IllegalArgumentException("value (" + String.valueOf(orderIndex) + ") may not be negative");
	sett.setOrderIndex(orderIndex);
	super.setOrderIndex(orderIndex);
    }

    @Override public Properties getProperties() throws PimException
    {
	return super.getProperties();
    }

    @Override public void saveProperties() throws PimException
    {
	try {
	    sett.setProperties(getPropertiesAsString());
	}
	catch(IOException e)
	{
	    throw new PimException(e);
	}
    }

    @Override public boolean equals(Object o)
    {
	if (o == null || !(o instanceof Folder))
	    return false;
	final Folder folder = (Folder)o;
	return id == folder.id;
    }

    boolean load() throws PimException
    {
	super.setTitle(sett.getTitle(""));
	super.setOrderIndex(sett.getOrderIndex(0) >= 0?sett.getOrderIndex(0):0);
	this.parentId = sett.getParentId(0);
	try {
	    setPropertiesFromString(sett.getProperties(""));
	}
	catch(IOException e)
	{
	    Log.error(LOG_COMPONENT, "unable to load properties from the registry:" + e.getClass().getName() + ":" + e.getMessage());
	    return false;
	}
	if (parentId < 0)
	    return false;
	return true;
    }
}
