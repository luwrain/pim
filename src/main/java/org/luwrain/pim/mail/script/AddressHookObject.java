/*
   Copyright 2012-2021 Michael Pozhidaev <msp@luwrain.org>
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

final class AddressHookObject extends EmptyHookObject
{
    static private final String LOG_COMPONENT = MailHookObject.LOG_COMPONENT;

    private final String full;
    private final String personal;
    private final String addr;

    AddressHookObject(String full)
    {
	NullCheck.notNull(full, "full");
	this.full = full;
	if (!this.full.trim().isEmpty())
	{
	    this.personal = AddressUtils.getPersonal(full);
	    this.addr = AddressUtils.getAddress(full);
	} else
	{
	this.personal = "";
	this.addr = "";
	}
    }

        @Override public Object getMember(String name)
    {
	NullCheck.notNull(name, "name");
	switch(name)
	{
	case "full":
	    return full;
	case "personal":
	    return personal;
	case "addr":
	    return addr;
	    	default:
	    return super.getMember(name);
	}
    }
}
