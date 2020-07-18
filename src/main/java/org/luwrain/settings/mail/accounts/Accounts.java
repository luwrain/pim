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

import java.util.*;

import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.popups.Popups;
import org.luwrain.cpanel.*;
import org.luwrain.pim.mail.*;
import org.luwrain.pim.*;
import org.luwrain.settings.mail.*;

public final class Accounts
{
    final Luwrain luwrain;
    final Strings strings;
    final MailStoring storing;
    final Conversations conv;

    public Accounts(Luwrain luwrain, Strings strings, MailStoring storing)
    {
	NullCheck.notNull(luwrain, "luwrain");
	NullCheck.notNull(strings, "strings");
	NullCheck.notNull(storing, "storing");
	this.luwrain = luwrain;
	this.strings = strings;
	this.storing = storing;
	this.conv = new Conversations(this);
    }

    public org.luwrain.cpanel.Element[] getAccountsElements(Element parent)
    {
	try {
	    final MailAccount[] accounts = storing.getAccounts().load();
	    final Element[] res = new Element[accounts.length];
	    for(int i = 0;i < accounts.length;++i)
		res[i] = new AccountElement(parent, storing.getAccounts().getId(accounts[i]), accounts[i].getTitle());
	    return res;
	}
	catch(PimException e)
	{
	    luwrain.crash(e);
	    return new org.luwrain.cpanel.Element[0];
	}
    }

public Area createArea(ControlPanel controlPanel, int id)
    {
	NullCheck.notNull(controlPanel, "controlPanel");
	final Luwrain luwrain = controlPanel.getCoreInterface();
	try {
	    return new Area(controlPanel, strings, storing, storing.getAccounts().loadById(id));
	}
	catch(PimException e)
	{
	    luwrain.crash(e);
	    return null;
	}
    }

    public Action[] getActions()
    {
				     return new Action[]{
					 new Action("add-mail-account", strings.addMailAccount(), new InputEvent(InputEvent.Special.INSERT)),
					 new Action("delete-mail-account", strings.deleteAccount(), new InputEvent(InputEvent.Special.DELETE)),
				     };
    }

public boolean onActionEvent(ControlPanel controlPanel, ActionEvent event, int id)
    {
	NullCheck.notNull(controlPanel, "controlPanel");
	NullCheck.notNull(event, "event");
	if (ActionEvent.isAction(event, "add-mail-account"))
	    return onAddAccount(controlPanel);
	if (ActionEvent.isAction(event, "delete-mail-account"))
	{
	    if (id < 0)
		return false;
	    try {
		final MailAccount account = storing.getAccounts().loadById(id);
		if (account == null)
		    return false;
		if (Popups.confirmDefaultNo(luwrain, "Удаление почтовой учётной записи", "Вы действительно хотите удалить почтовую запись \"" + account.getTitle() + "\"?"))
		{
		    storing.getAccounts().delete(account);
		    controlPanel.refreshSectionsTree();
		}
		return true;
	    }
	    catch(PimException e)
	    {
		luwrain.crash(e);
		return false;
	    }
	}
	return false;
    }

    private boolean onAddAccount(ControlPanel controlPanel)
    {
	NullCheck.notNull(controlPanel, "controlPanel");
	    try {
		final MailAccount.Type type = conv.newAccountType();
		if (type == null)
		    return true;
		final MailAccount account = new MailAccount();
		account.setType(type);
		account.setTitle("Новая");
		storing.getAccounts().save(account);
		controlPanel.refreshSectionsTree();
		return true;
	    }
	    catch(PimException e)
	    {
		luwrain.crash(e);
		return true;
	    }
	}
    }
