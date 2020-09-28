
package org.luwrain.pim.contacts.nitrite;

import java.io.*;
import java.util.*;
import org.dizitart.no2.*;
import org.dizitart.no2.objects.*;
import org.dizitart.no2.objects.Cursor;

import org.luwrain.core.*;
import org.luwrain.pim.*;
import org.luwrain.pim.contacts.*;

final class Folders implements ContactsFolders
{
    private final Nitrite db;
    //    private final NitriteCollection coll;
    private final ObjectRepository<Folder> repo;

    Folders(Nitrite db)
    {
	NullCheck.notNull(db, "db");
	this.db = db;
	//	this.coll = this.db.getCollection("folders");
	this.repo = this.db.getRepository(Folder.class);
    }

        @Override public ContactsFolder getRoot() throws PimException
    {
	return null;
    }

    @Override public ContactsFolder[] load(ContactsFolder folder) throws PimException
    {
final Cursor<Folder> c = this.repo.find();
final List<Folder> res = new LinkedList();
for(Folder f: c)
    res.add(f);
return res.toArray(new Folder[res.size()]);
    }

    @Override public void save(ContactsFolder addTo, ContactsFolder folder) throws PimException
    {
	final Folder f = new Folder();
	f.setTitle(folder.getTitle());
	f.setOrderIndex(folder.getOrderIndex());
	this.repo.insert(f);
    }

    @Override public void delete(ContactsFolder folder) throws PimException
    {
    }
}
