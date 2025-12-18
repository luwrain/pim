// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>
// Copyright 2015 Roman Volovodov <gr.rPman@gmail.com>

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
