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

package org.luwrain.pim.mail.nitrite;

import java.util.*;
import java.util.function.*;

import com.google.gson.*;
import com.google.gson.annotations.*;

import org.luwrain.core.*;
import org.luwrain.pim.*;
import org.luwrain.pim.mail.*;

final class Folders implements MailFolders
{
    static private final String LOG_COMPONENT = "mail";

        private final Gson gson = new Gson();
    private final Registry registry;
    private final org.luwrain.pim.mail.Settings sett;
    private final Messages messages;

    private Data data = null;

    Folders(Registry registry, Messages messages)
    {
	NullCheck.notNull(registry, "registry");
	NullCheck.notNull(messages, "messages");
	this.registry = registry;
	this.sett = org.luwrain.pim.mail.Settings.create(registry);
	this.messages = messages;
    }

    @Override public MailFolder findFirstByProperty(String propName, String propValue) throws PimException
    {
	NullCheck.notEmpty(propName, "propName");
	NullCheck.notNull(propValue, "propValue");
	loadAll();
	return find(data.root, (o)->{
		final Folder f = (Folder)o;
		final String value = f.getProperties().getProperty(propName);
		return value != null && value.equals(propValue);
	    });
    }

    @Override public int getId(MailFolder folder)
    {
	NullCheck.notNull(folder, "folder");
	loadAll();
	final Folder f = (Folder)folder;
	return f.getId();
    }

    @Override public MailFolder loadById(int id) throws PimException
    {
	loadAll();
	return find(data.root, (o)->{
		final Folder f = (Folder)o;
		return f.getId() == id;
	    });
    }

    @Override public MailFolder save(MailFolder parentFolder, MailFolder newFolder) throws PimException
    {
	NullCheck.notNull(parentFolder, "parentFolder");
	NullCheck.notNull(newFolder, "newFolder");
	loadAll();
	final Folder p = (Folder)parentFolder;
	final Folder n = new Folder();
	n.copyValues(newFolder);
	n.setId(data.nextId);
	n.setFolders(this);
	data.nextId++;
		p.addSubfolder(n);
		saveAll();
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
	return f.getSubfoldersAsArray();
    }

    private Folder find(Folder f, Predicate p)
    {
	if (f == null)
	    return null;
	if (p.test(f))
	    return f;
	for(Folder ff: f.getSubfoldersAsArray())
	{
	    final Folder res = find(ff, p);
	    if (res != null)
		return res;
	}
	return null;
    }

    private void loadAll()
    {
	if (this.data != null)
	    return;
	try {
	    final Data res = gson.fromJson(sett.getFolders(""), Data.class);
	if (res == null)
	{
	    Log.warning(LOG_COMPONENT, "unable to load a folders tree from the registry, creating default");
	    initRoot();
	    return;
	}
	this.data = res;
	this.data.root.visit((o)->{
		final Folder f = (Folder)o;
		f.setFolders(this);
	    });
	}
	catch(Exception e)
	{
	    Log.warning(LOG_COMPONENT, "unable to load a folders tree from the registry: " + e.getClass().getName() + ":" + e.getMessage());
	    initRoot();
	}
    }

    void saveAll()
    {
	sett.setFolders(gson.toJson(data));
    }

    private void initRoot()
    {
	this.data = new Data();
	this.data.root = new Folder();
	this.data.root.setTitle("Почта");
	this.data.root.setFolders(this);
	saveAll();

    }

    static private final class Data
    {
	@SerializedName("nextId")
	int nextId = 1;
	@SerializedName("root")
	Folder root = null;
    }
}
