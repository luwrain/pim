
package org.luwrain.pim.contacts.json;

import java.io.*;
import com.google.gson.*;

import org.luwrain.core.*;
import org.luwrain.pim.contacts.*;
import org.luwrain.util.*;

public final class Storing implements ContactsStoring
{
    private final Gson gson = new Gson();
    private final File file;
    Folder root = new Folder();
    private final Folders folders;
    private final Contacts contacts;

    public Storing(File file) throws IOException
    {
	NullCheck.notNull(file, "file");
	this.file = file;
	this.folders = new Folders(this);
	this.contacts = new Contacts(this);
	try (final BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
	    this.root = gson.fromJson(r, Folder.class);
	}
    }

    void save()
    {
	try {
	    try (final BufferedWriter w = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)))) {
		gson.toJson(root, w);
		w.flush();
	    }
	}
	catch(IOException e)
	{
	    throw new RuntimeException(e);
	}
    }

    @Override public ContactsFolders getFolders()
    {
	return this.folders;
    }

    @Override public Contacts getContacts()
    {
	return this.contacts;
    }
}
