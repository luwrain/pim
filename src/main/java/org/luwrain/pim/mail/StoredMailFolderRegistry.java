
package org.luwrain.pim.mail;

import org.luwrain.core.*;
import org.luwrain.pim.*;

class StoredMailFolderRegistry extends MailFolder implements StoredMailFolder
{
    private final Registry registry;
    private final Settings.Folder sett;

    final int id;
    int parentId = 0;

    StoredMailFolderRegistry(Registry registry, int id)
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

    @Override public boolean equals(Object o)
    {
	if (o == null || !(o instanceof StoredMailFolderRegistry))
	    return false;
	final StoredMailFolderRegistry folder = (StoredMailFolderRegistry)o;
	return id == folder.id;
    }

    boolean load()
    {
	this.title = sett.getTitle("");
	this.orderIndex = sett.getOrderIndex(0);
	this.parentId = sett.getParentId(0);
	if (title.isEmpty() || parentId < 0)
	    return false;
	if (orderIndex < 0)
	    orderIndex = 0;
	return true;
    }
}
