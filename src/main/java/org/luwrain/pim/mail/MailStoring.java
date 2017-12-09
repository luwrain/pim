
package org.luwrain.pim.mail;

import org.luwrain.pim.*;
//import org.luwrain.pim.mail.MailStoringSql.Condition;

public interface MailStoring extends Cloneable
{

    //accounts
    StoredMailAccount[] loadAccounts() throws PimException;
    int getAccountId(StoredMailAccount account) throws PimException;
    StoredMailAccount loadAccountById(long id) throws PimException;
    void saveAccount(MailAccount account) throws PimException;
    void deleteAccount(StoredMailAccount account) throws PimException;
    String getAccountUniRef(StoredMailAccount account) throws PimException;
    StoredMailAccount getAccountByUniRef(String uniRef) throws PimException;

    MailFolders getFolders();
    MailRules getRules();
    MailMessages getMessages();
}
