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

package org.luwrain.pim.mail;

import java.util.*;

import org.luwrain.core.Registry;
import org.luwrain.util.RegistryAutoCheck;
import org.luwrain.util.RegistryPath;
import org.luwrain.pim.RegistryKeys;

abstract class MailStoringRegistry implements MailStoring
{
    static private final String FOLDER_UNIREF_PREFIX = "mailfolder:";
    private static final String LOG_FACILITY = "pim.email";

    private Registry registry;
    private RegistryKeys registryKeys = new RegistryKeys();

    public MailStoringRegistry(Registry registry)
    {
	this.registry = registry;
	if (registry == null)
	    throw new NullPointerException("registry may not be null");
    }

    @Override public StoredMailFolder getFoldersRoot() throws Exception
    {
	final StoredMailFolderRegistry[] folders = loadFolders();
	for(StoredMailFolderRegistry f: folders)
	    if (f.id == f.parentId)
		return f;
	return null;
    }

    @Override public StoredMailFolder[] getFolders(StoredMailFolder folder) throws Exception
    {
	if (folder == null || !(folder instanceof StoredMailFolderRegistry))
	    return null;
	final StoredMailFolderRegistry parent = (StoredMailFolderRegistry)folder;
	final LinkedList<StoredMailFolder> res = new LinkedList<StoredMailFolder>();
	final StoredMailFolderRegistry[] folders = loadFolders();
	for(StoredMailFolderRegistry f: folders)
	    if (f.parentId == parent.id && f.id != f.parentId)
		res.add(f);
	return res.toArray(new StoredMailFolder[res.size()]);
    }

    @Override public StoredMailFolder getFolderByUniRef(String uniRef)
    {
	if (uniRef == null || uniRef.length() < FOLDER_UNIREF_PREFIX.length() + 1)
	    return null;
	if (!uniRef.startsWith(FOLDER_UNIREF_PREFIX))
	    return null;
	return readFolder(uniRef.substring(FOLDER_UNIREF_PREFIX.length()));
    }

    private StoredMailFolderRegistry[] loadFolders()
    {
	final String[] subdirs = registry.getDirectories(registryKeys.mailFolders());
	if (subdirs == null || subdirs.length < 1)
	    return new StoredMailFolderRegistry[0];
	final LinkedList<StoredMailFolderRegistry> folders = new LinkedList<StoredMailFolderRegistry>();
	for(String s: subdirs)
	{
	    if (s == null || s.isEmpty())
		continue;
	    final StoredMailFolderRegistry f = readFolder(s);
	    if (f != null)
		folders.add(f);
	}
	final StoredMailFolderRegistry[] res = folders.toArray(new StoredMailFolderRegistry[folders.size()]);
	Arrays.sort(res);
	return res;
    }

    @Override public StoredMailAccount[] loadAccounts() throws Exception
    {
	final String[] subdirs = registry.getDirectories(registryKeys.mailAccounts());
	if (subdirs == null || subdirs.length < 1)
	    return new StoredMailAccount[0];
	final LinkedList<StoredMailAccountRegistry> accounts = new LinkedList<StoredMailAccountRegistry>();
	for(String s: subdirs)
	{
	    if (s == null || s.isEmpty())
		continue;
	    long id = 0;
	    try {
		id = Integer.parseInt(s);
	    }
	    catch(NumberFormatException e)
	    {
		continue;
	    }
	    final StoredMailAccountRegistry account = new StoredMailAccountRegistry(registry, id);
	    if (account.load())
		accounts.add(account);
	}
	final StoredMailAccountRegistry[] res = accounts.toArray(new StoredMailAccountRegistry[accounts.size()]);
	Arrays.sort(res);
	return res;
    }

    private StoredMailFolderRegistry readFolder(String name)
    {
	final StoredMailFolderRegistry folder = new StoredMailFolderRegistry(registry);
	try {
	    folder.id = Integer.parseInt(name.trim());
	}
	catch(NumberFormatException e)
	{
	    e.printStackTrace();
	    return null;
	}
	final RegistryAutoCheck check = new RegistryAutoCheck(registry, LOG_FACILITY);
	final String path = RegistryPath.join(registryKeys.mailFolders(), name);
	folder.title = check.stringNotEmpty(RegistryPath.join(path, "title"), "");
	folder.orderIndex = check.intPositive(RegistryPath.join(path, "order-index"), -1);
	folder.parentId = check.intPositive(RegistryPath.join(path, "parent-id"), -1);
	if (folder.title.isEmpty() || folder.parentId < 0)
	    return null;
	if (folder.orderIndex < 0)
	    folder.orderIndex = 0;
	return folder;
    }
}
