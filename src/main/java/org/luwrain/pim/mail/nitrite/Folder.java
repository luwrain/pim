/*
   Copyright 2012-2023 Michael Pozhidaev <msp@luwrain.org>
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

package org.luwrain.pim.mail.nitrite;

import java.io.*;
import java.util.*;
import java.util.function.*;

import org.luwrain.core.*;
import org.luwrain.pim.*;
import org.luwrain.pim.mail.*;

final class Folder extends MailFolder
{
    int id = 0;
    List<Folder> subfolders = null;
    transient Folders folders = null;

    int getId()
    {
	return this.id;
    }

    void setId(int id)
    {
	if (id < 0)
	    throw new IllegalArgumentException("id can't be negative");
	this.id = id;
    }

    int getSubfolderCount()
    {
	return subfolders != null?subfolders.size():0;
    }

    Folder[] getSubfoldersAsArray()
    {
	if (subfolders == null)
	    return new Folder[0];
	return subfolders.toArray(new Folder[subfolders.size()]);
    }

        @Override public void save()
    {
	if (this.folders == null)
	    throw new IllegalStateException("folders can't be null on saving");
	this.folders.saveAll();
    }

    void visit(Consumer<Folder> c)
    {
	c.accept(this);
	if (subfolders != null)
	    for(Folder f: subfolders)
		f.visit(c);
    }

    void addSubfolder(Folder folder, int saveAtIndex)
    {
	NullCheck.notNull(folder, "folder");
	if (subfolders == null)
	    subfolders = new ArrayList<>();
	subfolders.add(saveAtIndex, folder);
    }

    boolean removeSubfolder(int index)
    {
	if (subfolders == null || index >= subfolders.size())
	    return false;
	subfolders.remove(index);
	return true;
    }

    void setFolders(Folders folders)
    {
	NullCheck.notNull(folders, "folders");
	this.folders = folders;
    }


    @Override public int hashCode()
    {
	return id;
    }

    @Override public boolean equals(Object o)
    {
	if (o == null || !(o instanceof Folder))
	    return false;
	return id == ((Folder)o).id;
    }
}
