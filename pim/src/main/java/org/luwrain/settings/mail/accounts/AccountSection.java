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
import org.luwrain.pim.*;
import org.luwrain.pim.mail.persistence.dao.*;

import static org.luwrain.pim.mail.persistence.MailPersistence.*;

public final class AccountSection implements Section
{
    final Accounts accounts;
    final AccountElement element;
    final String title;
    final int id;

    public AccountSection(Accounts accounts, AccountElement element)
    {
	this.accounts = accounts;
	this.element = element;
	this.title = element.title;
	this.id = element.id;
    }

    @Override public SectionArea getSectionArea(ControlPanel controlPanel)
    {
	final Luwrain luwrain = controlPanel.getCoreInterface();
	final AccountDAO dao = getAccountDAO();
	return new Area(controlPanel, accounts.strings, dao, dao.getById(id));
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
	return accounts.onActionEvent(controlPanel, event, id);
    }

    @Override public String toString()
    {
	return title;
    }
}
