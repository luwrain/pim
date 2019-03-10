/*
   Copyright 2012-2019 Michael Pozhidaev <michael.pozhidaev@gmail.com>
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

package org.luwrain.pim.mail.mem;

import java.util.*;

import org.luwrain.core.*;
import org.luwrain.pim.*;
import org.luwrain.pim.mail.*;

class Folders implements MailFolders
{
    private final List<Folder> folders = new LinkedList();

    @Override public StoredMailFolder findFirstByProperty(String propName, String propValue)
    {
	NullCheck.notEmpty(propName, "propName");
	NullCheck.notNull(propValue, "propValue");
	//FIXME:
	return null;
    }

    	    @Override public int getId(StoredMailFolder folder)
    {
	NullCheck.notNull(folder, "folder");
	final Folder f = (Folder)folder;
	return f.id;
    }

    @Override public StoredMailFolder loadById(int id)
    {
	if (id < 0)
	    throw new IllegalArgumentException("id (" + id + ") may not be negative");
	for(Folder f: folders)
	    if (f.id == id)
		return f;
	return null;
    }

    @Override public void save(StoredMailFolder parentFolder, MailFolder newFolder)
    {
	/*
	NullCheck.notNull(parentFolder, "parentFolder");
	NullCheck.notNull(parentFolder, "parentFolder");
	final Folder pf = (Folder)parentFolder;
	final Folder newFolder = new Folder(nextId());
	newFolder.copyValues(folder);
	folder.parentId = pf.id;
	folders.add(newFolder);
	*/
    }

    @Override public StoredMailFolder getRoot()
    {
	for(Folder f: folders)
	    if (f.id == f.parentId)
		return f;
		return null;
    }

    @Override public StoredMailFolder[] load(StoredMailFolder folder)
    {
	NullCheck.notNull(folder, "folder");
	final Folder f = (Folder)folder;
	final List<Folder> res = new LinkedList();
	
	return null;
    }

    @Override public String getUniRef(StoredMailFolder folder)
    {
	return null;
    }

    @Override public StoredMailFolder loadByUniRef(String uniRef)
    {
	return null;
    }
}
