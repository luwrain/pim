/*
   Copyright 2012-2022 Michael Pozhidaev <msp@luwrain.org>
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

package org.luwrain.pim.mail.script;

import java.util.*;

import org.graalvm.polyglot.*;
import org.graalvm.polyglot.proxy.*;

import org.luwrain.core.*;
import org.luwrain.pim.*;
import org.luwrain.pim.mail.*;
import org.luwrain.pim.mail2.persistence.model.*;
import org.luwrain.pim.mail2.persistence.dao.*;

import static org.luwrain.script.ScriptUtils.*;
import static org.luwrain.pim.mail.script.MailObj.*;

public final class FolderObj
{
    private final FolderDAO dao;
    private final Folder folder;
    FolderObj(FolderDAO dao, Folder folder)
    {
	this.dao = dao;
	this.folder = folder;
    }

    @HostAccess.Export
    public ProxyExecutable getTitle = (ProxyExecutable)this::getTitleImpl;
    public Object getTitleImpl(Value[] args) { return folder.getName(); }

    public Object setTitleImpl(Value[] args)
    {
	//FIXME:
	return null;
    }

    public Object getSubfolders()
    {
			    final List<Object> res = new ArrayList<>();
		    for(Folder f: dao.getChildFolders(folder))
			res.add(new FolderObj(dao, f));
		    return ProxyArray.fromArray(res.toArray(new Object[res.size()]));
    }

    @HostAccess.Export
    public final ProxyExecutable saveMessage = (ProxyExecutable)this::saveMessageImpl;
    private Object saveMessageImpl(Value[] args)
    {
	/*
	if (!notNullAndLen(args, 1))
	    return Boolean.valueOf(false);
	final MessageObj message = args[0].asHostObject();
	if (message == null)
	    throw new IllegalArgumentException("The first argument doesn't contain a valid message object");
	storing.getMessages().save(folder, message.message);
	return Boolean.valueOf(true);
	*/
	return false;
    }

    private Object saveProperties()
    {
	/*
		    try {
		folder.save();
		return new Boolean(true);
		}
		catch(Exception e)
		{
		    Log.error(LOG_COMPONENT, "unable to save properties of the stored mail folder:" + e.getClass().getName() + ":" + e.getMessage());
		    return new Boolean(false);
		}
	*/
	return false;
    }

    private Object newSubfolder()
    {
	final var f = new Folder();
	dao.add(f);
	return new FolderObj(dao, f);
    }
}
