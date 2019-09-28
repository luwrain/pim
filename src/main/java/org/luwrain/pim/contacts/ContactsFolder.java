
package org.luwrain.pim.contacts;

public class ContactsFolder implements Comparable
{
    public String title = "";
    public int orderIndex = 0;

    @Override public String toString()
    {
	return title;
    }

    @Override public int compareTo(Object o)
    {
	if (o == null || !(o instanceof ContactsFolder))
	    return 0;
	final ContactsFolder folder = (ContactsFolder)o;
	if (orderIndex < folder.orderIndex)
	    return -1;
	if (orderIndex > folder.orderIndex)
	    return 1;
	return 0;
    }

    @Override public boolean equals(Object o)
    {
	if (o == null || !(o instanceof ContactsFolder))
	    return false;
	final ContactsFolder folder = (ContactsFolder)o;
	return title.equals(folder.title);
}
}
