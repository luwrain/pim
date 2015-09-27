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
import org.luwrain.pim.mail.*;

public class MailAccountsSection extends EmptySection
{
    private MailAccountSection[] childSections = null;
    private MailStoring storing;

    MailAccountsSection(MailStoring storing)
    {
	this.storing = storing;
	NullCheck.notNull(storing, "storing");
    }

    @Override public int getDesiredRoot()
    {
	return BasicSections.NONE;
    }

    @Override public Section[] getChildSections()
    {
	constructChildSections();
	return childSections;
    }

    String getSectionName()
    {
	return "Учётные записи";
    }

    @Override public Area getSectionArea(Environment environment)
    {
	return null;
    }

    @Override public boolean canCloseSection(Environment environment)
    {
	return true;
    }

    @Override public boolean onTreeInsert(Environment environment)
    {
	return false;
    }

    @Override public boolean onTreeDelete(Environment environment)
    {
	return false;
    }

    @Override public boolean isSectionEnabled()
    {
	return true;
    }

    @Override public String toString()
    {
	return getSectionName();
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
	Popups.simple(environment.getLuwrain(), "Новая учётная запись", "Введите имя новой учётной записи:", "");
    }
}
