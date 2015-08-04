
package org.luwrain.pim.email;

import org.luwrain.core.Registry;

class StoredEmailFolderRegistry implements StoredEmailFolder, Comparable
{
    private Registry registry;

    public long id;
    public long parentId;
    public String title;
    public int orderIndex;

    public StoredEmailFolderRegistry(Registry registry)
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
	if (o == null || !(o instanceof StoredEmailFolderRegistry))
	    return false;
	final StoredEmailFolderRegistry folder = (StoredEmailFolderRegistry)o;
	return id == folder.id;
    }

    @Override public int compareTo(Object o)
    {
	if (o == null || !(o instanceof StoredEmailFolderRegistry))
	    return 0;
	final StoredEmailFolderRegistry folder = (StoredEmailFolderRegistry)o;
	if (orderIndex < folder.orderIndex)
	    return -1;
	if (orderIndex > folder.orderIndex)
	    return 1;
	return 0;
    }
}
