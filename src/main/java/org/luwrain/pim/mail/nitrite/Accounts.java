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

package org.luwrain.pim.mail.nitrite;

import java.util.*;
import java.lang.reflect.*;

import com.google.gson.*;
import com.google.gson.reflect.*;

import org.luwrain.core.*;
import org.luwrain.pim.*;
import org.luwrain.pim.mail.*;
import org.luwrain.pim.fetching.MailConversations;

final class Accounts implements MailAccounts
{
        static private final Type ACCOUNT_LIST_TYPE = new TypeToken<List<Account>>(){}.getType();

    private final Registry registry;
    private final org.luwrain.pim.mail.Settings sett;
    private final Gson gson = new Gson();
    private Map<Integer, Account> accounts = new HashMap();

    Accounts(Registry registry)
    {
	NullCheck.notNull(registry, "registry");
	this.registry = registry;
	this.sett = org.luwrain.pim.mail.Settings.create(registry);
    }

    @Override public void sendDirectly(MailAccount account, MailMessage message) throws PimException
    {
	NullCheck.notNull(account, "account");
	NullCheck.notNull(message, "message");
		final MailConversations.Params params = new MailConversations.Params();
	params.doAuth = !account.getLogin().isEmpty();
	params.host = account.getHost();
	params.port = account.getPort();
	params.ssl = account.getFlags().contains(MailAccount.Flags.SSL);
	params.tls = account.getFlags().contains(MailAccount.Flags.TLS);
	params.login = account.getLogin();
	params.passwd = account.getPasswd();
	//		params.extProps.put( "mail.pop3.ssl.trust", account.getTrustedHosts());
		final MailConversations conv = new MailConversations(params, false);
	    conv.send(message.getRawMessage());
    }

    @Override public synchronized MailAccount[] load()
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

    @Override public synchronized MailAccount loadById(int id)
    {
	if (id < 0)
	    throw new IllegalArgumentException("id (" + String.valueOf(id) + ") may not be negative");
	if (accounts.isEmpty())
	    load();
	return accounts.get(new Integer(id));
    }

    @Override public synchronized MailAccount save(MailAccount account)
    {
	if (accounts.isEmpty())
	    load();
	final int newId = sett.getNextAccountId(1);
	sett.setNextAccountId(newId + 1);
	final Account a = new Account();
	a.id = newId;
	a.accounts = this;
	a.copyValues(account);
	accounts.put(new Integer(a.id), a);
	saveAll();
	return a;
    }

    @Override public synchronized void delete(MailAccount account) throws PimException
    {
	final Account a = (Account)account;
	if (accounts.isEmpty())
	    load();
	accounts.remove(new Integer(a.id));
	saveAll();
		    }

    @Override public synchronized String getUniRef(MailAccount account) throws PimException
    {
	final Account a = (Account)account;
	return AccountUniRefProc.PREFIX + ":" + String.valueOf(a.id);
    }

    @Override public synchronized MailAccount loadByUniRef(String uniRef)
    {
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

    @Override public synchronized int getId(MailAccount account) throws PimException
    {
	return ((Account)account).id;
    }

    @Override public synchronized MailAccount getDefault(MailAccount.Type type) throws PimException
    {
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
