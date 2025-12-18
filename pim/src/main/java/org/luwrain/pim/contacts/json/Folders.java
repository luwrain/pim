// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.pim.contacts.json;

import java.util.*;

import org.luwrain.core.*;
import org.luwrain.pim.contacts.*;

final class Folders implements ContactsFolders
{
    private final Storing storing;

    Folders(Storing storing)
    {
	NullCheck.notNull(storing, "storing");
	this.storing = storing;
    }

        @Override public ContactsFolder getRoot()
    {
	return storing.root;
    }

    @Override public ContactsFolder[] load(ContactsFolder folder)
    {
	final Folder f = (Folder)folder;
	if (f.subfolders == null)
	    return new ContactsFolder[0];
	return f.subfolders.toArray(new Folder[f.subfolders.size()]);
    }

    @Override public void save(ContactsFolder addTo, ContactsFolder folder)
    {
	NullCheck.notNull(addTo, "addTo");
	NullCheck.notNull(folder, "folder");
	final Folder ff = (Folder)addTo;
	if (ff.subfolders == null)
	    ff.subfolders = new ArrayList<>();
	final Folder f = new Folder();
	if (folder.getTitle() != null)
	f.setTitle(folder.getTitle());
	f.setOrderIndex(folder.getOrderIndex());
	ff.subfolders.add(f);
	storing.save();
    }

    @Override public void delete(ContactsFolder folder)
    {
    }
}
