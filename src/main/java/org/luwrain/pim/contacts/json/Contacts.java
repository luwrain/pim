
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
	return null;
    }

    @Override public void save(org.luwrain.pim.contacts.ContactsFolder folder, org.luwrain.pim.contacts.Contact contact)
    {
    }

    @Override public void delete(org.luwrain.pim.contacts.Contact contact)
    {
    }
}
