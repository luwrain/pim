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

    //    private final Registry registry;
    private final org.luwrain.pim.mail.Settings sett;
    private final Gson gson = new Gson();
    //    private Map<Integer, Account> accounts = new HashMap();
    private Data data = null;

    Accounts(Registry registry)
    {
	NullCheck.notNull(registry, "registry");
	this.sett = org.luwrain.pim.mail.Settings.create(registry);
	loadAll();
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
	final List<MailAccount> res = new ArrayList<>();
	for(Map.Entry<Integer, Account> e: data.accounts.entrySet())
	    res.add(e.getValue());
	return res.toArray(new MailAccount[res.size()]);
    }

    @Override public synchronized MailAccount loadById(int id)
    {
	if (id < 0)
	    throw new IllegalArgumentException("id (" + String.valueOf(id) + ") may not be negative");
	return data.accounts.get(Integer.valueOf(id));
    }

    @Override public synchronized MailAccount save(MailAccount account)
    {
	NullCheck.notNull(account, "account");
	final int newId = data.nextId.intValue();
	data.nextId = Integer.valueOf(newId + 1);
	final Account a = new Account();
	a.id = newId;
	a.accounts = this;
	a.copyValues(account);
	data.accounts.put(Integer.valueOf(newId), a);
	saveAll();
	return a;
    }

    @Override public synchronized void delete(MailAccount account) throws PimException
    {
	NullCheck.notNull(account, "account");
	final Account a = (Account)account;
	data.accounts.remove(Integer.valueOf(a.id));
	saveAll();
		    }

    @Override public synchronized String getUniRef(MailAccount account) throws PimException
    {
	NullCheck.notNull(account, "account");
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
	return data.accounts.get(Integer.valueOf(id));
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

    private void loadAll()
    {
	final String accountsStr = sett.getAccounts("");
	if (accountsStr.trim().isEmpty())
	{
	    this.data = new Data();
	    this.data.nextId = Integer.valueOf(0);
	    this.data.accounts = new TreeMap<>();
	    saveAll();
	    return;
	}
	this.data = gson.fromJson(accountsStr, Data.class);
	final Map<Integer, Account> accounts = new TreeMap<>();
	int maxId = 0;
	for(Map.Entry<Integer, Account> e: data.accounts.entrySet())
	    if (e.getKey() != null && e.getValue() != null)
	    {
		e.getValue().accounts = this;
		accounts.put(e.getKey(), e.getValue());
		if (e.getKey().intValue() > maxId)
		    maxId = e.getKey().intValue();
	    }
	data.accounts = accounts;
	if(data.nextId == null)
	    data.nextId = Integer.valueOf(maxId + 1);
    }

    private void saveAll()
    {
	sett.setAccounts(gson.toJson(data));
    }

    static private final class Data
    {
	Integer nextId = null;
	Map<Integer, Account> accounts = null;
    }
}
