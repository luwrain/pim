
package org.luwrain.pim.contacts;

import org.luwrain.pim.*;

public interface ContactsStoring
{
    StoredContactsFolder getFoldersRoot() throws PimException;
    StoredContactsFolder[] getFolders(StoredContactsFolder folder) throws PimException;
    void saveFolder(StoredContactsFolder addTo, ContactsFolder folder) throws PimException;
    void deleteFolder(StoredContactsFolder folder) throws PimException;
    StoredContact[] loadContacts(StoredContactsFolder folder) throws PimException;
    void saveContact(StoredContactsFolder folder, Contact contact) throws PimException;
    void deleteContact(StoredContact contact) throws PimException;
}
