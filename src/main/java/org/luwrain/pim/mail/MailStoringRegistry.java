
package org.luwrain.pim.mail;

import java.util.*;

import org.luwrain.core.Registry;
import org.luwrain.util.RegistryAutoCheck;
import org.luwrain.util.RegistryPath;
import org.luwrain.pim.RegistryKeys;

abstract class MailStoringRegistry implements MailStoring
{
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
