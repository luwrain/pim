
package org.luwrain.pim.mail;

import java.util.*;

import org.luwrain.core.*;
import org.luwrain.pim.*;

class MailStoringMemory implements MailStoring
{
    MailStoringMemory()
		      {
		      }

    @Override public MailRules getRules()
    {
	return null;
    }

    @Override public MailMessages getMessages()
    {
	return null;
    }

    @Override public MailFolders getFolders()
    {
	return null;
    }

    @Override public StoredMailAccount[] loadAccounts() throws PimException
    {
	return null;
    }

    @Override public StoredMailAccount loadAccountById(long id) throws PimException
    {
	return null;
    }

    @Override public void saveAccount(MailAccount account) throws PimException
    {
	NullCheck.notNull(account, "account");
    }

    @Override public void deleteAccount(StoredMailAccount account) throws PimException
    {
	NullCheck.notNull(account, "account");
    }

    @Override public String getAccountUniRef(StoredMailAccount account) throws PimException
    {
	NullCheck.notNull(account, "account");
	return "";
    }

    @Override public StoredMailAccount getAccountByUniRef(String uniRef)
    {
	NullCheck.notEmpty(uniRef, "uniRef");
	return null;
    }

    @Override public int getAccountId(StoredMailAccount account) throws PimException
    {
	NullCheck.notNull(account, "account");
	return 0;
    }


}
