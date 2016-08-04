/*
   Copyright 2012-2016 Michael Pozhidaev <michael.pozhidaev@gmail.com>

   This file is part of the LUWRAIN.

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
import org.luwrain.popups.Popups;
import org.luwrain.cpanel.*;
import org.luwrain.pim.mail.*;
import org.luwrain.pim.*;

public class Factory implements org.luwrain.cpanel.Factory
{
    private Luwrain luwrain;
    private Strings strings;
    private MailStoring storing = null;
    private SimpleElement mailElement = null;

    private SimpleElement accountsElement = null;
    private SimpleElement rulesElement = null;
    private SimpleElement groupsElement = null;

    public Factory(Luwrain luwrain)
    {
	NullCheck.notNull(luwrain, "luwrain");
	this.luwrain = luwrain;
    }

    @Override public Element[] getElements()
    {
	if (!init())
	    return new Element[0];
	return new Element[]{
	    mailElement,
	    groupsElement,
	    accountsElement,
	    rulesElement,
	};
    }

    @Override public Element[] getOnDemandElements(Element parent)
    {
	NullCheck.notNull(parent, "parent");
	if (!initStoring())
	    return new Element[0];
	if (parent.equals(accountsElement))
	{
	    try {
		final StoredMailAccount[] accounts = storing.loadAccounts();
		final Element[] res = new Element[accounts.length];
		for(int i = 0;i < accounts.length;++i)
		    res[i] = new AccountElement(parent, accounts[i].getId(), accounts[i].getTitle());
		return res;
	    }
	    catch(PimException e)
	    {
		luwrain.crash(e);
	    }
	}
	return new Element[0];
    }

    @Override public Section createSection(Element el)
    {
	NullCheck.notNull(el, "el");
	if (el.equals(mailElement))
	    return new SimpleSection(mailElement, strings.mailSection());

	if (el.equals(accountsElement))
	    return new SimpleSection(accountsElement, strings.accountsSection(), null,
				     new Action[]{
					 new Action("add-mail-account-yandex", strings.addAccountYandex()),
					 new Action("add-mail-account", strings.addMailAccount(), new KeyboardEvent(KeyboardEvent.Special.INSERT)),
					 new Action("add-mail-account-google", strings.addAccountGoogle()),
				     }, (controlPanel, event)->onAccountsActionEvent(controlPanel, event, -1));

	if (el.equals(groupsElement))
	    return new SimpleSection(groupsElement, strings.groupsSection());
	if (el.equals(rulesElement))
	    return new SimpleSection(rulesElement, strings.rulesSection());

	if (el instanceof AccountElement)
	    return new SimpleSection(el, ((AccountElement)el).title(), (controlPanel)->Account.create(controlPanel, storing, ((AccountElement)el).id()),
				     new Action[]{
					 new Action("add-mail-account", strings.addMailAccount(), new KeyboardEvent(KeyboardEvent.Special.INSERT)),
					 new Action("add-mail-account-google", strings.addAccountGoogle()),
					 new Action("add-mail-account-yandex", strings.addAccountYandex()),
					 new Action("delete-mail-account", strings.deleteAccount(), new KeyboardEvent(KeyboardEvent.Special.DELETE)),
				     }, (controlPanel, event)->onAccountsActionEvent(controlPanel, event, ((AccountElement)el).id()));

	return null;
    }

    private boolean onAccountsActionEvent(ControlPanel controlPanel, ActionEvent event, long id)
    {
	NullCheck.notNull(controlPanel, "controlPanel");
	NullCheck.notNull(event, "event");
	if (!initStoring())
	{
	    luwrain.message(strings.noStoring(), Luwrain.MESSAGE_ERROR);
	    return true;
	}

	//adding
	if (ActionEvent.isAction(event, "add-mail-account"))
	{
	    try {
		final MailAccount account = new MailAccount();
		account.title = "Новая";
		account.flags = MailAccount.FLAG_ENABLED;
		storing.saveAccount(account);
		controlPanel.refreshSectionsTree();
		return true;
	    }
	    catch(PimException e)
	    {
		luwrain.crash(e);
		return false;
	    }
	}

	//deleting
	if (ActionEvent.isAction(event, "delete-mail-account"))
	{
	    if (id < 0)
		return false;
	    try {
		final StoredMailAccount account = storing.loadAccountById(id);
		if (account == null)
		    return false;
		if (Popups.confirmDefaultNo(luwrain, "Удаление почтовой учётной записи", "Вы действительно хотите удалить почтовую запись \"" + account.getTitle() + "\"?"))
		{
		    storing.deleteAccount(account);
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

	//yandex
	if (ActionEvent.isAction(event, "add-mail-account-yandex"))
	{
	    final String title = Popups.simple(luwrain, strings.addAccountPopupName(), strings.yourYandexAccountQuestion(), "");
	    if (title == null)
		return true;
	    final String fullName = Popups.simple(luwrain, strings.addAccountPopupName(), strings.yourFullNameQuestion(), "");
	    if (fullName == null)
		return true;
	    final String passwd = Popups.simple(luwrain, strings.addAccountPopupName(), strings.yourYandexPasswordQuestion(title), "");
	    if (passwd == null)
		return true;
	    try {
		final MailAccount account = new MailAccount();
		account.title = title.trim() + "@yandex.ru (" + strings.incomingMailSuffix() + ")";
		account.flags = MailAccount.FLAG_ENABLED | MailAccount.FLAG_LEAVE_MESSAGES | MailAccount.FLAG_SSL;
		account.type = MailAccount.POP3;
		account.host = "pop3.yandex.ru";
		account.port = 995;
		account.login = title.trim() + "@yandex.ru";
		account.passwd = passwd;
		account.substAddress = "";
		account.substName = "";
		storing.saveAccount(account);

		account.type = MailAccount.SMTP;
		account.title = title.trim() + "@yandex.ru (" + strings.outgoingMailSuffix() + ")";
		account.flags = MailAccount.FLAG_ENABLED | MailAccount.FLAG_DEFAULT | MailAccount.FLAG_TLS;
		account.host = "smtp.yandex.ru";
		account.port = 587;
		account.substAddress = account.login;
		account.substName = fullName;
		storing.saveAccount(account);

		controlPanel.refreshSectionsTree();
		return true;
	    }
	    catch(PimException e)
	    {
		luwrain.crash(e);
		return false;
	    }
	}

	//google
	if (ActionEvent.isAction(event, "add-mail-account-google"))
	{
	    final String title = Popups.simple(luwrain, strings.addAccountPopupName(), strings.yourGoogleAccountQuestion(), "");
	    if (title == null)
		return true;
	    final String fullName = Popups.simple(luwrain, strings.addAccountPopupName(), strings.yourFullNameQuestion(), "");
	    if (fullName == null)
		return true;
	    final String passwd = Popups.simple(luwrain, strings.addAccountPopupName(), strings.yourGooglePasswordQuestion(title), "");
	    if (passwd == null)
		return true;
	    try {
		final MailAccount account = new MailAccount();
		account.title = title.trim() + "@gmail.com(" + strings.incomingMailSuffix() + ")";
		account.flags = MailAccount.FLAG_ENABLED | MailAccount.FLAG_LEAVE_MESSAGES | MailAccount.FLAG_SSL;
		account.type = MailAccount.POP3;
		account.host = "pop.gmail.com";
		account.port = 995;
		account.login = title.trim() + "@gmail.com";
		account.passwd = passwd;
		account.substAddress = "";
		account.substName = "";
		storing.saveAccount(account);

		account.type = MailAccount.SMTP;
		account.title = title.trim() + "@gmail.com (" + strings.outgoingMailSuffix() + ")";
		account.flags = MailAccount.FLAG_ENABLED | MailAccount.FLAG_DEFAULT | MailAccount.FLAG_TLS;
		account.host = "smtp.gmail.com";
		account.port = 587;
		account.substAddress = account.login;
		account.substName = fullName;
		storing.saveAccount(account);

		controlPanel.refreshSectionsTree();
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

    private boolean init()
    {
	if (strings == null)
	{
	    final Object o = luwrain.i18n().getStrings(Strings.NAME);
	    if (o != null && (o instanceof Strings))
		strings = (Strings)o; else
		return false;
	}
	if (mailElement == null)
	    mailElement = new SimpleElement(StandardElements.APPLICATIONS, this.getClass().getName());
	if (accountsElement == null)
	    accountsElement = new SimpleElement(mailElement, this.getClass().getName() + ":Accounts");
	if (groupsElement == null)
	    groupsElement = new SimpleElement(mailElement, this.getClass().getName() + ":Groups");
	if (rulesElement == null)
	    rulesElement = new SimpleElement(mailElement, this.getClass().getName() + ":Rules");
	return true;
    }

    private boolean initStoring()
    {
	final Object obj = luwrain.getSharedObject("luwrain.pim.mail");
	if (obj == null || !(obj instanceof org.luwrain.pim.mail.Factory))
	    return false;
	storing = ((org.luwrain.pim.mail.Factory)obj).createMailStoring();
	return storing != null;
    }
}
