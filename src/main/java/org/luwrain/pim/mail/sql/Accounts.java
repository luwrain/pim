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
import java.lang.reflect.*;

import com.google.gson.*;
import com.google.gson.reflect.*;

import org.luwrain.core.*;
import org.luwrain.pim.*;
import org.luwrain.pim.mail.*;

final class Accounts implements MailAccounts
{
        static private final Type ACCOUNT_LIST_TYPE = new TypeToken<List<Account>>(){}.getType();

    private final Registry registry;
    private final org.luwrain.pim.mail.Settings.Accounts sett;
    private final Gson gson = new Gson();
    private Map<Integer, Account> accounts = new HashMap();

    Accounts(Registry registry)
    {
	NullCheck.notNull(registry, "registry");
	this.registry = registry;
	this.sett = org.luwrain.pim.mail.Settings.createAccounts(registry);
    }

    @Override public MailAccount[] load()
    {
		this.accounts.clear();
	final List<Account> res = gson.fromJson(sett.getAccounts(""), ACCOUNT_LIST_TYPE);
	if (res == null)
	    return new Account[0];
	for(Account a: res)
	{
	    a.accounts = this;
	    this.accounts.put(new Integer(a.id), a);
	}
	final Account[] a = res.toArray(new Account[res.size()]);
	Arrays.sort(a);
	return a;
    }

    @Override public MailAccount loadById(int id)
    {
	if (id < 0)
	    throw new IllegalArgumentException("id (" + String.valueOf(id) + ") may not be negative");
	if (accounts.isEmpty())
	    load();
	return accounts.get(new Integer(id));
    }

    @Override public MailAccount save(MailAccount account)
    {
	NullCheck.notNull(account, "account");
	if (accounts.isEmpty())
	    load();
	final int newId = sett.getNextId(1);
	sett.setNextId(newId + 1);
	final Account a = new Account();
	a.id = newId;
	a.accounts = this;
	a.copyValues(account);
	accounts.put(new Integer(a.id), a);
	saveAll();
	return a;
    }

    @Override public void delete(MailAccount account) throws PimException
    {
	NullCheck.notNull(account, "account");
	final Account a = (Account)account;
	if (accounts.isEmpty())
	    load();
	accounts.remove(new Integer(a.id));
	saveAll();
		    }

    @Override public String getUniRef(MailAccount account) throws PimException
    {
	NullCheck.notNull(account, "account");
	final Account a = (Account)account;
	return AccountUniRefProc.PREFIX + ":" + String.valueOf(a.id);
    }

    @Override public MailAccount loadByUniRef(String uniRef)
    {
	NullCheck.notEmpty(uniRef, "uniRef");
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
	if (accounts.isEmpty())
	    load();
	return accounts.get(new Integer(id));
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

    void saveAll()
    {
	final List<Account> a = new LinkedList();
	for(Map.Entry<Integer, Account> e: accounts.entrySet())
	    a.add(e.getValue());
	sett.setAccounts(gson.toJson(a));
    }
}
