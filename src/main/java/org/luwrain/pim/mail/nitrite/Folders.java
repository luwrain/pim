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

package org.luwrain.pim.mail.nitrite;

import java.io.*;
import java.util.*;

import com.google.gson.*;
import com.google.gson.annotations.*;

import org.luwrain.core.*;
import org.luwrain.pim.*;
import org.luwrain.pim.mail.*;

final class Folders implements MailFolders
{
    static private final String LOG_COMPONENT = "mail";

    private final Registry registry;
    private final Messages messages;
    private final Gson gson = new Gson();
    private Data data = null;

    Folders(Registry registry, Messages messages)
    {
	NullCheck.notNull(registry, "registry");
	NullCheck.notNull(messages, "messages");
	this.registry = registry;
	this.messages = messages;
    }

    @Override public MailFolder findFirstByProperty(String propName, String propValue) throws PimException
    {
	NullCheck.notEmpty(propName, "propName");
	NullCheck.notNull(propValue, "propValue");
	loadAll();
	final Folder[] folders = null;
	for(Folder f: folders)
	{
	    final String value = f.getProperties().getProperty(propName);
	    if (value != null && value.equals(propValue))
		return f;
	}
	return null;
    }

    @Override public int getId(MailFolder folder)
    {
	NullCheck.notNull(folder, "folder");
	loadAll();
	final Folder f = (Folder)folder;
	return f.id;
    }

    @Override public MailFolder loadById(int id) throws PimException
    {
	loadAll();
	final Folder folder = new Folder();
	return null;
    }

    @Override public MailFolder save(MailFolder parentFolder, MailFolder newFolder) throws PimException
    {
	NullCheck.notNull(parentFolder, "parentFolder");
	NullCheck.notNull(newFolder, "newFolder");
	loadAll();
	final Folder p = (Folder)parentFolder;
	final Folder n = (Folder)newFolder;
	p.subfolders.add(n);
	return n;
    }

    @Override public MailFolder getRoot() throws PimException
    {
	loadAll();
	return this.data.root;
    }

    @Override public MailFolder[] load(MailFolder folder) throws PimException
    {
	NullCheck.notNull(folder, "folder");
	loadAll();
	final Folder f = (Folder)folder;
	return f.subfolders.toArray(new Folder[f.subfolders.size()]);
    }

    @Override public String getUniRef(MailFolder folder) throws PimException
    {
	NullCheck.notNull(folder, "folder");
	loadAll();
	final Folder folderReg = (Folder)folder;
	return FolderUniRefProc.PREFIX + ":" + folderReg.id;
    }

    @Override public MailFolder loadByUniRef(String uniRef) throws PimException
    {
	NullCheck.notEmpty(uniRef, "uniRef");
	loadAll();
	if (!uniRef.startsWith(FolderUniRefProc.PREFIX + ":"))
	    return null;
	final int id;
	try {
	    id = Integer.parseInt(uniRef.substring(FolderUniRefProc.PREFIX.length() + 1));
	}
	catch (NumberFormatException e)
	{
	    Log.warning(LOG_COMPONENT, "parsing an invalid mail folder uniref: " + uniRef);
	    return null;
	}
	final Folder folder = new Folder();
	return null;
    }

    private void loadAll()
    {
	if (this.data != null)
	    return;
	try {
	final Data res = gson.fromJson(registry.getString("/org/luwrain/pim/mail/folders2"), Data.class);
	if (res == null)
	{
	    Log.warning(LOG_COMPONENT, "unable to load a folders tree from the registry");
	    this.data = new Data();
	    return;
	}
	this.data = res;
	}
	catch(Exception e)
	{
	    Log.warning(LOG_COMPONENT, "unable to load a folders tree from the registry: " + e.getClass().getName() + ":" + e.getMessage());
	    this.data = new Data();
	}
    }

    private void saveAll()
    {
	registry.setString("/org/luwrain/pim/mail/folders2", gson.toJson(data));
    }

    static private final class Data
    {
	@SerializedName("nextId")
	int nextId = 1;
	@SerializedName("root")
	Folder root = null;
    }
}
