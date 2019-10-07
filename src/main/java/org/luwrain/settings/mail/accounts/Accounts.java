
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
	    final MailAccount[] accounts = storing.getAccounts().load();
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
					 new Action("add-mail-account", strings.addMailAccount(), new KeyboardEvent(KeyboardEvent.Special.INSERT)),
					 new Action("add-mail-account-predefined", strings.addAccountPredefined()),
					 new Action("delete-mail-account", strings.deleteAccount(), new KeyboardEvent(KeyboardEvent.Special.DELETE)),
				     };
    }

public boolean onActionEvent(ControlPanel controlPanel, ActionEvent event, int id)
    {
	NullCheck.notNull(controlPanel, "controlPanel");
	NullCheck.notNull(event, "event");
	//adding
	if (ActionEvent.isAction(event, "add-mail-account"))
	{
	    try {
		final MailAccount account = new MailAccount();
		account.setTitle("Новая");
		//		account.flags = EnumSet.of(MailAccount.Flags.ENABLED);
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
}
