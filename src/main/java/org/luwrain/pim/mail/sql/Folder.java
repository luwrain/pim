/*
   Copyright 2012-2017 Michael Pozhidaev <michael.pozhidaev@gmail.com>
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

import org.luwrain.core.*;
import org.luwrain.pim.*;
import org.luwrain.pim.mail.*;

class Folder extends MailFolder implements StoredMailFolder
{
    private final Registry registry;
    private final org.luwrain.pim.mail.Settings.Folder sett;

    final int id;
    int parentId = 0;

    Folder(Registry registry, int id)
    {
	NullCheck.notNull(registry, "registry");
	this.registry = registry;
	this.id = id;
	this.sett = org.luwrain.pim.mail.Settings.createFolder(registry, Registry.join(org.luwrain.pim.mail.Settings.FOLDERS_PATH, "" + id));
    }

    @Override public String getTitle() throws PimException
    {
	return title;
    }

    @Override public void setTitle(String value) throws PimException
    {
	NullCheck.notNull(value, "value");
	sett.setTitle(value);
	this.title = value;
    }

    @Override public int getOrderIndex() throws PimException
    {
	return orderIndex;
    }

    @Override public void setOrderIndex(int value) throws PimException
    {
	sett.setOrderIndex(value);
	this.orderIndex = value;
    }

    @Override public boolean equals(Object o)
    {
	if (o == null || !(o instanceof Folder))
	    return false;
	final Folder folder = (Folder)o;
	return id == folder.id;
    }

    boolean load()
    {
	this.title = sett.getTitle("");
	this.orderIndex = sett.getOrderIndex(0);
	this.parentId = sett.getParentId(0);
	if (title.isEmpty() || parentId < 0)
	    return false;
	if (orderIndex < 0)
	    orderIndex = 0;
	return true;
    }
}
