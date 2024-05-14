/*
   Copyright 2012-2024 Michael Pozhidaev <msp@luwrain.org>

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

import java.util.*;

import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.cpanel.*;
import org.luwrain.pim.mail.*;
import org.luwrain.pim.*;
import org.luwrain.pim.mail2.persistence.model.*;
import org.luwrain.pim.mail2.persistence.dao.*;
import org.luwrain.settings.mail.*;

import static org.luwrain.pim.mail2.persistence.MailPersistence.*;

public final class Accounts
{
    final Luwrain luwrain;
    final org.luwrain.settings.mail.Strings strings;
    final Conv conv;

    public Accounts(Luwrain luwrain, org.luwrain.settings.mail.Strings strings)
    {
	this.luwrain = luwrain;
	this.strings = strings;
	this.conv = new Conv(this);
    }

    public org.luwrain.cpanel.Element[] getAccountsElements(Element parent)
    {
	final var accounts = getAccountDAO().getAll();
	final var res = new ArrayList<Element>();
	for(var a: accounts)
	    res.add(new AccountElement(parent, a.getId(), a.getName()));
	return res.toArray(new Element[res.size()]);
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
	final Account.Type type = conv.newAccountType();
	if (type == null)
	    return true;
	final String title = conv.newAccountTitle();
	if (title == null)
	    return true;
	final Account account = new Account();
	account.setType(type);
	account.setName(title);
	switch(type)
	{
	case SMTP:
	    account.setPort(587);
	    account.setTls(true);
	    break;
	case POP3:
	    account.setPort(995);
	    account.setSsl(true);
	    break;
	}
	getAccountDAO().add(account);
	controlPanel.refreshSectionsTree();
	return true;
    }

    private boolean onDeleteAccount(ControlPanel controlPanel, int id)
    {
	if (id < 0)
	    return false;
	final Account account = getAccountDAO().getById(id);
	if (account == null)
	    return false;
	if (conv.confirmAccountDeleting(account.getName()))
	{
	    getAccountDAO().delete(account);
	    controlPanel.refreshSectionsTree();
	}
	return true;
    }
}
