
package org.luwrain.pim.contacts.sql;

import org.luwrain.core.*;
import org.luwrain.pim.*;
import org.luwrain.pim.contacts.*;

class Folder extends ContactsFolder implements StoredContactsFolder
{
    private final Registry registry;
    private final org.luwrain.pim.contacts.Settings.Folder sett;

    final int id;
    int parentId; 

    Folder(Registry registry, int id)
    {
	NullCheck.notNull(registry, "registry");
	this.registry = registry;
	this.id = id;
	this.sett = org.luwrain.pim.contacts.Settings.createFolder(registry, Registry.join(org.luwrain.pim.contacts.Settings.FOLDERS_PATH, "" + id));
    }

    @Override public String getTitle()
    {
	return title;
    }

    @Override public void setTitle(String value)
    {
	NullCheck.notNull(value, "value");
	sett.setTitle(value);
	this.title = value;
    }

    @Override public int getOrderIndex()
    {
	return orderIndex;
    }

    @Override public void setOrderIndex(int value)
    {
	if (value < 0)
	    throw new IllegalArgumentException("value (" + value + ") may not be negative");
	sett.setOrderIndex(value);
	this.orderIndex = value;
}

    @Override public boolean isRoot()
    {
	return id == parentId;
    }

    void setParentId(int value)
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
	if (o == null || !(o instanceof Folder))
	    return false;
	final Folder folder = (Folder)o;
	return id == folder.id;
    }
}
