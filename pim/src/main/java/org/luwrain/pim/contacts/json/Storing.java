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

package org.luwrain.pim.contacts.json;

import java.io.*;
import com.google.gson.*;

import org.luwrain.core.*;
import org.luwrain.pim.contacts.*;
import org.luwrain.util.*;

public final class Storing implements ContactsStoring
{
    static private final String LOG_COMPONENT = org.luwrain.pim.contacts.Factory.LOG_COMPONENT;

    private final Gson gson = new Gson();
    private final File file;
    Folder root = new Folder();
    private final Folders folders;
    private final Contacts contacts;

    public Storing(File file)
    {
	NullCheck.notNull(file, "file");
	this.file = file;
	this.folders = new Folders(this);
	this.contacts = new Contacts(this);
	Folder f = null;
	try {
	    try (final BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
		f = gson.fromJson(r, Folder.class);
	    }
	}
	catch(IOException e)
	{
	    Log.debug(LOG_COMPONENT, "unable to read " + file.getAbsolutePath() + ": " + e.getClass().getName() + ": " + e.getMessage() + ", probably the first start");
	}
	if (f != null)
	    this.root = f; else
	    this.root = new Folder();
	this.root.setStoring(this);
    }

    void save()
    {
	try {
	    try (final BufferedWriter w = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)))) {
		gson.toJson(root, w);
		w.flush();
	    }
	}
	catch(IOException e)
	{
	    throw new RuntimeException(e);
	}
    }

    @Override public ContactsFolders getFolders()
    {
	return this.folders;
    }

    @Override public Contacts getContacts()
    {
	return this.contacts;
    }
}
