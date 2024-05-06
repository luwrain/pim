/*
   Copyright 2012-2024 Michael Pozhidaev <msp@luwrain.org>
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

package org.luwrain.pim.mail2.persistence.model;

import java.util.*;
import java.io.*;
import org.apache.logging.log4j.*;
import lombok.*;
import jakarta.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table (name = "folder")
public class Folder
{
    static private final Logger log = LogManager.getLogger();
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private int parentFolderId, deleteReadMessagesAfterDays;

    @Column(columnDefinition="TEXT")
    private String propsText;
    private transient Properties properties;

    public Properties getProperties()
    {
	log.debug("requesting properties");
	if (properties == null)
	    properties = new Properties();
	return properties;
    }

    public void saveProperties()
    {
	if (properties == null)
	{
	    propsText = "";
	    return;
	}
	final var w = new StringWriter();
	try {
	    properties.store(w, "");
	}
	catch(IOException e)
	{
	    throw new RuntimeException(e);
	}
	w.flush();
	propsText = w.toString();
    }

    public void loadProperties()
    {
	properties = new Properties();
	if (propsText == null || propsText.isEmpty())
	{
	    propsText = null;
	    return;
	}
	final var r = new StringReader(propsText);
	try {
	    properties.load(r);
	}
	catch(IOException e)
	{
	    throw new RuntimeException(e);
	}
    }

    @Override public boolean equals(Object o)
    {
	if (o != null && o instanceof Folder f)
	    return id == f.id;
	return false;
    }

    @Override public int hashCode()
    {
	return id;
    }

    @Override public String toString()
    {
	return name != null?name:"";
    }
}
