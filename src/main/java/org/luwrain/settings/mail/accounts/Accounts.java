
package org.luwrain.settings.mail.accounts;

import java.util.*;

import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.popups.Popups;
import org.luwrain.cpanel.*;
import org.luwrain.pim.mail.*;
import org.luwrain.pim.*;
import org.luwrain.settings.mail.*;

public class Accounts
{
    private Luwrain luwrain;
    private Strings strings;
    private MailStoring storing = null;

    public Accounts(Luwrain luwrain, Strings strings,
MailStoring storing)
    {
	NullCheck.notNull(luwrain, "luwrain");
	NullCheck.notNull(strings, "strings");
	NullCheck.notNull(storing, "storing");
	this.luwrain = luwrain;
	this.strings = strings;
	this.storing = storing;
    }

    public org.luwrain.cpanel.Element[] getElements(org.luwrain.cpanel.Element parent)
    {
	try {
	    final StoredMailAccount[] accounts = storing.getAccounts().load();
	    final Element[] res = new Element[accounts.length];
	    for(int i = 0;i < accounts.length;++i)
		res[i] = new Element(parent, storing.getAccounts().getId(accounts[i]), accounts[i].getTitle());
	    return res;
	}
	catch(PimException e)
	{
	    luwrain.crash(e);
	    return new org.luwrain.cpanel.Element[0];
	}
    }

public Area createArea(ControlPanel controlPanel, long id)
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
					 new Action("add-mail-account", strings.addMailAccount(), new KeyboardEvent(KeyboardEvent.Special.INSERT)),
					 new Action("add-mail-account-predefined", strings.addAccountPredefined()),
					 new Action("delete-mail-account", strings.deleteAccount(), new KeyboardEvent(KeyboardEvent.Special.DELETE)),
				     };
    }

public boolean onActionEvent(ControlPanel controlPanel, ActionEvent event, long id)
    {
	NullCheck.notNull(controlPanel, "controlPanel");
	NullCheck.notNull(event, "event");
	//adding
	if (ActionEvent.isAction(event, "add-mail-account"))
	{
	    try {
		final MailAccount account = new MailAccount();
		account.title = "Новая";
		account.flags = EnumSet.of(MailAccount.Flags.ENABLED);
		storing.getAccounts().save(account);
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
		final StoredMailAccount account = storing.getAccounts().loadById(id);
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

	if (ActionEvent.isAction(event, "add-mail-account-predefined"))
	{

	    final String google = "gmail.com";
	    final String yandex = "yandex.ru";
	    final String mailRu = "mail.ru";

	    final Object res = Popups.fixedList(luwrain, "Выберите почтовую службу:", new String[]{google, mailRu, yandex});

	    try {
		if (res == google)
		    return addAccountGoogle(controlPanel);
		if (res == yandex)
		    return addAccountYandex(controlPanel);
		return true;

	    }
	    catch (PimException e)
	    {
		controlPanel.getCoreInterface().crash(e);
	    }
	    return false;
	}


	return false;
    }

private boolean addAccountGoogle(ControlPanel controlPanel) throws PimException
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
		final MailAccount account = new MailAccount();
		account.title = title.trim() + "@gmail.com(" + strings.incomingMailSuffix() + ")";
		account.flags = EnumSet.of(MailAccount.Flags.ENABLED, MailAccount.Flags.LEAVE_MESSAGES, MailAccount.Flags.SSL);
		account.type = MailAccount.Type.POP3;
		account.host = "pop.gmail.com";
		account.port = 995;
		account.login = title.trim() + "@gmail.com";
		account.passwd = passwd;
		account.substAddress = "";
		account.substName = "";
		storing.getAccounts().save(account);

		account.type = MailAccount.Type.SMTP;
		account.title = title.trim() + "@gmail.com (" + strings.outgoingMailSuffix() + ")";
		account.flags = EnumSet.of(MailAccount.Flags.ENABLED, MailAccount.Flags.DEFAULT, MailAccount.Flags.TLS);
		account.host = "smtp.gmail.com";
		account.port = 587;
		account.substAddress = account.login;
		account.substName = fullName;
		storing.getAccounts().save(account);

		controlPanel.refreshSectionsTree();
		return true;
}

	    private boolean addAccountYandex(ControlPanel controlPanel) throws PimException
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
		final MailAccount account = new MailAccount();
		account.title = title.trim() + "@yandex.ru (" + strings.incomingMailSuffix() + ")";
		account.flags = EnumSet.of(MailAccount.Flags.ENABLED, MailAccount.Flags.LEAVE_MESSAGES, MailAccount.Flags.SSL);
		account.type = MailAccount.Type.POP3;
		account.host = "pop3.yandex.ru";
		account.port = 995;
		account.login = title.trim() + "@yandex.ru";
		account.passwd = passwd;
		account.substAddress = "";
		account.substName = "";
		storing.getAccounts().save(account);

		account.type = MailAccount.Type.SMTP;
		account.title = title.trim() + "@yandex.ru (" + strings.outgoingMailSuffix() + ")";
		account.flags = EnumSet.of(MailAccount.Flags.ENABLED, MailAccount.Flags.DEFAULT, MailAccount.Flags.TLS);
		account.host = "smtp.yandex.ru";
		account.port = 587;
		account.substAddress = account.login;
		account.substName = fullName;
		storing.getAccounts().save(account);

		controlPanel.refreshSectionsTree();
		return true;
	    }
}
