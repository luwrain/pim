/*
   Copyright 2012-2023 Michael Pozhidaev <msp@luwrain.org>
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

import org.luwrain.core.*;
import org.luwrain.pim.*;
import org.luwrain.pim.mail.*;

import static org.luwrain.pim.mail.MailFolders.*;

final class Folders implements MailFolders
{
    static private final String
	LOG_COMPONENT = "mail",
	TRUE = "true";

    private final Gson gson = new Gson();
    private final Registry registry;
    private org.luwrain.pim.mail.Strings strings;
    private final org.luwrain.pim.mail.Settings sett;
    private final Messages messages;
    private Data data = null;

    Folders(Storing storing, Messages messages)
    {
	this.registry = storing.luwrain.getRegistry();
	this.strings = (org.luwrain.pim.mail.Strings)storing.luwrain.i18n().getStrings(org.luwrain.pim.mail.Strings.NAME);
	this.sett = org.luwrain.pim.mail.Settings.create(registry);
	this.messages = messages;
	loadAll();
    }

    @Override synchronized public MailFolder findFirstByProperty(String propName, String propValue)
    {
	return find(data.root, (f)->{
		if (f.getProperties() == null)
		    return false;
		final String value = f.getProperties().getProperty(propName);
		return value != null && value.equals(propValue);
	    });
    }

    @Override public int getId(MailFolder folder)
    {
	final Folder f = (Folder)folder;
	return f.getId();
    }

    @Override synchronized public MailFolder loadById(int id) throws PimException
    {
	return find(data.root, (o)->{
		final Folder f = (Folder)o;
		return f.getId() == id;
	    });
    }

    @Override synchronized public MailFolder save(MailFolder parentFolder, MailFolder newFolder, int saveAtIndex)
    {
	NullCheck.notNull(newFolder, "newFolder");
	final Folder p = (Folder)parentFolder;
	final Folder n = new Folder();
	n.copyValues(newFolder);
	n.setId(data.nextId++);
	n.setFolders(this);
	p.addSubfolder(n, saveAtIndex);
	saveAll();
	return n;
    }

    @Override synchronized public boolean remove(MailFolder parent, int index)
    {
	final Folder folder = (Folder)parent;
	if (!folder.removeSubfolder(index))
	    return false;
	saveAll();
	return true;
    }

    @Override public boolean hasSubfolders(MailFolder mailFolder)
    {
	final Folder folder = (Folder)mailFolder;
	return folder.getSubfolderCount() > 0;
    }

    @Override public MailFolder getRoot() throws PimException
    {
	return this.data.root;
    }

    @Override synchronized public MailFolder[] load(MailFolder folder)
    {
	final Folder f = (Folder)folder;
	return f.getSubfoldersAsArray();
    }

    synchronized void saveAll()
    {
	sett.setFolders(gson.toJson(data));
    }

    private Folder find(Folder f, Predicate<Folder> p)
    {
	if (f == null)
	    return null;
	if (p.test(f))
	    return f;
	if (f.subfolders != null)
	for(Folder ff: f.subfolders)
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

    private void initRoot()
    {
Folder f;
List<Folder> ff = new ArrayList<>();

//Inbox
	f = new Folder();
	f.id = 10;
	f.folders = this;
	f.setTitle(strings.inboxFolder());
	f.setProperties(new Properties());
	ff.add(f);

	//Mailing lists
	f = new Folder();
	f.id = 11;
	f.folders = this;
	f.setTitle(strings.mailingListsFolder());
	f.setProperties(new Properties());
	f.getProperties().setProperty(PROP_DEFAULT_MAILING_LISTS, TRUE);
	ff.add(f);

		//OUtgoing
	f = new Folder();
	f.id = 12;
	f.folders = this;
	f.setTitle(strings.outgoingFolder());
	f.setProperties(new Properties());
	f.getProperties().setProperty(PROP_DEFAULT_OUTGOING, TRUE);
	ff.add(f);

			//Sent
	f = new Folder();
	f.id = 13;
	f.folders = this;
	f.setTitle(strings.sentFolder());
	f.setProperties(new Properties());
	f.getProperties().setProperty(PROP_DEFAULT_SENT, TRUE);
	ff.add(f);

				//Drafts
	f = new Folder();
	f.id = 14;
	f.folders = this;
	f.setTitle(strings.draftsFolder());
	f.setProperties(new Properties());
	f.getProperties().setProperty(PROP_DEFAULT_DRAFTS, TRUE);
	ff.add(f);

		this.data = new Data();
	this.data.root = new Folder();
	this.data.root.id = 1;
	this.data.root.setTitle(strings.mailFoldersRoot());
	this.data.root.folders = this;
	this.data.root.subfolders = ff;
	this.data.nextId = 100;
	saveAll();
    }

    static private final class Data
    {
	int nextId = 1;
	Folder root = null;
    }
}
