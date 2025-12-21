
package org.luwrain.settings.mail.accounts;

import org.luwrain.core.*;
import org.luwrain.pim.mail.*;
import org.luwrain.pim.mail.persistence.*;
import org.luwrain.settings.mail.*;

import static org.luwrain.popups.Popups.*;

final class Conv
{
    final Luwrain luwrain;
    final org.luwrain.settings.mail.Strings strings;

    Conv(Accounts accounts)
    {
	this.luwrain = accounts.luwrain;
	this.strings = accounts.strings;
    }

    String newAccountTitle() { return textNotEmpty(luwrain, strings.newAccountTitlePopupName(), strings.newAccountTitlePopupPrefix(), ""); }
    boolean confirmAccountDeleting(String title) { return confirmDefaultNo(luwrain, "Удаление почтовой учётной записи", "Вы действительно хотите удалить почтовую запись \"" + title + "\"?"); } //FIXME:

    Account.Type newAccountType()
    {
	final String pop3 = "POP3";
	final String smtp = "SMTP";
	final Object obj = fixedList(luwrain, strings.newAccountTypePopupName(), new String[]{pop3, smtp});
	if (obj == null)
	    return null;
	if (obj == pop3)
	    return Account.Type.POP3;
	if (obj == smtp)
	    return Account.Type.SMTP;
	return null;
    }
}
