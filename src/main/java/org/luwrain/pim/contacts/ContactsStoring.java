
package org.luwrain.pim.contacts;

public interface ContactsStoring
{
    StoredContactsFolder getRootFolder() throws Exception;
    StoredContactsFolder[] getFolders(StoredContactsFolder folder) throws Exception;
    void saveFolder(StoredContactsFolder addTo, ContactsFolder folder) throws Exception;
    void deleteSFolder(StoredContactsFolder folder) throws Exception;
    StoredContact[] getContacts(StoredContactsFolder folder) throws Exception;
    void saveContact(StoredContactsFolder folder, Contact contact) throws Exception;
    void deleteContact(StoredContact contact) throws Exception;
}
