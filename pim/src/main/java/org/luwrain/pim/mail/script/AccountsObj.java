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
import org.luwrain.pim.mail.persistence.model.*;
import org.luwrain.pim.mail.persistence.dao.*;

import static org.luwrain.core.NullCheck.*;
import static org.luwrain.pim.mail.script.MailObj.*;
import static org.luwrain.pim.mail.persistence.MailPersistence.*;

final class AccountsObj
{
    private final AccountDAO dao;

    AccountsObj(AccountDAO dao)
    {
	notNull(dao, "dao");
	this.dao = dao;
    }

    @HostAccess.Export
    public final ProxyExecutable newAccount = (ProxyExecutable)this::newAccountImpl;
    public Object newAccountImpl(Value[] args)
    {
	try {
	    final var a = new Account();
	    dao.add(a);
	    return new AccountObj(a);
	}
	catch(PimException ex)
	{
	    log.error("Unable to save newly created mail account", ex);
	    return null;
	}
    }
}
