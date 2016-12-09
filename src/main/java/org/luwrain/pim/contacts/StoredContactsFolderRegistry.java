
package org.luwrain.pim.contacts;

import org.luwrain.core.*;
import org.luwrain.pim.*;

class StoredContactsFolderRegistry extends ContactsFolder implements StoredContactsFolder
{
    private final Registry registry;
    private final Settings.Folder sett;

    final int id;
    int parentId; 

    StoredContactsFolderRegistry(Registry registry, int id)
    {
	NullCheck.notNull(registry, "registry");
	this.registry = registry;
	this.id = id;
	this.sett = Settings.createFolder(registry, Registry.join(Settings.FOLDERS_PATH, "" + id));
    }

    @Override public String getTitle() throws PimException
    {
	return title;
    }

    @Override public void setTitle(String value) throws PimException
    {
	NullCheck.notNull(value, "value");
	sett.setTitle(value);
	this.title = value;
    }

    @Override public int getOrderIndex() throws PimException
    {
	return orderIndex;
    }

    @Override public void setOrderIndex(int value) throws PimException
    {
	sett.setOrderIndex(value);
	this.orderIndex = value;
}

    @Override public boolean isRoot()
    {
	return id == parentId;
    }

    void setParentId(int value) throws Exception
    {
	sett.setParentId(value);
	this.parentId = value;
    }

    boolean load()
    {
	title = sett.getTitle("");
	orderIndex = sett.getOrderIndex(0);
	parentId = sett.getParentId(-1);
	if (title.isEmpty() || parentId < 0)
	    return false;
	if (orderIndex < 0)
	    orderIndex = 0;
	return true;
    }

    @Override public boolean equals(Object o)
    {
	if (o == null || !(o instanceof StoredContactsFolderRegistry))
	    return false;
	final StoredContactsFolderRegistry folder = (StoredContactsFolderRegistry)o;
	return id == folder.id;
    }

    /*
    String getPath()
    {
return Registry.join(Settings.FOLDERS_PATH, "" + id);
    }

    private void updateError(String param) throws PimException
    {
	throw new PimException("Unable to update in the registry " + getPath() + "/" + param);
    }
    */
}
