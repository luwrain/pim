/*
   Copyright 2012-2019 Michael Pozhidaev <msp@luwrain.org>
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
    private Folder[] cache = null;

    Folders(Registry registry)
    {
	NullCheck.notNull(registry, "registry");
	this.registry = registry;
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
	if (!(folder instanceof Folder))
	    throw new IllegalArgumentException("folder must be an instance of org.luwrain.pim.mail.sql.Folder");
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
	if (!(parentFolder instanceof Folder))
	    throw new IllegalArgumentException("parentFolder must be an instance of org.luwrain.pim.mail.sql.Folder");
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
	    if (f.id == f.parentId)
		return f;
	return null;
    }

    @Override public MailFolder[] load(MailFolder folder) throws PimException
    {
	NullCheck.notNull(folder, "folder");
	if (!(folder instanceof Folder))
	    return null;
	final Folder parent = (Folder)folder;
	final List<Folder> res = new LinkedList();
	final Folder[] folders = loadAllFolders();
	for(Folder f: folders)
	    if (f.parentId == parent.id && f.id != f.parentId)
		res.add(f);
	return res.toArray(new MailFolder[res.size()]);
    }

    @Override public String getUniRef(MailFolder folder) throws PimException
    {
	if (folder == null || !(folder instanceof Folder))
	    return "";
	final Folder folderRegistry = (Folder)folder;
	return FolderUniRefProc.PREFIX + ":" + folderRegistry.id;
    }

    @Override public MailFolder loadByUniRef(String uniRef) throws PimException
    {
	if (uniRef == null || uniRef.length() < FolderUniRefProc.PREFIX.length() + 2)
	    return null;
	if (!uniRef.startsWith(FolderUniRefProc.PREFIX + ":"))
	    return null;
	int id = 0;
	try {
	    id = Integer.parseInt(uniRef.substring(FolderUniRefProc.PREFIX.length() + 1));
	}
	catch (NumberFormatException e)
	{
	    e.printStackTrace();
	    return null;
	}
	final Folder folder = new Folder(registry, id);
	if (folder.load())
	    return folder;
	return null;
    }

    private Folder[] loadAllFolders() throws PimException
    {
	if (cache != null)
	    return cache;
	registry.addDirectory(org.luwrain.pim.mail.Settings.FOLDERS_PATH);
	final String[] subdirs = registry.getDirectories(org.luwrain.pim.mail.Settings.FOLDERS_PATH);
	if (subdirs.length == 0)
	    return new Folder[0];
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
	cache = folders.toArray(new Folder[folders.size()]);
	Arrays.sort(cache);
	return cache;
    }
    }
