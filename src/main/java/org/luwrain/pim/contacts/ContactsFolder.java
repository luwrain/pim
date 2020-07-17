/*
   Copyright 2012-2020 Michael Pozhidaev <msp@luwrain.org>
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

package org.luwrain.pim.contacts;

import org.luwrain.core.*;
import org.luwrain.pim.*;

public class ContactsFolder implements Comparable
{
    private String title = "";
    private int orderIndex = 0;

    public void setTitle(String title) throws PimException
    {
	NullCheck.notNull(title, "title");
	this.title = title;
    }

    public String getTitle() throws PimException
    {
	return this.title;
    }

    public void setOrderIndex(int orderIndex) throws PimException
    {
	if (orderIndex < 0)
	    throw new IllegalArgumentException("orderIndex (" + String.valueOf(orderIndex) + ") may not be negative");
	this.orderIndex = orderIndex;
    }

    public int getOrderIndex() throws PimException
    {
	return this.orderIndex;
    }

    public boolean isRoot()
    {
	return false;
    }

    @Override public String toString()
    {
	return title;
    }

    @Override public int compareTo(Object o)
    {
	if (o == null || !(o instanceof ContactsFolder))
	    return 0;
	final ContactsFolder folder = (ContactsFolder)o;
	if (orderIndex < folder.orderIndex)
	    return -1;
	if (orderIndex > folder.orderIndex)
	    return 1;
	return 0;
    }

    @Override public boolean equals(Object o)
    {
	if (o == null || !(o instanceof ContactsFolder))
	    return false;
	final ContactsFolder folder = (ContactsFolder)o;
	return title.equals(folder.title);
    }
}
