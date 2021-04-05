
package org.luwrain.pim.contacts.json;

import java.util.*;

import org.luwrain.core.*;
import org.luwrain.pim.contacts.*;

final class Folders implements ContactsFolders
{
    private final Storing storing;

    Folders(Storing storing)
    {
	NullCheck.notNull(storing, "storing");
	this.storing = storing;
    }

        @Override public ContactsFolder getRoot()
    {
	return storing.root;
    }

    @Override public ContactsFolder[] load(ContactsFolder folder)
    {
	final Folder f = (Folder)folder;
	if (f.children == null)
	    return new ContactsFolder[0];
	return f.children.toArray(new Folder[f.children.size()]);
    }

    @Override public void save(ContactsFolder addTo, ContactsFolder folder)
    {
	final Folder f = new Folder();
	f.setTitle(folder.getTitle());
	f.setOrderIndex(folder.getOrderIndex());
    }

    @Override public void delete(ContactsFolder folder)
    {
    }
}
