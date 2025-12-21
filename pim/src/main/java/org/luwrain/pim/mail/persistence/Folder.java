// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>
// Copyright 2015 Roman Volovodov <gr.rPman@gmail.com>

package org.luwrain.pim.mail.persistence;

import java.util.*;
import java.io.*;
import org.apache.logging.log4j.*;
import lombok.*;

@Data
public class Folder implements Serializable
{
    static private final Logger log = LogManager.getLogger();

    private int id, parentFolderId, deleteReadMessagesAfterDays;
    private String name, propsText;
    private transient Properties properties;

    public Properties getProperties()
    {
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
	/*
	if (o != null && o instanceof Folder f)
	    return id == f.id;
	*/
	return false;
    }

    @Override public int hashCode()
    {
	return 0;//id;
    }

    @Override public String toString()
    {
	return name != null?name:"";
    }
}
