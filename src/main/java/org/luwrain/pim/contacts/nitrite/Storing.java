
package org.luwrain.pim.contacts.nitrite;

import java.io.*;
import org.dizitart.no2.*;

import org.luwrain.core.*;
import org.luwrain.pim.contacts.*;

final class Storing implements ContactsStoring
{
    final Nitrite  db;
    private final Folders folders;

    Storing(File file)
    {
	this.db = Nitrite.builder()
        .compressed()
        .filePath(file.getAbsolutePath())
        .openOrCreate("luwrain", "passwd");
	this.folders = new Folders(this.db);
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
