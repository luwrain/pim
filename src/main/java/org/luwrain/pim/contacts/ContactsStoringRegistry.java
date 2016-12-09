/*
   Copyright 2012-2015 Michael Pozhidaev <michael.pozhidaev@gmail.com>
   Copyright 2015 Roman Volovodov <gr.rPman@gmail.com>

   This file is part of the LUWRAIN.

   LUWRAIN is free software; you can redistribute it and/or
   modify it under the terms of the GNU General Public
   License as published by the Free Software Foundation; either
   version 3 of the License, or (at your option) any later version.

   LUWRAIN is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
   General Public License for more details.
*/

package org.luwrain.pim.contacts;

import java.util.*;

import org.luwrain.core.*;
import org.luwrain.pim.*;

abstract class ContactsStoringRegistry implements ContactsStoring
{
    private Registry registry;

    public ContactsStoringRegistry(Registry registry)
    {
	this.registry = registry;
	if (registry == null)
	    throw new NullPointerException("registry may not be null");
    }

    @Override public StoredContactsFolder getFoldersRoot() throws PimException
    {
	final StoredContactsFolderRegistry[] folders = loadAllFolders();
	for(StoredContactsFolderRegistry f: folders)
	    if (f.id == f.parentId)
		return f;
	return null;
    }

    @Override public StoredContactsFolder[] getFolders(StoredContactsFolder folder) throws PimException
    {
	NullCheck.notNull(folder, "folder");
	if (!(folder instanceof StoredContactsFolderRegistry))
	    return new StoredContactsFolder[0];
	final StoredContactsFolderRegistry parent = (StoredContactsFolderRegistry)folder;
	final LinkedList<StoredContactsFolder> res = new LinkedList<StoredContactsFolder>();
	for(StoredContactsFolderRegistry f: loadAllFolders())
	    if (f.parentId == parent.id && f.id != f.parentId)
		res.add(f);
	return res.toArray(new StoredContactsFolder[res.size()]);
    }

    @Override public void saveFolder(StoredContactsFolder addTo, ContactsFolder folder) throws PimException
    {
	NullCheck.notNull(addTo, "addTo");
	NullCheck.notNull(folder, "folder");
	NullCheck.notNull(folder.title, "folder.title");
	if (folder.title.isEmpty())
	    throw new IllegalArgumentException("folder.title may not be null");
	final StoredContactsFolderRegistry parentFolder = (StoredContactsFolderRegistry)addTo;
	final int newId = newFolderId();
	final String newPath = Registry.join(Settings.FOLDERS_PATH, "" + newId);
	if (!registry.addDirectory(newPath))
	    throw new PimException("Unable to add to the registry new directory " + newPath);
	if (!registry.setString(Registry.join(newPath, "title"), folder.title))
	    throw new PimException("Unable to add to the registry new string value " + newPath + "/title");
	if (!registry.setInteger(Registry.join(newPath, "order-index"), folder.orderIndex))
	    throw new PimException("Unable to add to the registry new integer value " + newPath + "/order-index");
	if (!registry.setInteger(Registry.join(newPath, "parent-id"), parentFolder.id))
	    throw new PimException("Unable to add to the registry new integer value " + newPath + "/parent-id");
    }

    @Override public void deleteFolder(StoredContactsFolder folder) throws PimException
    {
	NullCheck.notNull(folder, "folder");
	if (!(folder instanceof StoredContactsFolderRegistry))
	    throw new IllegalArgumentException("folder is not an instance of StoredContactsFolderRegistry");
	final StoredContactsFolderRegistry folderRegistry = (StoredContactsFolderRegistry)folder;
	final String path = Registry.join(Settings.FOLDERS_PATH, "" + folderRegistry.id);
	if (!registry.deleteDirectory(path))
	    throw new PimException("Unable to delete the registry directory " + path);
    }

    private StoredContactsFolderRegistry[] loadAllFolders()
    {
	registry.addDirectory(Settings.FOLDERS_PATH);
	final LinkedList<StoredContactsFolderRegistry> folders = new LinkedList<StoredContactsFolderRegistry>();
	for(String s: registry.getDirectories(Settings.FOLDERS_PATH))
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
	    final StoredContactsFolderRegistry f = new StoredContactsFolderRegistry(registry, id);
	    if (f.load())
		folders.add(f);
	}
	Log.debug("pim-contacts", "loaded " + folders.size() + " folders");
	return folders.toArray(new StoredContactsFolderRegistry[folders.size()]);
    }

    private int newFolderId()//FIXME:
    {
	final String[] values = registry.getDirectories(Settings.FOLDERS_PATH);
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
