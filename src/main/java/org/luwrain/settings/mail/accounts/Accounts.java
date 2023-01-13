/*
   Copyright 2012-2023 Michael Pozhidaev <msp@luwrain.org>

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
import org.luwrain.core.events.*;
import org.luwrain.cpanel.*;
import org.luwrain.pim.mail.*;
import org.luwrain.pim.*;
import org.luwrain.settings.mail.*;

public final class Accounts
{
    final Luwrain luwrain;
    final org.luwrain.settings.mail.Strings strings;
    final MailStoring storing;
    final Conv conv;

    public Accounts(Luwrain luwrain, org.luwrain.settings.mail.Strings strings, MailStoring storing)
    {
	this.luwrain = luwrain;
	this.strings = strings;
	this.storing = storing;
	this.conv = new Conv(this);
    }

    public org.luwrain.cpanel.Element[] getAccountsElements(Element parent)
    {
	final MailAccount[] accounts = storing.getAccounts().load();
	final Element[] res = new Element[accounts.length];
	for(int i = 0;i < accounts.length;++i)
	    res[i] = new AccountElement(parent, storing.getAccounts().getId(accounts[i]), accounts[i].getTitle());
	return res;
    }

    boolean onActionEvent(ControlPanel controlPanel, ActionEvent event, int id)
    {
	if (ActionEvent.isAction(event, "add-mail-account"))
	    return onAddAccount(controlPanel);
	if (ActionEvent.isAction(event, "delete-mail-account"))
	    return onDeleteAccount(controlPanel, id);
	return false;
    }

    private boolean onAddAccount(ControlPanel controlPanel)
    {
	final MailAccount.Type type = conv.newAccountType();
	if (type == null)
	    return true;
	final String title = conv.newAccountTitle();
	if (title == null)
	    return true;
	final MailAccount account = new MailAccount();
	account.setType(type);
	account.setTitle(title);
	switch(type)
	{
	case SMTP:
	    account.setPort(587);
	    account.getFlags().add(MailAccount.Flags.TLS);
	    break;
	case POP3:
	    account.setPort(995);
	    account.getFlags().add(MailAccount.Flags.SSL);
	    break;
	}
	storing.getAccounts().save(account);
	controlPanel.refreshSectionsTree();
	return true;
    }

    private boolean onDeleteAccount(ControlPanel controlPanel, int id)
    {
	if (id < 0)
	    return false;
	final MailAccount account = storing.getAccounts().loadById(id);
	if (account == null)
	    return false;
	if (conv.confirmAccountDeleting(account.getTitle()))
	{
	    storing.getAccounts().delete(account);
	    controlPanel.refreshSectionsTree();
	}
	return true;
    }
}
