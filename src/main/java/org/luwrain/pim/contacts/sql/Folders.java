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

package org.luwrain.pim.contacts.sql;

import java.util.*;

import org.luwrain.core.*;
import org.luwrain.pim.*;
import org.luwrain.pim.contacts.*;

class Folders implements ContactsFolders
{
    private final Registry registry;

    Folders(Registry registry)
    {
	NullCheck.notNull(registry, "registry");
	this.registry = registry;
    }

    @Override public ContactsFolder getRoot()
    {
	final Folder[] folders = loadAllFolders();
	for(Folder f: folders)
	    if (f.id == f.parentId)
		return f;
	return null;
    }

    @Override public ContactsFolder[] load(ContactsFolder folder)
    {
	NullCheck.notNull(folder, "folder");
	final Folder parent = (Folder)folder;
	final List<ContactsFolder> res = new LinkedList();
	for(Folder f: loadAllFolders())
	    if (f.parentId == parent.id && f.id != f.parentId)
		res.add(f);
	return res.toArray(new ContactsFolder[res.size()]);
    }

    @Override public void save(ContactsFolder addTo, ContactsFolder folder) throws PimException
    {
	NullCheck.notNull(addTo, "addTo");
	NullCheck.notNull(folder, "folder");
	if (folder.getTitle().isEmpty())
	    throw new IllegalArgumentException("folder.title may not be null");
	final Folder parentFolder = (Folder)addTo;
	final int newId = newFolderId();
	final String newPath = Registry.join(org.luwrain.pim.contacts.Settings.FOLDERS_PATH, "" + newId);
	if (!registry.addDirectory(newPath))
	    throw new PimException("Unable to add to the registry new directory " + newPath);
	if (!registry.setString(Registry.join(newPath, "title"), folder.getTitle()))
	    throw new PimException("Unable to add to the registry new string value " + newPath + "/title");
	if (!registry.setInteger(Registry.join(newPath, "order-index"), folder.getOrderIndex()))
	    throw new PimException("Unable to add to the registry new integer value " + newPath + "/order-index");
	if (!registry.setInteger(Registry.join(newPath, "parent-id"), parentFolder.id))
	    throw new PimException("Unable to add to the registry new integer value " + newPath + "/parent-id");
    }

    @Override public void delete(ContactsFolder folder) throws PimException
    {
	NullCheck.notNull(folder, "folder");
	final Folder folderRegistry = (Folder)folder;
	final String path = Registry.join(org.luwrain.pim.contacts.Settings.FOLDERS_PATH, "" + folderRegistry.id);
	if (!registry.deleteDirectory(path))
	    throw new PimException("Unable to delete the registry directory " + path);
    }

    private Folder[] loadAllFolders()
    {
	registry.addDirectory(org.luwrain.pim.contacts.Settings.FOLDERS_PATH);
	final List<Folder> folders = new LinkedList();
	for(String s: registry.getDirectories(org.luwrain.pim.contacts.Settings.FOLDERS_PATH))
	{
	    if (s.isEmpty())
		continue;
	    int id = 0;
	    try {
		id = Integer.parseInt(s);
	    }
	    catch (NumberFormatException e)
	    {
		Log.error("pim-contacts", "invalid contact folder directory name:" + s);
		continue;
	    }
	    final Folder f = new Folder(registry, id);
	    if (f.load())
		folders.add(f);
	}
	return folders.toArray(new Folder[folders.size()]);
    }

    private int newFolderId()//FIXME:
    {
	final String[] values = registry.getDirectories(org.luwrain.pim.contacts.Settings.FOLDERS_PATH);
	int res = 0;
	for(String s: values)
	{
	    if (s == null || s.isEmpty())
		continue;
	    int value = 0;
	    try {
		value = Integer.parseInt(s);
	    }
	    catch (NumberFormatException e)
	    {
		e.printStackTrace();
		continue;
	    }
	    if (value > res)
		res = value;
	}
	return res + 1;
    }
}
