
package org.luwrain.pim.email;

import java.util.*;

import org.luwrain.core.Registry;
import org.luwrain.util.RegistryAutoCheck;
import org.luwrain.util.RegistryPath;
import org.luwrain.pim.RegistryKeys;

abstract class EmailStoringRegistry implements EmailStoring
{
    private static final String LOG_FACILITY = "pim.email";

    private Registry registry;
    private RegistryKeys registryKeys = new RegistryKeys();

    public EmailStoringRegistry(Registry registry)
    {
	this.registry = registry;
	if (registry == null)
	    throw new NullPointerException("registry may not be null");
    }

    @Override public StoredEmailFolder getFoldersRoot() throws Exception
    {
	final StoredEmailFolderRegistry[] folders = loadFolders();
	for(StoredEmailFolderRegistry f: folders)
	    if (f.id == f.parentId)
		return f;
	return null;
    }

    @Override public StoredEmailFolder[] getFolders(StoredEmailFolder folder) throws Exception
    {
	if (folder == null || !(folder instanceof StoredEmailFolderRegistry))
	    return null;
	final StoredEmailFolderRegistry parent = (StoredEmailFolderRegistry)folder;
	final LinkedList<StoredEmailFolder> res = new LinkedList<StoredEmailFolder>();
	final StoredEmailFolderRegistry[] folders = loadFolders();
	for(StoredEmailFolderRegistry f: folders)
	    if (f.parentId == parent.id && f.id != f.parentId)
		res.add(f);
	return res.toArray(new StoredEmailFolder[res.size()]);
    }

    private StoredEmailFolderRegistry[] loadFolders()
    {
	final String[] subdirs = registry.getDirectories(registryKeys.mailFolders());
	if (subdirs == null || subdirs.length < 1)
	    return new StoredEmailFolderRegistry[0];
	final LinkedList<StoredEmailFolderRegistry> folders = new LinkedList<StoredEmailFolderRegistry>();
	for(String s: subdirs)
	{
	    if (s == null || s.isEmpty())
		continue;
	    final StoredEmailFolderRegistry f = readFolder(s);
	    if (f != null)
		folders.add(f);
	}
final StoredEmailFolderRegistry[] res = folders.toArray(new StoredEmailFolderRegistry[folders.size()]);
Arrays.sort(res);
return res;
    }

    private StoredEmailFolderRegistry readFolder(String name)
    {
	final StoredEmailFolderRegistry folder = new StoredEmailFolderRegistry(registry);
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
