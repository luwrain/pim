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

import java.io.*;
import org.graalvm.polyglot.*;

import org.luwrain.core.*;
import org.luwrain.pim.*;
import org.luwrain.pim.mail.*;

public final class MailingListHookObject
{
    static private final String
	LOG_COMPONENT = MailHookObject.LOG_COMPONENT,
HEADER_ID = "list-id:";

    @HostAccess.Export
    public final String id;

    @HostAccess.Export
    public final String name;

    MailingListHookObject(String[] headers)
    {
	NullCheck.notNullItems(headers, "headers");
	String idValue = null;
	for(String s: headers)
	    if (s.toLowerCase().startsWith(HEADER_ID))
	    {
		idValue = s.substring(HEADER_ID.length());
		break;
	    }
	if (idValue == null || idValue.trim().isEmpty())
	{
	    this.id = "";
	    this.name = "";
	    return;
	}
	try {
	    idValue = BinaryMessage.decodeText(idValue);
	}
	catch(IOException e)
	{
	    Log.warning(LOG_COMPONENT, "unable to decode the list ID name:" + e.getClass().getName() + ":" + e.getMessage());
	}
	final String idStr = AddressUtils.getAddress(idValue).trim();
	if (!idStr.isEmpty())
	    this.id = idStr; else
	    this.id = idValue.trim();
	this.name = AddressUtils.getPersonal(idValue);
    }
}
