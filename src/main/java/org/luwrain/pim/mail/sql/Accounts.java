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

package org.luwrain.pim.mail.sql;

import java.util.*;

import org.luwrain.core.*;
import org.luwrain.pim.*;
import org.luwrain.pim.mail.*;

class Accounts implements MailAccounts
{
    private final Registry registry;

    Accounts(Registry registry)
    {
	NullCheck.notNull(registry, "registry");
	this.registry = registry;
    }

    @Override public StoredMailAccount[] load() throws PimException
    {
	registry.addDirectory(org.luwrain.pim.mail.Settings.ACCOUNTS_PATH);
	final List<Account> accounts = new LinkedList();
	for(String s: registry.getDirectories(org.luwrain.pim.mail.Settings.ACCOUNTS_PATH))
	{
	    final int id ;
	    try {
		id = Integer.parseInt(s);
	    }
	    catch(NumberFormatException e)
	    {
		Log.warning("pim", "invalid mail account directory:" + s);
		continue;
	    }
	    final Account account = new Account(registry, id);
	    if (account.load())
		accounts.add(account);
	}
	final Account[] res = accounts.toArray(new Account[accounts.size()]);
	Arrays.sort(res);
	return res;
    }

    @Override public StoredMailAccount loadById(int id) throws PimException
    {
	if (id < 0)
	    throw new IllegalArgumentException("id (" + id + ") may not be negative");
	final Account account = new Account(registry, id);
	return account.load()?account:null;
    }

    @Override public void save(MailAccount account) throws PimException
    {
	NullCheck.notNull(account, "account");
	final int newId = Registry.nextFreeNum(registry, org.luwrain.pim.mail.Settings.ACCOUNTS_PATH);
	final String path = Registry.join(org.luwrain.pim.mail.Settings.ACCOUNTS_PATH, "" + newId);
	registry.addDirectory(path);
	final boolean enabled = account.flags.contains(MailAccount.Flags.ENABLED);
	final boolean ssl = account.flags.contains(MailAccount.Flags.SSL);
	final boolean tls = account.flags.contains(MailAccount.Flags.TLS);
	final boolean def = account.flags.contains(MailAccount.Flags.DEFAULT);
	final boolean leaveMessages = account.flags.contains(MailAccount.Flags.LEAVE_MESSAGES);
	final org.luwrain.pim.mail.Settings.Account s = org.luwrain.pim.mail.Settings.createAccount(registry, path);
	s.setType(Account.getTypeStr(account.type));
	s.setTitle(account.title);
	s.setHost(account.host);
	s.setPort(account.port);
	s.setLogin(account.login);
	s.setPasswd(account.passwd);
	s.setTrustedHosts(account.trustedHosts);
	s.setSubstName(account.substName);
	s.setSubstAddress(account.substAddress);
	s.setEnabled(enabled);
	s.setSsl(ssl);
	s.setTls(tls);
	s.setDefault(def);
	s.setLeaveMessages(leaveMessages);
    }

    @Override public void delete(StoredMailAccount account) throws PimException
    {
	NullCheck.notNull(account, "account");
	if (!(account instanceof Account))
	    throw new PimException("account must be an instance of StoredMailAccountRegistry");
	final Account accountRegistry = (Account)account;
	final String path = Registry.join(org.luwrain.pim.mail.Settings.ACCOUNTS_PATH, "" + accountRegistry.id);
	if (!registry.deleteDirectory(path))
	    throw new PimException("Unable to delete the registry directory " + path);
    }

    @Override public String getUniRef(StoredMailAccount account) throws PimException
    {
	NullCheck.notNull(account, "account");
	if (!(account instanceof Account))
	    throw new PimException("account must be an instance of StoredMailAccountRegistry");
	final Account accountRegistry = (Account)account;
	return AccountUniRefProc.PREFIX + ":" + accountRegistry.id;
    }

    @Override public StoredMailAccount loadByUniRef(String uniRef)
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
	final Account account = new Account(registry, id);
	return account.load()?account:null;
    }

    @Override public int getId(StoredMailAccount account) throws PimException
    {
	NullCheck.notNull(account, "account");
	if (!(account instanceof Account))
	    throw new PimException("account must be an instance of StoredMailAccountRegistry");
	return ((Account)account).id;
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
}
