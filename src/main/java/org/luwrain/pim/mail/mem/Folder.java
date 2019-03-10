/*
   Copyright 2012-2019 Michael Pozhidaev <michael.pozhidaev@gmail.com>
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

package org.luwrain.pim.mail.mem;

import java.util.*;

import org.luwrain.core.*;
import org.luwrain.pim.*;
import org.luwrain.pim.mail.*;

class Folder extends MailFolder implements StoredMailFolder
{
    final int id;
    int parentId = 0;

    Folder(int id)
    {
	if (id < 0)
	    throw new IllegalArgumentException("id (" + id + ") may not be negative");
	this.id = id;
    }

    @Override public String getTitle() throws PimException
    {
	return title;
    }

    @Override public void setTitle(String value) throws PimException
    {
	NullCheck.notNull(value, "value");
	this.title = value;
    }

    @Override public int getOrderIndex() throws PimException
    {
	return orderIndex;
    }

    @Override public void setOrderIndex(int value)
    {
	if (value < 0)
	    throw new IllegalArgumentException("value (" + value + ") may not be negative");
	this.orderIndex = value;
    }

    @Override public Properties getProperties()
    {
	return props;
    }

    @Override public boolean equals(Object o)
    {
	if (o == null || !(o instanceof Folder))
	    return false;
	final Folder folder = (Folder)o;
	return id == folder.id;
    }
}
