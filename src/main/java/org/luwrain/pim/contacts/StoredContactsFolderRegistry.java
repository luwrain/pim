
package org.luwrain.pim.contacts;

import org.luwrain.core.*;
import org.luwrain.pim.*;

class StoredContactsFolderRegistry extends ContactsFolder implements StoredContactsFolder
{
    private Registry registry;

    int id;
    int parentId; 

    StoredContactsFolderRegistry(Registry registry)
    {
	this.registry = registry;
	NullCheck.notNull(registry, "registry");
    }

    @Override public String getTitle() throws PimException
    {
	return title != null?title:"";
    }

    @Override public void setTitle(String value) throws PimException
    {
	NullCheck.notNull(value, "value");
	if (!registry.setString(Registry.join(getPath(), "title"), value))
	    updateError("title");
	title = value;
    }

    @Override public int getOrderIndex() throws PimException
    {
	return orderIndex;
    }

    @Override public void setOrderIndex(int value) throws PimException
    {
	if (!registry.setInteger(Registry.join(getPath(), "order-index"), value))
	    updateError("order-index");
	orderIndex = value;
    }

    @Override public boolean isRoot()
    {
	return id == parentId;
    }

    void setParentId(int value) throws Exception
    {
	if (!registry.setInteger(Registry.join(getPath(), "parent-id"), value))
	    updateError("parent-id");
	parentId = value;
    }

    boolean load()
    {
	/*
	final RegistryAutoCheck check = new RegistryAutoCheck(registry);
	final String path = getPath();
	title = check.stringNotEmpty(Registry.join(path, "title"), "");
	orderIndex = check.intPositive(Registry.join(path, "order-index"), -1);
	parentId = check.intPositive(Registry.join(path, "parent-id"), -1);
	if (title.isEmpty() || parentId < 0)
	    return false;
	if (orderIndex < 0)
	    orderIndex = 0;
	*/
	return true;
    }

    @Override public boolean equals(Object o)
    {
	if (o == null || !(o instanceof StoredContactsFolderRegistry))
	    return false;
	final StoredContactsFolderRegistry folder = (StoredContactsFolderRegistry)o;
	return id == folder.id;
    }

    String getPath()
    {
return Registry.join(Settings.FOLDERS_PATH, "" + id);
    }

    private void updateError(String param) throws PimException
    {
	throw new PimException("Unable to update in the registry " + getPath() + "/" + param);
    }
}
