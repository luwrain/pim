
package org.luwrain.pim.contacts.json;

import java.io.*;
import org.dizitart.no2.*;

import org.luwrain.core.*;
import org.luwrain.pim.contacts.*;

public final class Storing implements ContactsStoring
{
    Folder root = new Folder();
    private final Folders folders;

    public Storing(File file)
    {
	this.folders = new Folders(this);
    }

        @Override public ContactsFolders getFolders()
    {
	return this.folders;
    }

    @Override public Contacts getContacts()
    {
	return null;
    }
}
