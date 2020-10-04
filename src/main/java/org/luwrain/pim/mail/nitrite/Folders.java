/*
   Copyright 2012-2020 Michael Pozhidaev <msp@luwrain.org>
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

import org.luwrain.core.*;
import org.luwrain.pim.*;
import org.luwrain.pim.mail.*;

final class Folders implements MailFolders
{
    static private final String LOG_COMPONENT = "fixme";

    private final Registry registry;
    private final Messages messages;
    private Folder[] cache = null;

    Folders(Registry registry, Messages messages)
    {
	NullCheck.notNull(registry, "registry");
	NullCheck.notNull(messages, "messages");
	this.registry = registry;
	this.messages = messages;
    }

    @Override public MailFolder findFirstByProperty(String propName, String propValue) throws PimException
    {
	NullCheck.notEmpty(propName, "propName");
	NullCheck.notNull(propValue, "propValue");
	final Folder[] folders = null;
	for(Folder f: folders)
	{
	    final String value = f.getProperties().getProperty(propName);
	    if (value != null && value.equals(propValue))
		return f;
	}
	return null;
    }

    @Override public int getId(MailFolder folder)
    {
	NullCheck.notNull(folder, "folder");
	return ((Folder)folder).id;
    }

    @Override public MailFolder loadById(int id) throws PimException
    {
	final Folder folder = new Folder();
	return null;
    }

    @Override public MailFolder save(MailFolder parentFolder, MailFolder newFolder) throws PimException
    {
	NullCheck.notNull(parentFolder, "parentFolder");
	NullCheck.notNull(newFolder, "newFolder");
	final Folder parent = (Folder)parentFolder;
	return null;
    }

    @Override public MailFolder getRoot() throws PimException
    {
	return null;
    }

    @Override public MailFolder[] load(MailFolder folder) throws PimException
    {
	NullCheck.notNull(folder, "folder");
	return null;
    }

    @Override public String getUniRef(MailFolder folder) throws PimException
    {
	NullCheck.notNull(folder, "folder");
	final Folder folderReg = (Folder)folder;
	return FolderUniRefProc.PREFIX + ":" + folderReg.id;
    }

    @Override public MailFolder loadByUniRef(String uniRef) throws PimException
    {
	NullCheck.notEmpty(uniRef, "uniRef");
	if (!uniRef.startsWith(FolderUniRefProc.PREFIX + ":"))
	    return null;
	final int id;
	try {
	    id = Integer.parseInt(uniRef.substring(FolderUniRefProc.PREFIX.length() + 1));
	}
	catch (NumberFormatException e)
	{
	    Log.warning(LOG_COMPONENT, "parsing an invalid mail folder uniref: " + uniRef);
	    return null;
	}
	final Folder folder = new Folder();
	return null;
    }

}
