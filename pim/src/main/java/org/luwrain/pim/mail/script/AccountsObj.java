// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>
// Copyright 2015 Roman Volovodov <gr.rPman@gmail.com>

package org.luwrain.pim.mail.script;

import org.graalvm.polyglot.*;
import org.graalvm.polyglot.proxy.*;

import org.luwrain.core.*;
import org.luwrain.pim.*;
import org.luwrain.pim.mail.*;
import org.luwrain.pim.mail.persistence.*;
//import org.luwrain.pim.mail.persistence.*;

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
