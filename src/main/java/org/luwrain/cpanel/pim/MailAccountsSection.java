/*
   Copyright 2012-2015 Michael Pozhidaev <michael.pozhidaev@gmail.com>
   Copyright 2015 Roman Volovodov <gr.rPman@gmail.com>

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

package org.luwrain.cpanel.pim;

import org.luwrain.core.*;
import org.luwrain.cpanel.*;
import org.luwrain.popups.Popups;
import org.luwrain.pim.mail.*;

class MailAccountsSection extends EmptySection
{
    private MailAccountSection[] childSections = null;
    private MailStoring storing;

    MailAccountsSection(MailStoring storing)
    {
	this.storing = storing;
	NullCheck.notNull(storing, "storing");
    }

    @Override public Section[] getChildSections()
    {
	constructChildSections();
	return childSections;
    }

    @Override public boolean onTreeInsert(Environment environment)
    {
	addNew(storing, environment);
	return true;
    }

    @Override public void refreshChildSubsections()
    {
	if (childSections == null)
	    return;
	childSections = null;
	constructChildSections();
    }

    @Override public String toString()
    {
	return "Учётные записи";//FIXME:
    }

    private void constructChildSections()
    {
	if (childSections != null)
	    return;
	try {
	    final StoredMailAccount[] accounts = storing.loadAccounts();
	    if (accounts == null || accounts.length < 1)
	    {
		childSections = new MailAccountSection[0];
		return;
	    }
	    childSections = new MailAccountSection[accounts.length];
	    for(int i = 0;i < accounts.length;++i)
		childSections[i] = new MailAccountSection(storing, accounts[i]);
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	    childSections = new MailAccountSection[0];
	}
    }

    static void addNew(MailStoring storing, Environment environment)
    {
	final String title = Popups.simple(environment.getLuwrain(), "Новая учётная запись", "Введите имя новой учётной записи:", "");
	if (title == null)
	    return;
	if (title.trim().isEmpty())
	{
	    environment.getLuwrain().message("Введённое имя новой учётной записи не может быть пустым", Luwrain.MESSAGE_ERROR);//FIXME:
	    return;
	}
	final MailAccount account = new MailAccount();
	account.title = title;
	try {
	    storing.saveAccount(account);
	}
	catch(Exception e)
	{
	    e.printStackTrace();
	    environment.getLuwrain().message("Во время добавления новой учётной записи произошла непредвиденная ошибка", Luwrain.MESSAGE_ERROR);
	    return;
	}
	environment.refreshSectionsTree();
    }
}
