/*
   Copyright 2012-2020 Michael Pozhidaev <msp@luwrain.org>

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
import org.luwrain.popups.Popups;
import org.luwrain.pim.mail.*;
import org.luwrain.settings.mail.*;

final class Conversations
{
    final Luwrain luwrain;
    final Strings strings;

    Conversations(Accounts accounts)
    {
	NullCheck.notNull(accounts, "accounts");
	this.luwrain = accounts.luwrain;
	this.strings = accounts.strings;
    }

    MailAccount.Type newAccountType()
    {
	final String pop3 = "POP3";
	final String smtp = "SMTP";
	final Object obj = Popups.fixedList(luwrain, strings.newAccountTypePopupName(), new String[]{pop3, smtp});
	if (obj == null)
	    return null;
	if (obj == pop3)
	    return MailAccount.Type.POP3;
	if (obj == smtp)
	    return MailAccount.Type.SMTP;
	return null;
    }
}
