
package org.luwrain.pim.contacts.json;

import java.util.*;

import org.luwrain.core.*;

final class Contacts implements org.luwrain.pim.contacts.Contacts
{
    private final Storing storing;

    Contacts(Storing storing)
    {
	NullCheck.notNull(storing, "storing");
	this.storing = storing;
    }

    @Override public org.luwrain.pim.contacts.Contact[] load(org.luwrain.pim.contacts.ContactsFolder folder)
    {
	final Folder f = (Folder)folder;
	if (f.contacts == null)
	    return new org.luwrain.pim.contacts.Contact[0];
	return f.contacts.toArray(new org.luwrain.pim.contacts.Contact[f.contacts.size()]);
    }

    @Override public void save(org.luwrain.pim.contacts.ContactsFolder folder, org.luwrain.pim.contacts.Contact contact)
    {
	final Folder f = (Folder)folder;
	final Contact c = new Contact();
	if (contact.getTitle() != null)
	    c.setTitle(contact.getTitle());
	if (f.contacts == null)
	    f.contacts = new ArrayList<Contact>();
	f.contacts.add(c);
	storing.save();
    }

    @Override public void delete(org.luwrain.pim.contacts.Contact contact)
    {
    }
}
