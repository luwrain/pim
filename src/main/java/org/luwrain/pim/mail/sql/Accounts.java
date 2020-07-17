/*
   Copyright 2012-2020 Michael Pozhidaev <msp@luwrain.org>
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

package org.luwrain.pim.mail.sql;

import java.util.*;

import org.luwrain.core.*;
import org.luwrain.pim.*;
import org.luwrain.pim.mail.*;

final class Accounts implements MailAccounts
{
    private final Registry registry;

    Accounts(Registry registry)
    {
	NullCheck.notNull(registry, "registry");
	this.registry = registry;
    }

    @Override public MailAccount[] load() throws PimException
    {
	registry.addDirectory(org.luwrain.pim.mail.Settings.ACCOUNTS_PATH);
	return null;
    }

    @Override public MailAccount loadById(int id) throws PimException
    {
	if (id < 0)
	    throw new IllegalArgumentException("id (" + String.valueOf(id) + ") may not be negative");
	/*
	final Account account = new Account(registry, id);
	return account.load()?account:null;
	*/
	return null;
    }

    @Override public MailAccount save(MailAccount account) throws PimException
    {
	NullCheck.notNull(account, "account");
	final int newId = Registry.nextFreeNum(registry, org.luwrain.pim.mail.Settings.ACCOUNTS_PATH);
	return null;
    }

    @Override public void delete(MailAccount account) throws PimException
    {
	NullCheck.notNull(account, "account");
	final Account accountReg = (Account)account;
	final String path = Registry.join(org.luwrain.pim.mail.Settings.ACCOUNTS_PATH, String.valueOf(accountReg.id));

	    }

    @Override public String getUniRef(MailAccount account) throws PimException
    {
	NullCheck.notNull(account, "account");
	final Account accountReg = (Account)account;
	return AccountUniRefProc.PREFIX + ":" + String.valueOf(accountReg.id);
    }

    @Override public MailAccount loadByUniRef(String uniRef)
    {
	NullCheck.notEmpty(uniRef, "uniRef");
	/*
	if (!uniRef.startsWith(AccountUniRefProc.PREFIX + ":"))
	    return null;
	final int id;
	try {
	    id = Integer.parseInt(uniRef.substring(AccountUniRefProc.PREFIX.length() + 1));
	}
	catch (NumberFormatException e)
	{
	    return null;
	}
	final Account account = new Account(registry, id);
	return account.load()?account:null;
	*/
	return null;
    }

    @Override public int getId(MailAccount account) throws PimException
    {
	NullCheck.notNull(account, "account");
	return ((Account)account).id;
    }

    @Override public MailAccount getDefault(MailAccount.Type type) throws PimException
    {
	NullCheck.notNull(type, "type");
	final MailAccount[] accounts = load();
	MailAccount anyEnabled = null;
	for(MailAccount a: accounts)
	{
	    if (a.getType() != type)
		continue;
	    if (!a.getFlags().contains(MailAccount.Flags.ENABLED))
		continue;
	    anyEnabled = a;
    	    if (!a.getFlags().contains(MailAccount.Flags.DEFAULT))
		continue;
	    return a;
	}
	return anyEnabled;
    }
}
