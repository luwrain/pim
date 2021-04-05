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

import java.util.*;
import java.util.function.*;

import org.luwrain.core.*;
import org.luwrain.script.*;
import org.luwrain.pim.*;
import org.luwrain.pim.mail.*;

public final class MailHookObject extends EmptyHookObject
{
    static final String LOG_COMPONENT = "pim";

    private final MailStoring storing;

    public MailHookObject(MailStoring storing)
    {
	NullCheck.notNull(storing, "storing");
	this.storing = storing;
    }

    @Override public Object getMember(String name)
    {
	NullCheck.notNull(name, "name");
	switch(name)
	{
	case "folders":
	    return new FoldersHookObject(storing);
	case "accounts":
	    return new AccountsHookObject(storing);
	    	default:
	    return super.getMember(name);
	}
    }
}
