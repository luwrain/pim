/*
   Copyright 2012-2022 Michael Pozhidaev <msp@luwrain.org>

   This file is part of LUWRAIN.

   LUWRAIN is free software; you can redistribute it and/or
   modify it under the terms of the GNU General Public
   License as published by the Free Software Foundation; either
   version 3 of the License, or (at your option) any later version.

   LUWRAIN is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
   General Public License for more details.
*/

package org.luwrain.settings.mail.accounts;

import org.luwrain.core.*;
import org.luwrain.pim.mail.*;
import org.luwrain.pim.mail.persistence.model.*;
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
