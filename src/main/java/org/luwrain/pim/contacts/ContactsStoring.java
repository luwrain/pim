
package org.luwrain.pim.contacts;

public interface ContactsStoring
{
    StoredContactsGroup getRootGroup() throws Exception;
    StoredContactsGroup[] getSubgroups(StoredContactsGroup group) throws Exception;
    void saveSubgroup(StoredContactsGroup addTo, ContactsGroup group) throws Exception;
    void deleteSubgroup(StoredContactsGroup subgroup) throws Exception;
    StoredContact[] getContacts(StoredContactsGroup subgroup) throws Exception;
    void saveContact(StoredContactsGroup subgroup, Contact contact) throws Exception;
    void deleteContact(StoredContact contact) throws Exception;
}
