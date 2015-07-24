
package org.luwrain.pim.contacts;

import java.util.*;

import org.luwrain.core.Registry;
import org.luwrain.util.RegistryAutoCheck;
import org.luwrain.util.RegistryPath;
import org.luwrain.pim.RegistryKeys;

abstract class ContactsStoringRegistry implements ContactsStoring
{
    private static final String LOG_FACILITY = "pim.contacts";

    private Registry registry;
    private RegistryKeys registryKeys = new RegistryKeys();

    public ContactsStoringRegistry(Registry registry)
    {
	this.registry = registry;
	if (registry == null)
	    throw new NullPointerException("registry may not be null");
    }

    @Override public StoredContactsFolder getRootFolder() throws Exception
    {
	final StoredContactsFolderRegistry[] folders = loadFolders();
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
	final StoredContactsFolderRegistry[] folders = loadFolders();
	for(StoredContactsFolderRegistry f: folders)
	    if (f.parentId == parent.id)
		res.add(f);
	return res.toArray(new StoredContactsFolder[res.size()]);
    }

    @Override public void saveFolder(StoredContactsFolder addTo, ContactsFolder folder) throws Exception
    {
	//FIXME:
    }

    @Override public void deleteSFolder(StoredContactsFolder folder) throws Exception
    {
	//FIXME:
    }

    private StoredContactsFolderRegistry[] loadFolders()
    {
	final String[] subdirs = registry.getDirectories(registryKeys.contactsFolders());
	if (subdirs == null || subdirs.length < 1)
	    return new StoredContactsFolderRegistry[0];
	final LinkedList<StoredContactsFolderRegistry> folders = new LinkedList<StoredContactsFolderRegistry>();
	for(String s: subdirs)
	{
	    if (s == null || s.isEmpty())
		continue;
	    final StoredContactsFolderRegistry f = readFolder(s);
	    if (f != null)
		folders.add(f);
	}
	return folders.toArray(new StoredContactsFolderRegistry[folders.size()]);
    }

    private StoredContactsFolderRegistry readFolder(String name)
    {
	final StoredContactsFolderRegistry folder = new StoredContactsFolderRegistry(registry);
	try {
	    folder.id = Integer.parseInt(name.trim());
	}
	catch(NumberFormatException e)
	{
	    e.printStackTrace();
	    return null;
	}
	final RegistryAutoCheck check = new RegistryAutoCheck(registry, LOG_FACILITY);
	final String path = RegistryPath.join(registryKeys.contactsFolders(), name);
	folder.title = check.stringNotEmpty(RegistryPath.join(path, "name"), "");
	folder.orderIndex = check.intPositive(RegistryPath.join(path, "order-index"), -1);
	folder.parentId = check.intPositive(RegistryPath.join(path, "parent-id"), -1);
	if (folder.title.isEmpty() || folder.parentId < 0)
	    return null;
	if (folder.orderIndex < 0)
	    folder.orderIndex = 0;
	return folder;
    }
}
