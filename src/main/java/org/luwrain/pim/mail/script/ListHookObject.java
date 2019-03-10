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

package org.luwrain.pim.mail.script;

import org.luwrain.core.*;
import org.luwrain.script.*;
import org.luwrain.pim.*;
import org.luwrain.pim.mail.*;

public final class ListHookObject extends EmptyHookObject
{
    static private final String LOG_COMPONENT = MessageHookObject.LOG_COMPONENT;
    static private final String HEADER_ID = "list-id:";

    private final String id;
    private final String name;

    ListHookObject(String[] headers)
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
	final String idStr = AddressUtils.getAddress(idValue).trim();
	if (!idStr.isEmpty())
	    this.id = idStr; else
	    this.id = idValue.trim();
	this.name = AddressUtils.getPersonal(idValue);
    }

        @Override public Object getMember(String name)
    {
	NullCheck.notNull(name, "name");
	switch(name)
	{
	case "id":
	    return this.id;
	case "name":
	    return this.name;
	    	default:
	    return super.getMember(name);
	}
    }
}
