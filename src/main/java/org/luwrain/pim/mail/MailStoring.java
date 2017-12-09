
package org.luwrain.pim.mail;

import org.luwrain.pim.*;
//import org.luwrain.pim.mail.MailStoringSql.Condition;

public interface MailStoring extends Cloneable
{
    StoredMailFolder getFoldersRoot() throws PimException;
    StoredMailFolder[] getFolders(StoredMailFolder folder) throws PimException;
    String getFolderUniRef(StoredMailFolder folder) throws PimException;
    StoredMailFolder getFolderByUniRef(String uniRef) throws PimException;
    int getFolderId(StoredMailFolder folder) throws PimException;
    StoredMailFolder loadFolderById(int id) throws PimException;
    void saveFolder(StoredMailFolder parentFolder, MailFolder newFolder) throws PimException;

    //accounts
    StoredMailAccount[] loadAccounts() throws PimException;
    int getAccountId(StoredMailAccount account) throws PimException;
    StoredMailAccount loadAccountById(long id) throws PimException;
    void saveAccount(MailAccount account) throws PimException;
    void deleteAccount(StoredMailAccount account) throws PimException;
    String getAccountUniRef(StoredMailAccount account) throws PimException;
    StoredMailAccount getAccountByUniRef(String uniRef) throws PimException;

    MailRules getRules();

    //messages
    void saveMessage(StoredMailFolder folder, MailMessage message) throws PimException;
    StoredMailMessage[] loadMessages(StoredMailFolder folder) throws PimException;
    void moveMessageToFolder(StoredMailMessage message, StoredMailFolder folder) throws PimException;
    void deleteMessage(StoredMailMessage message) throws PimException;
}
