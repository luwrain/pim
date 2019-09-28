
package org.luwrain.pim.contacts;

import org.luwrain.pim.*;

public interface Contacts
{
    Contact[] load(StoredContactsFolder folder) throws PimException;
    void save(StoredContactsFolder folder, org.luwrain.pim.contacts.Contact contact) throws PimException;
    void delete(org.luwrain.pim.contacts.Contact contact) throws PimException;
}
