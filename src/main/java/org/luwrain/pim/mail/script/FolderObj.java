/*
   Copyright 2012-2024 Michael Pozhidaev <msp@luwrain.org>
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

import org.luwrain.script.core.*;
import org.luwrain.pim.mail2.persistence.model.*;
import org.luwrain.pim.mail2.persistence.dao.*;

import static org.luwrain.core.NullCheck.*;
import static org.luwrain.script.ScriptUtils.*;
import static org.luwrain.pim.mail.script.MailObj.*;

public final class FolderObj
{
    private final MailObj mailObj;
    private final Folder folder;
    FolderObj(MailObj mailObj, Folder folder)
    {
	notNull(mailObj, "mailObj");
	notNull(folder, "folder");
	this.mailObj = mailObj;
	this.folder = folder;
    }

    @HostAccess.Export
	public ProxyExecutable getName = (ProxyExecutable)this::getNameImpl;
    public Object getNameImpl(Value[] args) { return folder.getName(); }

    @HostAccess.Export
	public ProxyExecutable setName = (ProxyExecutable)this::setNameImpl;
    public Object setNameImpl(Value[] args)
    {
	if (!notNullAndLen(args, 1) || !args[0].isString())
	    throw new IllegalArgumentException("Folder.setName() takes exactly one string argument");
	folder.setName(args[0].asString());
	return this;
    }

        @HostAccess.Export
	public ProxyExecutable getProperties = (ProxyExecutable)this::getPropertiesImpl;
    public Object getPropertiesImpl(Value[] args) { return new PropertiesObj(folder.getProperties()); }


    @HostAccess.Export
	public ProxyExecutable getSubfolders = (ProxyExecutable)this::getSubfoldersImpl;
    private Object getSubfoldersImpl(Value[] args)
    {
	final var res = new ArrayList<>();
	for(Folder f: mailObj.folderDAO.getChildFolders(folder))
	    res.add(new FolderObj(mailObj, f));
	return ProxyArray.fromArray((Object[])res.toArray(new Object[res.size()]));
    }

    @HostAccess.Export
	public final ProxyExecutable saveMessage = (ProxyExecutable)this::saveMessageImpl;
    private Object saveMessageImpl(Value[] args)
    {
	if (!notNullAndLen(args, 1))
	    throw new IllegalArgumentException("The first argument doesn't contain a valid message object");
	final MessageObj message = args[0].asHostObject();
	if (message == null || message.message == null)
	    throw new IllegalArgumentException("The first argument doesn't contain a valid message object");
	message.message.getMetadata().setFolderId(folder.getId());
	mailObj.messageDAO.add(message.message.getMetadata());
	return this;
    }

    @HostAccess.Export
	public final ProxyExecutable update = (ProxyExecutable)this::updateImpl;
    private Object updateImpl(Value[] args)
    {
	mailObj.folderDAO.update(folder);
	return this;
    }

    @HostAccess.Export
	public final ProxyExecutable newSubfolder = (ProxyExecutable)this::newSubfolderImpl;
    private Object newSubfolderImpl(Value[] args)
    {
	final var f = new Folder();
	f.setParentFolderId(folder.getId());
	mailObj.folderDAO.add(f);
	return new FolderObj(mailObj, f);
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
}
