
package org.luwrain.settings.mail;

import java.util.*;

import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.cpanel.*;
import org.luwrain.pim.*;
import org.luwrain.pim.mail.*;

import org.luwrain.settings.mail.accounts.Accounts;
import org.luwrain.settings.mail.folders.Folders;

public class Factory implements org.luwrain.cpanel.Factory
{
    private final Luwrain luwrain;
    private Strings strings = null;
    private MailStoring storing = null;

    private Accounts accounts = null;
    private Folders folders = null;

    private SimpleElement mailElement = null;
    private SimpleElement accountsElement = null;
    private SimpleElement rulesElement = null;
    private SimpleElement foldersElement = null;

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
	    foldersElement,
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
	    return accounts.getElements(parent);
	if (parent.equals(foldersElement) || (parent instanceof org.luwrain.settings.mail.folders.Element))
	    return folders.getElements(parent);
	return new Element[0];
    }

    @Override public Section createSection(Element el)
    {
	NullCheck.notNull(el, "el");
	if (el.equals(mailElement))
	    return new SimpleSection(mailElement, strings.mailSection());
	if (el.equals(accountsElement))
	    return new SimpleSection(accountsElement, strings.accountsSection(), null,
				     accounts.getActions(), (controlPanel, event)->accounts.onActionEvent(controlPanel, event, -1));
	if (el.equals(foldersElement))
	    return new SimpleSection(foldersElement, strings.groupsSection(), null,
				     folders.getActions(false), (controlPanel, event)->folders.onActionEvent(controlPanel, event, -1));
	if (el.equals(rulesElement))
	    return new SimpleSection(rulesElement, strings.rulesSection());
	if (el instanceof org.luwrain.settings.mail.accounts.Element)
	{
	    final org.luwrain.settings.mail.accounts.Element accountElement = (org.luwrain.settings.mail.accounts.Element)el;
	    return new SimpleSection(el, accountElement.title, (controlPanel)->accounts.createArea(controlPanel, accountElement.id),
				     accounts.getActions(), (controlPanel, event)->accounts.onActionEvent(controlPanel, event, accountElement.id));
	}
	if (el instanceof org.luwrain.settings.mail.folders.Element)
	{
	    final org.luwrain.settings.mail.folders.Element folderElement = (org.luwrain.settings.mail.folders.Element)el;
	    return new SimpleSection(el, folderElement.title, (controlPanel)->folders.createArea(controlPanel, folderElement.id),
				     folders.getActions(true), (controlPanel, event)->folders.onActionEvent(controlPanel, event, folderElement.id));
	}
	return null;
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
	if (foldersElement == null)
	    foldersElement = new SimpleElement(mailElement, this.getClass().getName() + ":Groups");
	if (rulesElement == null)
	    rulesElement = new SimpleElement(mailElement, this.getClass().getName() + ":Rules");
	return true;
    }

    private boolean initStoring()
    {
	if (storing != null)
	    return true;
	storing = org.luwrain.pim.Connections.getMailStoring(luwrain);
	if (storing == null)
	    return false;
	accounts = new Accounts(luwrain, strings, storing);
	folders = new Folders(luwrain, strings, storing);
	return true;
    }
}
