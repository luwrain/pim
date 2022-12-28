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
//import java.util.function.*;

import org.graalvm.polyglot.*;
import org.graalvm.polyglot.proxy.*;

import org.luwrain.core.*;
import org.luwrain.script.*;
import org.luwrain.pim.*;
import org.luwrain.pim.mail.*;

import static org.luwrain.script.ScriptUtils.*;
import static org.luwrain.pim.mail.script.MailObj.*;

public final class FoldersObj
{
    private final MailStoring storing;
    FoldersObj(MailStoring storing) { this.storing = storing; }

    @HostAccess.Export
    public final ProxyExecutable findByProp = (ProxyExecutable)this::findFirstByPropertyImpl;
    private Object findFirstByPropertyImpl(Value[] args)
    {
	if (!notNullAndLen(args, 2))
	    return null;
	final String
	name = asString(args[0]),
	value = asString(args[1]);
	if (name == null || value == null || name.trim().isEmpty())
	    return null;
	final MailFolder res = storing.getFolders().findFirstByProperty(name, value);
	return res != null?new FolderObj(storing, res):null;
    }

    /*
	case "local":
		return new FolderHookObject(storing, storing.getFolders().getRoot());
    */
}
