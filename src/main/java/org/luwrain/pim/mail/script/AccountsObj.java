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

import org.graalvm.polyglot.*;
import org.graalvm.polyglot.proxy.*;

import org.luwrain.core.*;
import org.luwrain.pim.*;
import org.luwrain.pim.mail.*;

import static org.luwrain.pim.mail.script.MailObj.*;

final class AccountsObj
{
    private final MailStoring storing;

    AccountsObj(MailStoring storing)
    {
	NullCheck.notNull(storing, "storing");
	this.storing = storing;
    }

    @HostAccess.Export
    public final ProxyExecutable newAccount = (ProxyExecutable)this::newAccountImpl;
    public Object newAccountImpl(Value[] args)
    {
	try {
	    return new AccountObj(storing.getAccounts().save(new MailAccount()));
	}
	catch(PimException e)
	{
	    Log.error(LOG_COMPONENT, "unable to save newly created mail account: " + e.getClass().getName() + ": " + e.getMessage());
	    e.printStackTrace();
	    return null;
	}
    }
}
