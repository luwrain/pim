
package org.luwrain.pim.mail;

import org.luwrain.core.Registry;

class StoredMailFolderRegistry implements StoredMailFolder, Comparable
{
    private Registry registry;

    public long id;
    public long parentId;
    public String title;
    public int orderIndex;

    public StoredMailFolderRegistry(Registry registry)
    {
	this.registry = registry;
	if (registry == null)
	    throw new NullPointerException("registry may not be null");
    }

    @Override public String getTitle() throws Exception
    {
	return title != null?title:"";
    }

    @Override public void setTitle(String value) throws Exception
    {
	//FIXME:
    }

    @Override public int getOrderIndex() throws Exception
    {
	return orderIndex;
    }

    @Override public void setOrderIndex(int value) throws Exception
    {
	//FIXME:
    }

    @Override public String toString()
    {
	return title != null?title:"";
    }

    @Override public boolean equals(Object o)
    {
	if (o == null || !(o instanceof StoredMailFolderRegistry))
	    return false;
	final StoredMailFolderRegistry folder = (StoredMailFolderRegistry)o;
	return id == folder.id;
    }

    @Override public int compareTo(Object o)
    {
	if (o == null || !(o instanceof StoredMailFolderRegistry))
	    return 0;
	final StoredMailFolderRegistry folder = (StoredMailFolderRegistry)o;
	if (orderIndex < folder.orderIndex)
	    return -1;
	if (orderIndex > folder.orderIndex)
	    return 1;
	return 0;
    }
}
