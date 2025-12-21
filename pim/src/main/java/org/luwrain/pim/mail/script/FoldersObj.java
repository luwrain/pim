// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>
// Copyright 2015 Roman Volovodov <gr.rPman@gmail.com>

package org.luwrain.pim.mail.script;

import java.util.*;
//import java.util.function.*;

import org.graalvm.polyglot.*;
import org.graalvm.polyglot.proxy.*;

import org.luwrain.core.*;
import org.luwrain.script.*;
import org.luwrain.pim.*;
import org.luwrain.pim.mail.*;

//import org.luwrain.pim.mail.persistence.dao.*;
import org.luwrain.pim.mail.persistence.*;

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
