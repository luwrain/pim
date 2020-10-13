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

//LWR_API 1.0

package org.luwrain.pim.mail;

import java.io.*;
import java.util.*;

import org.luwrain.core.*;
import org.luwrain.pim.*;

public class MailFolder implements Comparable
{
    private String title = "";
    private int orderIndex = 0;
    private Properties props = new Properties();

    public final void setTitle(String title)
    {
	NullCheck.notNull(title, "title");
	this.title = title;
    }

    public final String getTitle()
    {
	return this.title;
    }

    public final void setOrderIndex(int orderIndex)
    {
	if (orderIndex < 0)
	    throw new IllegalArgumentException("orderIndex (" + String.valueOf(orderIndex) + ") may not be negative");
	this.orderIndex = orderIndex;
    }

    public final int getOrderIndex()
    {
	return this.orderIndex;
    }

    public final Properties getProperties()
    {
	return this.props;
    }

    public void saveProperties()
    {
    }

    public final void copyValues(MailFolder folder)
    {
	NullCheck.notNull(folder, "folder");
	this.title = folder.title;
	this.orderIndex = folder.orderIndex;
	this.props = new Properties();
	this.props.putAll(folder.props);
    }

    public final String getPropertiesAsString() throws IOException
    {
	final StringWriter writer = new StringWriter();
	props.store(writer, "");
	writer.flush();
	return writer.toString();
    }

    public final void setPropertiesFromString(String text) throws IOException
    {
	NullCheck.notNull(text, "text");
	final StringReader reader = new StringReader(text);
	props.clear();
	props.load(reader);
    }

    public void save() throws PimException
    {
    }

    @Override public String toString()
    {
	return title != null?title:"";
    }

    @Override public int compareTo(Object o)
    {
	if (o == null || !(o instanceof MailFolder))
	    return 0;
	final MailFolder folder = (MailFolder)o;
	if (orderIndex < folder.orderIndex)
	    return -1;
	if (orderIndex > folder.orderIndex)
	    return 1;
	return 0;
    }
}
