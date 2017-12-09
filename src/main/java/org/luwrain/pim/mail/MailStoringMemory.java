
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

	    @Override public int getFolderId(StoredMailFolder folder)
    {
	NullCheck.notNull(folder, "folder");
	return 0;
    }

    @Override public StoredMailFolder loadFolderById(int id)
    {
	return null;
    }

    @Override public void saveFolder(StoredMailFolder parentFolder, MailFolder newFolder)
    {
	NullCheck.notNull(parentFolder, "parentFolder");
	NullCheck.notNull(parentFolder, "parentFolder");
    }

    @Override public StoredMailFolder getFoldersRoot()
    {
	return null;
    }

    @Override public StoredMailFolder[] getFolders(StoredMailFolder folder)
    {
	return null;
    }

    @Override public String getFolderUniRef(StoredMailFolder folder) throws PimException
    {
	return null;
    }

    @Override public StoredMailFolder getFolderByUniRef(String uniRef)
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


    @Override public void saveMessage(StoredMailFolder folder, MailMessage message)
    {
	NullCheck.notNull(folder, "folder");
	NullCheck.notNull(message, "message");
    }

    @Override public StoredMailMessage[] loadMessages(StoredMailFolder folder)
    {
	NullCheck.notNull(folder, "folder");
	return null;
    }

    @Override public void moveMessageToFolder(StoredMailMessage message, StoredMailFolder folder)
    {
	NullCheck.notNull(folder, "folder");
	NullCheck.notNull(message, "message");
    }

    @Override public void deleteMessage(StoredMailMessage message)
    {
	NullCheck.notNull(message, "message");
    }
}
