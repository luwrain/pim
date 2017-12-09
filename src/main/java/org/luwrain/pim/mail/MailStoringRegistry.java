
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
