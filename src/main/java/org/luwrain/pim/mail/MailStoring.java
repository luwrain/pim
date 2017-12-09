
package org.luwrain.pim.mail;

import org.luwrain.pim.*;
//import org.luwrain.pim.mail.MailStoringSql.Condition;

public interface MailStoring extends Cloneable
{
    MailAccounts getAccounts();
   MailFolders getFolders();
    MailRules getRules();
    MailMessages getMessages();
}
