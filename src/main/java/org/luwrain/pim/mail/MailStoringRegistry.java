/*
   Copyright 2012-2016 Michael Pozhidaev <michael.pozhidaev@gmail.com>
   Copyright 2015 Roman Volovodov <gr.rPman@gmail.com>

   This file is part of the LUWRAIN.

   LUWRAIN is free software; you can redistribute it and/or
   modify it under the terms of the GNU General Public
   License as published by the Free Software Foundation; either
   version 3 of the License, or (at your option) any later version.

   LUWRAIN is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
   General Public License for more details.
*/

package org.luwrain.pim.mail;

import java.util.*;

import org.luwrain.core.*;
//import org.luwrain.util.RegistryAutoCheck;
import org.luwrain.pim.*;
import org.luwrain.pim.RegistryKeys;

abstract class MailStoringRegistry implements MailStoring
{
    private Registry registry;
    private RegistryKeys registryKeys = new RegistryKeys();

    public MailStoringRegistry(Registry registry)
    {
	this.registry = registry;
	if (registry == null)
	    throw new NullPointerException("registry may not be null");
    }

    @Override public StoredMailFolder getFoldersRoot() throws PimException
    {
	final StoredMailFolderRegistry[] folders = loadAllFolders();
	for(StoredMailFolderRegistry f: folders)
	    if (f.id == f.parentId)
		return f;
	return null;
    }

    @Override public StoredMailFolder[] getFolders(StoredMailFolder folder) throws PimException
    {
	if (folder == null || !(folder instanceof StoredMailFolderRegistry))
	    return null;
	final StoredMailFolderRegistry parent = (StoredMailFolderRegistry)folder;
	final LinkedList<StoredMailFolder> res = new LinkedList<StoredMailFolder>();
	final StoredMailFolderRegistry[] folders = loadAllFolders();
	for(StoredMailFolderRegistry f: folders)
	    if (f.parentId == parent.id && f.id != f.parentId)
		res.add(f);
	return res.toArray(new StoredMailFolder[res.size()]);
    }

    @Override public String getFolderUniRef(StoredMailFolder folder) throws PimException
    {
	if (folder == null || !(folder instanceof StoredMailFolderRegistry))
	    return "";
	final StoredMailFolderRegistry folderRegistry = (StoredMailFolderRegistry)folder;
	return FolderUniRefProc.PREFIX + ":" + folderRegistry.id;
    }

    @Override public StoredMailFolder getFolderByUniRef(String uniRef)
    {
	if (uniRef == null || uniRef.length() < FolderUniRefProc.PREFIX.length() + 2)
	    return null;
	if (!uniRef.startsWith(FolderUniRefProc.PREFIX + ":"))
	    return null;
	int id = 0;
	try {
	    id = Integer.parseInt(uniRef.substring(FolderUniRefProc.PREFIX.length() + 1));
	}
	catch (NumberFormatException e)
	{
	    e.printStackTrace();
	    return null;
	}
	final StoredMailFolderRegistry folder = new StoredMailFolderRegistry(registry, id);
	if (folder.load())
	    return folder;
	return null;
    }

    @Override public StoredMailRule[] getRules() throws PimException
    {
	final String[] dirNames = registry.getDirectories(registryKeys.mailRules());
	if (dirNames == null || dirNames.length < 1)
	    return new StoredMailRule[0];
	final LinkedList<StoredMailRuleRegistry> res = new LinkedList<StoredMailRuleRegistry>();
	for(String s: dirNames)
	{
	    if (s == null || s.trim().isEmpty())
		continue;
	    int id;
	    try {
		id = Integer.parseInt(s);
	    }
	    catch(NumberFormatException e)
	    {
		e.printStackTrace();
		continue;
	    }
	    final StoredMailRuleRegistry rule = new StoredMailRuleRegistry(registry, id);
	    if (rule.load())
		res.add(rule);
	}
	return res.toArray(new StoredMailRuleRegistry[res.size()]);
    }

    @Override public void saveRule(MailRule rule) throws PimException
    {
	NullCheck.notNull(rule, "rule");
	final int newId = org.luwrain.pim.Util.newFolderId(registry, registryKeys.mailRules());
	final String path = Registry.join(registryKeys.mailRules(), "" + newId);
	if (!registry.addDirectory(path))
	    throw new PimException("Unable to create new registry directory " + path);
	if (!registry.setString(Registry.join(path, "action"), StoredMailRuleRegistry.getActionStr(rule.action)))
	    throw new PimException("Unable to set a string value " + Registry.join(path, "action"));
	if (!registry.setString(Registry.join(path, "header-regex"), rule.headerRegex))
	    throw new PimException("Unable to set a string value " + Registry.join(path, "header-regex"));
	if (!registry.setString(Registry.join(path, "dest-folder-uniref"), rule.destFolderUniRef))
	    throw new PimException("Unable to set a string value " + Registry.join(path, "dest-folder-uniref"));
    }

    @Override public void deleteRule(StoredMailRule rule) throws PimException
    {
	NullCheck.notNull(rule, "rule");
	if (!(rule instanceof StoredMailRuleRegistry))
	    throw new IllegalArgumentException("rule is not an instance of StoredMailRuleRegistry");
	final StoredMailRuleRegistry ruleRegistry = (StoredMailRuleRegistry)rule;
	final String path = Registry.join(registryKeys.mailRules(), "" + ruleRegistry.id);
	if (!registry.deleteDirectory(path))
	    throw new PimException("Unable to delete the registry directory " + path);
    }

    @Override public StoredMailAccount[] loadAccounts() throws PimException
    {
	final LinkedList<StoredMailAccountRegistry> accounts = new LinkedList<StoredMailAccountRegistry>();
	for(String s: registry.getDirectories(registryKeys.mailAccounts()))
	{
	    if (s.isEmpty())
		continue;
	    int id = 0;
	    try {
		id = Integer.parseInt(s);
	    }
	    catch(NumberFormatException e)
	    {
		Log.warning("pim", "invalid mail account registry directory:" + s);
		continue;
	    }
	    final StoredMailAccountRegistry account = new StoredMailAccountRegistry(registry, id);
	    if (account.load())
		accounts.add(account);
	}
	final StoredMailAccountRegistry[] res = accounts.toArray(new StoredMailAccountRegistry[accounts.size()]);
	Arrays.sort(res);
	return res;
    }

    @Override public StoredMailAccount loadAccountById(long id) throws PimException
    {
	final StoredMailAccountRegistry account = new StoredMailAccountRegistry(registry, (int)id);//FIXME:
	    return account.load()?account:null;
    }

    @Override public void saveAccount(MailAccount account) throws PimException
    {
	NullCheck.notNull(account, "account");
	final int newId = Registry.nextFreeNum(registry, registryKeys.mailAccounts());
	final String path = Registry.join(registryKeys.mailAccounts(), "" + newId);
	if (!registry.addDirectory(path))
	    throw new PimException("Unable to create new registry directory " + path);
	final boolean enabled = account.flags.contains(MailAccount.Flags.ENABLED);
	final boolean ssl = account.flags.contains(MailAccount.Flags.SSL);
	final boolean tls = account.flags.contains(MailAccount.Flags.TLS);
	final boolean def = account.flags.contains(MailAccount.Flags.DEFAULT);
	final boolean leaveMessages = account.flags.contains(MailAccount.Flags.LEAVE_MESSAGES);
	final Settings.Account s = Settings.createAccount(registry, path);
	s.setType(StoredMailAccountRegistry.getTypeStr(account.type));
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

    @Override public void deleteAccount(StoredMailAccount account) throws PimException
    {
	NullCheck.notNull(account, "account");
	if (!(account instanceof StoredMailAccountRegistry))
	    throw new IllegalArgumentException("account is not an instance of StoredMailRAccountRegistry");
	final StoredMailAccountRegistry accountRegistry = (StoredMailAccountRegistry)account;
	final String path = Registry.join(registryKeys.mailAccounts(), "" + accountRegistry.getId());
	if (!registry.deleteDirectory(path))
	    throw new PimException("Unable to delete the registry directory " + path);
    }

    @Override public String getAccountUniRef(StoredMailAccount account) throws PimException
    {
	if (account == null || !(account instanceof StoredMailAccountRegistry))
	    return "";
	final StoredMailAccountRegistry accountRegistry = (StoredMailAccountRegistry)account;
	return AccountUniRefProc.PREFIX + ":" + accountRegistry.getId();
    }

    @Override public StoredMailAccount getAccountByUniRef(String uniRef)
    {
	if (uniRef == null || uniRef.length() < AccountUniRefProc.PREFIX.length() + 2)
	    return null;
	if (!uniRef.startsWith(AccountUniRefProc.PREFIX + ":"))
	    return null;
	int id = 0;
	try {
	    id = Integer.parseInt(uniRef.substring(AccountUniRefProc.PREFIX.length() + 1));
	}
	catch (NumberFormatException e)
	{
	    e.printStackTrace();
	    return null;
	}
	final StoredMailAccountRegistry account = new StoredMailAccountRegistry(registry, id);
	if (account.load())
	    return account;
	return null;
    }

    private StoredMailFolderRegistry[] loadAllFolders()
    {
	final String[] subdirs = registry.getDirectories(registryKeys.mailFolders());
	if (subdirs == null || subdirs.length < 1)
	    return new StoredMailFolderRegistry[0];
	final LinkedList<StoredMailFolderRegistry> folders = new LinkedList<StoredMailFolderRegistry>();
	for(String s: subdirs)
	{
	    if (s == null || s.isEmpty())
		continue;
	    int id = 0;
	    try {
		id = Integer.parseInt(s);
	    }
	    catch (NumberFormatException e)
	    {
		e.printStackTrace();
		continue;
	    }
	    final StoredMailFolderRegistry f = new StoredMailFolderRegistry(registry, id);
	    if (f.load())
		folders.add(f);
	}
	final StoredMailFolderRegistry[] res = folders.toArray(new StoredMailFolderRegistry[folders.size()]);
	Arrays.sort(res);
	return res;
    }
}
