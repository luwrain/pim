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

package org.luwrain.pim.mail;

import java.io.*;
import java.util.*;

import org.luwrain.core.*;

public class MailFolder implements Comparable
{
    public String title = "";
    public int orderIndex = 0;
    public final Properties props = new Properties();

    public String getPropertiesAsString() throws IOException
    {
	final StringWriter writer = new StringWriter();
	props.store(writer, "");
	writer.flush();
	return writer.toString();
    }

    public void setPropertiesFromString(String text) throws IOException
    {
	NullCheck.notNull(text, "text");
	final StringReader reader = new StringReader(text);
	props.clear();
	props.load(reader);
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
