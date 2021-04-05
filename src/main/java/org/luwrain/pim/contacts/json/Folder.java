
package org.luwrain.pim.contacts.json;

import java.util.*;

import org.luwrain.pim.contacts.*;

final class Folder extends ContactsFolder
{
    List<Folder> children = new ArrayList<Folder>();
    List<Contact> contacts = new ArrayList<Contact>();
}
