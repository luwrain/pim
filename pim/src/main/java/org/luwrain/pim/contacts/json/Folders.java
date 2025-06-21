/*
   Copyright 2012-2021 Michael Pozhidaev <msp@luwrain.org>
   Copyright 2015 Roman Volovodov <gr.rPman@gmail.com>

   This file is part of LUWRAIN.

   LUWRAIN is free software; you can redistribute it and/or
   modify it under the terms of the GNU General Public
   License as published by the Free Software Foundation; either
   version 3 of the License, or (at your option) any later version.

   LUWRAIN is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
   General Public License for more details.
*/

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
