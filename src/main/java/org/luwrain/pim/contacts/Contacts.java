
package org.luwrain.pim.contacts;

import org.luwrain.pim.*;

public interface Contacts
{
    StoredContact[] load(StoredContactsFolder folder) throws PimException;
    void save(StoredContactsFolder folder, Contact contact) throws PimException;
    void delete(StoredContact contact) throws PimException;
}
