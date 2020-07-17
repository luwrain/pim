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

package org.luwrain.pim.mail.sql;

import java.io.*;
import java.util.*;

import org.luwrain.core.*;
import org.luwrain.pim.*;
import org.luwrain.pim.mail.*;

final class Folders implements MailFolders
{
    static private final String LOG_COMPONENT = Storing.LOG_COMPONENT;

    private final Registry registry;
    private final Messages messages;
    private Folder[] cache = null;

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
	final Folder[] folders = loadAllFolders();
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
	return ((Folder)folder).id;
    }

    @Override public MailFolder loadById(int id) throws PimException
    {
	final Folder folder = new Folder(registry, id);
	return folder.load()?folder:null;
    }

    @Override public MailFolder save(MailFolder parentFolder, MailFolder newFolder) throws PimException
    {
	NullCheck.notNull(parentFolder, "parentFolder");
	NullCheck.notNull(newFolder, "newFolder");
	final Folder parent = (Folder)parentFolder;
	this.cache = null;
	registry.addDirectory(org.luwrain.pim.mail.Settings.FOLDERS_PATH);
	final int newId = Registry.nextFreeNum(registry, org.luwrain.pim.mail.Settings.FOLDERS_PATH);
	final String path = Registry.join(org.luwrain.pim.mail.Settings.FOLDERS_PATH, "" + newId);
	registry.addDirectory(path);
	final org.luwrain.pim.mail.Settings.Folder sett = org.luwrain.pim.mail.Settings.createFolder(registry, path);
	sett.setTitle(newFolder.getTitle());
	sett.setOrderIndex(newFolder.getOrderIndex());
	sett.setParentId(parent.id);
	try {
	    sett.setProperties(newFolder.getPropertiesAsString());
	}
	catch(IOException e)
	{
	    throw new PimException(e);
	}
	return loadById(newId);
    }

    @Override public MailFolder getRoot() throws PimException
    {
	final Folder[] folders = loadAllFolders();
	for(Folder f: folders)
	    if (f.id == f.getParentId())
		return f;
	return null;
    }

    @Override public MailFolder[] load(MailFolder folder) throws PimException
    {
	NullCheck.notNull(folder, "folder");
	final Folder parent = (Folder)folder;
	final List<Folder> res = new LinkedList();
	final Folder[] folders = loadAllFolders();
	for(Folder f: folders)
	    if (f.getParentId() == parent.id && f.id != f.getParentId())
		res.add(f);
	return res.toArray(new MailFolder[res.size()]);
    }

    @Override public String getUniRef(MailFolder folder) throws PimException
    {
	NullCheck.notNull(folder, "folder");
	final Folder folderReg = (Folder)folder;
	return FolderUniRefProc.PREFIX + ":" + folderReg.id;
    }

    @Override public MailFolder loadByUniRef(String uniRef) throws PimException
    {
	NullCheck.notEmpty(uniRef, "uniRef");
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
	final Folder folder = new Folder(registry, id);
	return folder.load()?folder:null;
    }

    private Folder[] loadAllFolders() throws PimException
    {
	if (this.cache != null)
	    return this.cache;
	registry.addDirectory(org.luwrain.pim.mail.Settings.FOLDERS_PATH);
	final String[] subdirs = registry.getDirectories(org.luwrain.pim.mail.Settings.FOLDERS_PATH);
	final List<Folder> folders = new LinkedList();
	for(String s: subdirs)
	{
	    if (s.isEmpty())
		continue;
	    final int id;
	    try {
		id = Integer.parseInt(s);
	    }
	    catch (NumberFormatException e)
	    {
		Log.warning(LOG_COMPONENT, "the mail folder with unparsable name \'" + s + "\'");
		continue;
	    }
	    final Folder f = new Folder(registry, id);
	    if (f.load())
		folders.add(f);
	}
	this.cache = folders.toArray(new Folder[folders.size()]);
	Arrays.sort(cache);
	return this.cache;
    }
}
