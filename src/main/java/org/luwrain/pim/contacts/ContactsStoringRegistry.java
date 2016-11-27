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

import org.luwrain.core.Registry;
import org.luwrain.core.NullCheck;
//import org.luwrain.util.RegistryAutoCheck;
import org.luwrain.pim.RegistryKeys;

abstract class ContactsStoringRegistry implements ContactsStoring
{
    private final RegistryKeys registryKeys = new RegistryKeys();
    private Registry registry;

    public ContactsStoringRegistry(Registry registry)
    {
	this.registry = registry;
	if (registry == null)
	    throw new NullPointerException("registry may not be null");
    }

    @Override public StoredContactsFolder getFoldersRoot() throws Exception
    {
	final StoredContactsFolderRegistry[] folders = loadAllFolders();
	for(StoredContactsFolderRegistry f: folders)
	    if (f.id == f.parentId)
		return f;
	return null;
    }

    @Override public StoredContactsFolder[] getFolders(StoredContactsFolder folder) throws Exception
    {
	if (folder == null || !(folder instanceof StoredContactsFolderRegistry))
	    return null;
	final StoredContactsFolderRegistry parent = (StoredContactsFolderRegistry)folder;
	final LinkedList<StoredContactsFolder> res = new LinkedList<StoredContactsFolder>();
	final StoredContactsFolderRegistry[] folders = loadAllFolders();
	for(StoredContactsFolderRegistry f: folders)
	    if (f.parentId == parent.id && f.id != f.parentId)
		res.add(f);
	return res.toArray(new StoredContactsFolder[res.size()]);
    }

    @Override public void saveFolder(StoredContactsFolder addTo, ContactsFolder folder) throws Exception
    {
	NullCheck.notNull(addTo, "addTo");
	NullCheck.notNull(folder, "folder");
	NullCheck.notNull(folder.title, "folder.title");
	if (folder.title.isEmpty())
	    throw new IllegalArgumentException("folder.title may not be null");
	final StoredContactsFolderRegistry parentFolder = (StoredContactsFolderRegistry)addTo;
	final int newId = newFolderId();
	final String newPath = Registry.join(registryKeys.contactsFolders(), "" + newId);
	if (!registry.addDirectory(newPath))
	    throw new Exception("Unable to add to the registry new directory " + newPath);
	if (!registry.setString(Registry.join(newPath, "title"), folder.title))
	    throw new Exception("Unable to add to the registry new string value " + newPath + "/title");
	if (!registry.setInteger(Registry.join(newPath, "order-index"), folder.orderIndex))
	    throw new Exception("Unable to add to the registry new integer value " + newPath + "/order-index");
	if (!registry.setInteger(Registry.join(newPath, "parent-id"), parentFolder.id))
	    throw new Exception("Unable to add to the registry new integer value " + newPath + "/parent-id");
    }

    @Override public void deleteFolder(StoredContactsFolder folder) throws Exception
    {
	NullCheck.notNull(folder, "folder");
	if (!(folder instanceof StoredContactsFolderRegistry))
	    throw new IllegalArgumentException("folder is not an instance of StoredContactsFolderRegistry");
	final StoredContactsFolderRegistry folderRegistry = (StoredContactsFolderRegistry)folder;
	final String path = Registry.join(registryKeys.contactsFolders(), "" + folderRegistry.id);
	if (!registry.deleteDirectory(path))
	    throw new Exception("Unable to delete the registry directory " + path);
    }

    private StoredContactsFolderRegistry[] loadAllFolders()
    {
	final String[] subdirs = registry.getDirectories(registryKeys.contactsFolders());
	if (subdirs == null || subdirs.length < 1)
	    return new StoredContactsFolderRegistry[0];
	final LinkedList<StoredContactsFolderRegistry> folders = new LinkedList<StoredContactsFolderRegistry>();
	for(String s: subdirs)
	{
	    if (s == null || s.isEmpty())
		continue;
	    int id = 0;
	    try {
		id = Integer.parseInt(s);
	    }
	    catch (NumberFormatException e)
	    {
		e.printStackTrace();
		continue;
	    }
	    final StoredContactsFolderRegistry f = new StoredContactsFolderRegistry(registry);
	    f.id = id;
	    if (f.load())
		folders.add(f);
	}
	return folders.toArray(new StoredContactsFolderRegistry[folders.size()]);
    }

    private int newFolderId()//FIXME:
    {
	final String[] values = registry.getDirectories(registryKeys.contactsFolders());
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
