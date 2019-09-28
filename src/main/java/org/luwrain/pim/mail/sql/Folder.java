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
    final int id;
    private final org.luwrain.pim.mail.Settings.Folder sett;
    private int parentId = 0;

    Folder(Registry registry, int id)
    {
	NullCheck.notNull(registry, "registry");
	this.registry = registry;
	this.id = id;
	if (id < 0)
	    throw new IllegalArgumentException("id (" + String.valueOf(id) + ") may not be negative");
	this.sett = org.luwrain.pim.mail.Settings.createFolder(registry, Registry.join(org.luwrain.pim.mail.Settings.FOLDERS_PATH, "" + id));
    }

    @Override public void setTitle(String title) throws PimException
    {
	NullCheck.notNull(title, "title");
	sett.setTitle(title);
	super.setTitle(title);
    }

    public void setParentId(int parentId) throws PimException
    {
	if (parentId < 0)
	    throw new IllegalArgumentException("parentId (" + String.valueOf(parentId) + ") may not be negative");
	sett.setParentId(parentId);
	this.parentId = parentId;
    }

    public int getParentId() throws PimException
    {
	return this.parentId;
    }

    @Override public void setOrderIndex(int orderIndex) throws PimException
    {
	if (orderIndex < 0)
	    throw new IllegalArgumentException("value (" + String.valueOf(orderIndex) + ") may not be negative");
	sett.setOrderIndex(orderIndex);
	super.setOrderIndex(orderIndex);
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

    /** The only method to get the Folder class fully loaded.*/
    boolean load()
    {
	try {
	    super.setTitle(sett.getTitle(""));
	    final int orderIndex = sett.getOrderIndex(0);
	    super.setOrderIndex(orderIndex >= 0?orderIndex:0);
	    this.parentId = sett.getParentId(0);
	    if (parentId < 0)
	    {
		Log.warning(LOG_COMPONENT, "there is a mail folder with ID=" + String.valueOf(id) + "and with the negative parent ID (" + parentId + ")");
		return false;
	    }
	    try {
		setPropertiesFromString(sett.getProperties(""));
	    }
	    catch(IOException e)
	    {
		Log.error(LOG_COMPONENT, "unable to load properties from the registryfor the mail folder with ID=" + String.valueOf(id) + ":" + e.getClass().getName() + ":" + e.getMessage());
		return false;
	    }
	    return true;
	}
	catch(PimException e)
	{
	    Log.warning(LOG_COMPONENT, "unexpected exception on loading the mail folder with ID=" + String.valueOf(id) + ":" + e.getClass().getName() + ":" + e.getMessage());
	    return false;
	}
    }
}
