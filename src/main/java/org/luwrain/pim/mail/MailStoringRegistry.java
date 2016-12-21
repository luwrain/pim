
package org.luwrain.pim.mail;

import java.util.*;

import org.luwrain.core.*;
import org.luwrain.pim.*;

abstract class MailStoringRegistry implements MailStoring
{
    private final Registry registry;

    MailStoringRegistry(Registry registry)
    {
	NullCheck.notNull(registry, "registry");
	this.registry = registry;
    }

    //folders

    @Override public int getFolderId(StoredMailFolder folder)
    {
	NullCheck.notNull(folder, "folder");
	if (!(folder instanceof StoredMailFolderRegistry))
	    throw new IllegalArgumentException("folder must be an instance of StoredMailFolderRegistry");
	return ((StoredMailFolderRegistry)folder).id;
    }

    @Override public StoredMailFolder loadFolderById(int id)
    {
	final StoredMailFolderRegistry folder = new StoredMailFolderRegistry(registry, id);
	return folder.load()?folder:null;
    }

    @Override public void saveFolder(StoredMailFolder parentFolder, MailFolder newFolder)
    {
	NullCheck.notNull(parentFolder, "parentFolder");
	NullCheck.notNull(parentFolder, "parentFolder");
	if (!(parentFolder instanceof StoredMailFolderRegistry))
	    throw new IllegalArgumentException("parentFolder must be an instance of StoredMailFolderRegistry");
	final StoredMailFolderRegistry parent = (StoredMailFolderRegistry)parentFolder;
	final int newId = Registry.nextFreeNum(registry, Settings.FOLDERS_PATH);
	final String path = Registry.join(Settings.FOLDERS_PATH, "" + newId);
	registry.addDirectory(path);
	final Settings.Folder sett = Settings.createFolder(registry, path);
	sett.setTitle(newFolder.title);
	sett.setOrderIndex(newFolder.orderIndex);
	sett.setParentId(parent.id);
    }

    @Override public StoredMailFolder getFoldersRoot()
    {
	final StoredMailFolderRegistry[] folders = loadAllFolders();
	for(StoredMailFolderRegistry f: folders)
	    if (f.id == f.parentId)
		return f;
	return null;
    }

    @Override public StoredMailFolder[] getFolders(StoredMailFolder folder)
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

    private StoredMailFolderRegistry[] loadAllFolders()
    {
	final String[] subdirs = registry.getDirectories(Settings.FOLDERS_PATH);
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

    //rules

    @Override public StoredMailRule[] getRules() throws PimException
    {
	final String[] dirNames = registry.getDirectories(Settings.RULES_PATH);
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
	final int newId = org.luwrain.pim.Util.newFolderId(registry, Settings.RULES_PATH);
	final String path = Registry.join(Settings.RULES_PATH, "" + newId);
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
	final String path = Registry.join(Settings.RULES_PATH, "" + ruleRegistry.id);
	if (!registry.deleteDirectory(path))
	    throw new PimException("Unable to delete the registry directory " + path);
    }

    //accounts

    @Override public StoredMailAccount[] loadAccounts() throws PimException
    {
	registry.addDirectory(Settings.ACCOUNTS_PATH);
	final LinkedList<StoredMailAccountRegistry> accounts = new LinkedList<StoredMailAccountRegistry>();
	for(String s: registry.getDirectories(Settings.ACCOUNTS_PATH))
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
	final int newId = Registry.nextFreeNum(registry, Settings.ACCOUNTS_PATH);
	final String path = Registry.join(Settings.ACCOUNTS_PATH, "" + newId);
	registry.addDirectory(path);
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
	    throw new PimException("account must be an instance of StoredMailAccountRegistry");
	final StoredMailAccountRegistry accountRegistry = (StoredMailAccountRegistry)account;
	final String path = Registry.join(Settings.ACCOUNTS_PATH, "" + accountRegistry.id);
	if (!registry.deleteDirectory(path))
	    throw new PimException("Unable to delete the registry directory " + path);
    }

    @Override public String getAccountUniRef(StoredMailAccount account) throws PimException
    {
	NullCheck.notNull(account, "account");
	if (!(account instanceof StoredMailAccountRegistry))
	    throw new PimException("account must be an instance of StoredMailAccountRegistry");
	final StoredMailAccountRegistry accountRegistry = (StoredMailAccountRegistry)account;
	return AccountUniRefProc.PREFIX + ":" + accountRegistry.id;
    }

    @Override public StoredMailAccount getAccountByUniRef(String uniRef)
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
	final StoredMailAccountRegistry account = new StoredMailAccountRegistry(registry, id);
	return account.load()?account:null;
    }

    @Override public int getAccountId(StoredMailAccount account) throws PimException
    {
	NullCheck.notNull(account, "account");
	if (!(account instanceof StoredMailAccountRegistry))
	    throw new PimException("account must be an instance of StoredMailAccountRegistry");
	return ((StoredMailAccountRegistry)account).id;
    }
}
