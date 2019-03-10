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

package org.luwrain.pim.mail.mem;

import java.util.*;

import org.luwrain.core.*;
import org.luwrain.pim.*;
import org.luwrain.pim.mail.*;

class Accounts implements MailAccounts
{
    private final List<Account> accounts = new LinkedList();

    @Override public StoredMailAccount[] load()
    {
	return accounts.toArray(new StoredMailAccount[accounts.size()]);
    }

    @Override public StoredMailAccount loadById(int id)
    {
	if (id < 0)
	    throw new IllegalArgumentException("id (" + id + ") may not be negative");
	for(Account a: accounts)
	    if (a.id == id)
		return a;
	return null;
    }

    @Override public void save(MailAccount account)
    {
	NullCheck.notNull(account, "account");
	final Account newAccount = new Account(nextId());
	newAccount.copyValues(account);
	accounts.add(newAccount);
    }

    @Override public void delete(StoredMailAccount account)
    {
	NullCheck.notNull(account, "account");
	final Account a = (Account)account;
	final Iterator<Account> it = accounts.iterator();
	while(it.hasNext())
	{
	    final Account v = it.next();
	    if (v.id == a.id)
	    {
		accounts.remove(it);
		return;
	    }
	}
    }

    @Override public String getUniRef(StoredMailAccount account)
    {
	NullCheck.notNull(account, "account");
	final Account a = (Account)account;
	return MailStoring.ACCOUNT_UNIREF_PREFIX + ":" + a.id;
    }

    @Override public StoredMailAccount loadByUniRef(String uniRef)
    {
	NullCheck.notEmpty(uniRef, "uniRef");
	if (!uniRef.startsWith(MailStoring.ACCOUNT_UNIREF_PREFIX + ":"))
	    return null;
	final String numStr = uniRef.substring(MailStoring.ACCOUNT_UNIREF_PREFIX.length() + 1);
	final int id;
	try {
	    id = Integer.parseInt(numStr);
	}
	catch(NumberFormatException e)
	{
	    return null;
	}
	return loadById(id);
    }

    @Override public int getId(StoredMailAccount account)
    {
	NullCheck.notNull(account, "account");
	final Account a = (Account)account;
	return a.id;
    }

    @Override public StoredMailAccount getDefault(MailAccount.Type type) throws PimException
    {
	NullCheck.notNull(type, "type");
	final StoredMailAccount[] accounts = load();
	StoredMailAccount anyEnabled = null;
	for(StoredMailAccount a: accounts)
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

    private int nextId()
    {
	int value = 0;
	for (Account a: accounts)
	    value = Math.max(value, a.id);
	return value + 1;
    }
}
