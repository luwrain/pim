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
import org.luwrain.core.events.*;
import org.luwrain.cpanel.*;
import org.luwrain.pim.*;

public class AccountSection implements Section
{
    private final Accounts accounts;
    private final AccountElement element;
    private final String title;
    private final int id;

    public AccountSection(Accounts accounts, AccountElement element)
    {
	NullCheck.notNull(accounts, "accounts");
	NullCheck.notNull(element, "element");
	this.accounts = accounts;
	this.element = element;
	this.title = element.title;
	this.id = element.id;
    }

    @Override public SectionArea getSectionArea(ControlPanel controlPanel)
    {
	NullCheck.notNull(controlPanel, "controlPanel");
	final Luwrain luwrain = controlPanel.getCoreInterface();
	try {
	    return new Area(controlPanel, accounts.strings, accounts.storing, accounts.storing.getAccounts().loadById(id));
	}
	catch(PimException e)
	{
	    luwrain.crash(e);
	    return null;
	}
    }

    @Override public Element getElement()
    {
	return this.element;
    }

    @Override public Action[] getSectionActions()
    {
	return new Action[]{
	    new Action("add-mail-account", accounts.strings.addMailAccount(), new InputEvent(InputEvent.Special.INSERT)),
	    new Action("delete-mail-account", accounts.strings.deleteAccount(), new InputEvent(InputEvent.Special.DELETE)),
	};
    }

    @Override public boolean onSectionActionEvent(ControlPanel controlPanel, ActionEvent event)
    {
	NullCheck.notNull(controlPanel, "controlPanel");
	NullCheck.notNull(event, "event");
	return accounts.onActionEvent(controlPanel, event, id);
    }

    @Override public String toString()
    {
	return title;
    }
}
