/*
   Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

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

package org.luwrain.app.mail.layouts;

import java.util.*;

import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.controls.*;
import org.luwrain.controls.list.*;
import org.luwrain.app.base.*;
import org.luwrain.app.mail.*;
import org.luwrain.pim.mail.persistence.model.*;

import static org.luwrain.core.DefaultEventResponse.*;
import static org.luwrain.core.events.InputEvent.*;

public final class AccountsLayout extends LayoutBase
{
    final App app;
    final List<Account> accounts = new ArrayList<>();
    final Data data;
    final ListArea<Account> accountsArea;

    public AccountsLayout(App app, ActionHandler closing)
    {
	super(app);
	this.app = app;
	this.data = app.getData();
	final var s = app.getStrings();
	accounts.addAll(data.accountDAO.getAll());

	this.accountsArea = new ListArea<Account>(listParams( p -> {
		    p.name = s.accountsAreaName();
		    p.model = new ListUtils.ListModel(accounts);
		    p.appearance = new Appearance();		    
		}));
	setCloseHandler(closing);
		setOkHandler(closing);
		setAreaLayout(accountsArea, actions(
						    action("insert", s.actionNewAccount(), new InputEvent(Special.INSERT), this::newAccount)
));
    }

    boolean newAccount()
    {
	final var type = app.conv.newAccountType();
	if (type == null)
	    return true;
	final var a = new Account();
	switch(type)
	{
	case POP3:
	    a.setType(Account.Type.POP3);
	    break;
	case SMTP:
	    a.setType(Account.Type.SMTP);
	    break;
	}
	a.setName(app.getStrings().newAccountName());
	data.accountDAO.add(a);
	accounts.clear();
	accounts.addAll(data.accountDAO.getAll());
	accountsArea.refresh();
	accountsArea.select(a, false);
	return true;
    }

    final class Appearance extends AbstractAppearance<Account>
    {
	@Override public void announceItem(Account account, Set<Flags> flags)
	{
	    app.setEventResponse(listItem(account.getName()));
	}

	@Override public String getScreenAppearance(Account account, Set<Flags> flags)
	{
	    return account.getName();
	}
    }
}
