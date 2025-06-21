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
//import java.util.function.*;

import org.graalvm.polyglot.*;
import org.graalvm.polyglot.proxy.*;

import org.luwrain.core.*;
import org.luwrain.script.*;
import org.luwrain.pim.*;
import org.luwrain.pim.mail.*;

import org.luwrain.pim.mail.persistence.dao.*;
import org.luwrain.pim.mail.persistence.model.*;

import static org.luwrain.script.ScriptUtils.*;
import static org.luwrain.pim.mail.script.MailObj.*;
import static org.luwrain.pim.mail.FolderProperties.*;

public final class FoldersObj
{
    private final MailObj mailObj;
    FoldersObj(MailObj mailObj) { this.mailObj = mailObj; }

    @HostAccess.Export
    public final ProxyExecutable findFirstByProperty = (ProxyExecutable)this::findFirstByPropertyImpl;
    private Object findFirstByPropertyImpl(Value[] args)
    {
	if (!notNullAndLen(args, 2))
	    return null;
	final String
	name = asString(args[0]),
	value = asString(args[1]);
	if (name == null || value == null || name.trim().isEmpty())
	    return null;
	final Folder res = mailObj.folderDAO.findFirstByProperty(name, value);
	return res != null?new FolderObj(mailObj, res):null;
    }

        @HostAccess.Export
    public final ProxyExecutable getDefaultIncoming = (ProxyExecutable)this::getDefaultIncomingImpl;
    private Object getDefaultIncomingImpl(Value[] args)
    {
	final var f = mailObj.folderDAO.findFirstByProperty(DEFAULT_INCOMING, "true");
	return f != null?new FolderObj(mailObj, f):null;
    }

            @HostAccess.Export
    public final ProxyExecutable getDefaultMailingLists = (ProxyExecutable)this::getDefaultMailingListsImpl;
    private Object getDefaultMailingListsImpl(Value[] args)
    {
	final var f = mailObj.folderDAO.findFirstByProperty(DEFAULT_MAILING_LISTS, "true");
	return f != null?new FolderObj(mailObj, f):null;
    }



    /*
	case "local":
		return new FolderHookObject(storing, storing.getFolders().getRoot());
    */
}
