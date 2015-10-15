/*
   Copyright 2012-2015 Michael Pozhidaev <michael.pozhidaev@gmail.com>
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

import org.luwrain.core.Registry;
import org.luwrain.core.NullCheck;
import org.luwrain.util.RegistryAutoCheck;
import org.luwrain.util.RegistryPath;
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

    @Override public StoredMailFolder getFoldersRoot() throws Exception
    {
	final StoredMailFolderRegistry[] folders = loadAllFolders();
	for(StoredMailFolderRegistry f: folders)
	    if (f.id == f.parentId)
		return f;
	return null;
    }

    @Override public StoredMailFolder[] getFolders(StoredMailFolder folder) throws Exception
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

    @Override public String getFolderUniRef(StoredMailFolder folder) throws Exception
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

    @Override public StoredMailRule[] getRules() throws Exception
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

    @Override public void saveRule(MailRule rule) throws Exception
    {
	NullCheck.notNull(rule, "rule");
	final int newId = org.luwrain.pim.Util.newFolderId(registry, registryKeys.mailRules());
	final String path = RegistryPath.join(registryKeys.mailRules(), "" + newId);
	if (!registry.addDirectory(path))
	    throw new Exception("Unable to create new registry directory " + path);
	if (!registry.setString(RegistryPath.join(path, "action"), StoredMailRuleRegistry.getActionStr(rule.action)))
	    throw new Exception("Unable to set a string value " + RegistryPath.join(path, "action"));
	if (!registry.setString(RegistryPath.join(path, "header-regex"), rule.headerRegex))
	    throw new Exception("Unable to set a string value " + RegistryPath.join(path, "header-regex"));
	if (!registry.setString(RegistryPath.join(path, "dest-folder-uniref"), rule.destFolderUniRef))
	    throw new Exception("Unable to set a string value " + RegistryPath.join(path, "dest-folder-uniref"));
    }

    @Override public void deleteRule(StoredMailRule rule) throws Exception
    {
	NullCheck.notNull(rule, "rule");
	if (!(rule instanceof StoredMailRuleRegistry))
	    throw new IllegalArgumentException("rule is not an instance of StoredMailRuleRegistry");
	final StoredMailRuleRegistry ruleRegistry = (StoredMailRuleRegistry)rule;
	final String path = RegistryPath.join(registryKeys.mailRules(), "" + ruleRegistry.id);
	if (!registry.deleteDirectory(path))
	    throw new Exception("Unable to delete the registry directory " + path);
    }

    @Override public StoredMailAccount[] loadAccounts() throws Exception
    {
	final String[] subdirs = registry.getDirectories(registryKeys.mailAccounts());
	if (subdirs == null || subdirs.length < 1)
	    return new StoredMailAccount[0];
	final LinkedList<StoredMailAccountRegistry> accounts = new LinkedList<StoredMailAccountRegistry>();
	for(String s: subdirs)
	{
	    if (s == null || s.isEmpty())
		continue;
	    int id = 0;
	    try {
		id = Integer.parseInt(s);
	    }
	    catch(NumberFormatException e)
	    {
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

    @Override public void saveAccount(MailAccount account) throws Exception
    {
	NullCheck.notNull(account, "account");
	final int newId = org.luwrain.pim.Util.newFolderId(registry, registryKeys.mailAccounts());
	final String path = RegistryPath.join(registryKeys.mailAccounts(), "" + newId);
	if (!registry.addDirectory(path))
	    throw new Exception("Unable to create new registry directory " + path);
	if (!registry.setString(RegistryPath.join(path, "type"), StoredMailAccountRegistry.getTypeStr(account.type)))
	    throw new Exception("Unable to set a string value " + RegistryPath.join(path, "type"));
	if (!registry.setString(RegistryPath.join(path, "title"), account.title))
	    throw new Exception("Unable to set a string value " + RegistryPath.join(path, "title"));
	if (!registry.setString(RegistryPath.join(path, "host"), account.host))
	    throw new Exception("Unable to set a string value " + RegistryPath.join(path, "host"));
	if (!registry.setInteger(RegistryPath.join(path, "port"), account.port))
	    throw new Exception("Unable to set a string value " + RegistryPath.join(path, "hport"));
	if (!registry.setString(RegistryPath.join(path, "login"), account.login))
	    throw new Exception("Unable to set a string value " + RegistryPath.join(path, "login"));
	if (!registry.setString(RegistryPath.join(path, "passwd"), account.passwd))
	    throw new Exception("Unable to set a string value " + RegistryPath.join(path, "passwd"));
	if (!registry.setString(RegistryPath.join(path, "trusted-hosts"), account.trustedHosts))
	    throw new Exception("Unable to set a string value " + RegistryPath.join(path, "trusted-hosts"));
	if (!registry.setString(RegistryPath.join(path, "subst-name"), account.substName))
	    throw new Exception("Unable to set a string value " + RegistryPath.join(path, "subst-name"));
	if (!registry.setString(RegistryPath.join(path, "subst-address"), account.substAddress))
	    throw new Exception("Unable to set a string value " + RegistryPath.join(path, "subst-address"));
	final boolean enabled = (account.flags & MailAccount.FLAG_ENABLED) > 0;
	final boolean ssl = (account.flags & MailAccount.FLAG_SSL) > 0;
	final boolean tls = (account.flags & MailAccount.FLAG_TLS) > 0;
	final boolean def = (account.flags & MailAccount.FLAG_DEFAULT) > 0;
	final boolean leaveMessages = (account.flags & MailAccount.FLAG_LEAVE_MESSAGES) > 0;
	if (!registry.setBoolean(RegistryPath.join(path, "enabled"), enabled))
	    throw new Exception("Unable to set a string value " + RegistryPath.join(path, "enabled"));
	if (!registry.setBoolean(RegistryPath.join(path, "ssl"), ssl))
	    throw new Exception("Unable to set a string value " + RegistryPath.join(path, "ssl"));
	if (!registry.setBoolean(RegistryPath.join(path, "tls"), tls))
	    throw new Exception("Unable to set a string value " + RegistryPath.join(path, "tls"));
	if (!registry.setBoolean(RegistryPath.join(path, "default"), def))
	    throw new Exception("Unable to set a string value " + RegistryPath.join(path, "default"));
	if (!registry.setBoolean(RegistryPath.join(path, "leave-messages"), leaveMessages))
	    throw new Exception("Unable to set a string value " + RegistryPath.join(path, "leave-messages"));
    }

    @Override public void deleteAccount(StoredMailAccount account) throws Exception
    {
	NullCheck.notNull(account, "account");
	if (!(account instanceof StoredMailAccountRegistry))
	    throw new IllegalArgumentException("account is not an instance of StoredMailRAccountRegistry");
	final StoredMailAccountRegistry accountRegistry = (StoredMailAccountRegistry)account;
	final String path = RegistryPath.join(registryKeys.mailAccounts(), "" + accountRegistry.id);
	if (!registry.deleteDirectory(path))
	    throw new Exception("Unable to delete the registry directory " + path);
    }

    @Override public String getAccountUniRef(StoredMailAccount account) throws Exception
    {
	if (account == null || !(account instanceof StoredMailAccountRegistry))
	    return "";
	final StoredMailAccountRegistry accountRegistry = (StoredMailAccountRegistry)account;
	return AccountUniRefProc.PREFIX + ":" + accountRegistry.id;
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
