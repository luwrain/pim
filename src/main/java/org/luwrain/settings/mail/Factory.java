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

package org.luwrain.settings.mail;

import java.util.*;

import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.cpanel.*;
import org.luwrain.pim.*;
import org.luwrain.pim.mail.*;

import org.luwrain.settings.mail.accounts.*;

public final class Factory implements org.luwrain.cpanel.Factory
{
    private final Luwrain luwrain;
    private final SimpleElement mailElement, accountsElement, commonSettElement;

    private Strings strings = null;
    private MailStoring storing = null;
    private Accounts accounts = null;

    public Factory(Luwrain luwrain)
    {
	NullCheck.notNull(luwrain, "luwrain");
	this.luwrain = luwrain;
	this.mailElement = new SimpleElement(StandardElements.APPLICATIONS, this.getClass().getName());
	this.accountsElement = new SimpleElement(mailElement, this.getClass().getName() + ":Accounts");
		this.commonSettElement = new SimpleElement(mailElement, this.getClass().getName() + ":Settings");
    }

    @Override public Element[] getElements()
    {
	if (!initStrings())
	    return new Element[0];
	return new Element[]{ mailElement, accountsElement };
    }

    @Override public Element[] getOnDemandElements(Element parent)
    {
	if (!initStoring())
	    return new Element[0];
	if (parent.equals(accountsElement))
	    return accounts.getAccountsElements(parent);
	return new Element[0];
    }

    @Override public Section createSection(Element el)
    {
	if (el.equals(mailElement))
	    return new SimpleSection(mailElement, strings.mailSection());
	if (el.equals(accountsElement))
	    return new AccountsSection(accounts, el);
	if (el instanceof AccountElement)
	    return new AccountSection(accounts, (AccountElement)el);
	return null;
    }

    private boolean initStoring()
    {
	if (storing != null)
	    return true;
	storing = org.luwrain.pim.Connections.getMailStoring(luwrain, true);
	if (storing == null)
	    return false;
	this.accounts = new Accounts(luwrain, strings);
	return true;
    }

    private boolean initStrings()
    {
	final Object o = luwrain.i18n().getStrings(Strings.NAME);
	if (o != null && (o instanceof Strings))
	{
	    this.strings = (Strings)o;
	    return true;
	}
	this.strings = null;
	return false;
    }
}
