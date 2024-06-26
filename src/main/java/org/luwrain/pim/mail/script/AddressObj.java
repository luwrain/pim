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

import org.graalvm.polyglot.*;

import org.luwrain.core.*;
import org.luwrain.pim.*;
import org.luwrain.pim.mail.*;

import static org.luwrain.core.NullCheck.*;
import static org.luwrain.pim.mail.obsolete.AddressUtils.*;

public final class AddressObj
{
    @HostAccess.Export
    public final String full, personal, addr;

    AddressObj(String full)
    {
	notNull(full, "full");
	this.full = full;
	if (!this.full.trim().isEmpty())
	{
	    this.personal = getPersonal(full);
	    this.addr = getAddress(full);
	} else
	{
	    this.personal = "";
	    this.addr = "";
	}
    }
}
