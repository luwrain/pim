// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.mail.layouts;

import java.util.*;

import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.controls.*;
import org.luwrain.controls.list.*;
import org.luwrain.app.base.*;
import org.luwrain.app.mail.*;
import org.luwrain.pim.mail.persistence.*;

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
		    p.clickHandler = (area, index, account) -> onClick(account);
		}));
	setCloseHandler(closing);
		setOkHandler(closing);
		setAreaLayout(accountsArea, actions(
						    action("insert", s.actionNewAccount(), new InputEvent(Special.INSERT), this::newAccount)
));
    }

    boolean onClick(Account account)
    {
	if (account.getType() == null)
	    account.setType(Account.Type.SMTP);
	switch(account.getType())
	{
	case POP3:
	app.setAreaLayout(new Pop3AccountLayout(app, account, () -> {
		    app.setAreaLayout(AccountsLayout.this);
		    getLuwrain().announceActiveArea();
		    accountsArea.redraw();
		    return true;
	}));
	getLuwrain().announceActiveArea();
	return true;
	case SMTP:
	    	app.setAreaLayout(new SmtpAccountLayout(app, account, () -> {
		    app.setAreaLayout(AccountsLayout.this);
		    getLuwrain().announceActiveArea();
		    accountsArea.redraw();
		    return true;
	}));
	getLuwrain().announceActiveArea();
	return true;
	default:
	    throw new IllegalArgumentException("Unknown account type: " + account.getType());
	}
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
