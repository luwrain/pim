
package org.luwrain.pim.mail.sql;

import org.luwrain.core.*;
import org.luwrain.pim.*;
import org.luwrain.pim.mail.*;

class Folder extends MailFolder implements StoredMailFolder
{
    private final Registry registry;
    private final org.luwrain.pim.mail.Settings.Folder sett;

    public final int id;//FIXME:not public
    int parentId = 0;

    Folder(Registry registry, int id)
    {
	NullCheck.notNull(registry, "registry");
	this.registry = registry;
	this.id = id;
	this.sett = org.luwrain.pim.mail.Settings.createFolder(registry, Registry.join(org.luwrain.pim.mail.Settings.FOLDERS_PATH, "" + id));
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
	if (o == null || !(o instanceof Folder))
	    return false;
	final Folder folder = (Folder)o;
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
