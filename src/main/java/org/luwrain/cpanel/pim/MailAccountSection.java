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

import java.util.*;

import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.controls.*;
import org.luwrain.popups.*;
import org.luwrain.cpanel.*;
import org.luwrain.pim.mail.*;

class MailAccountSection extends EmptySection
{
    private MailStoring storing;
    private StoredMailAccount account;
    private MailAccountArea area;

    MailAccountSection(MailStoring storing, StoredMailAccount account)
    {
	this.storing = storing;
	this.account = account;
	NullCheck.notNull(storing, "storing");
	NullCheck.notNull(account, "account");
    }

    @Override public SectionArea getSectionArea(Environment environment)
    {
	try {
	    if (area == null)
		area = new MailAccountArea(environment, storing, account);
	}
	catch (Exception e)
	{
	    environment.getLuwrain().message("Невозможно получить параметры учётной записи", Luwrain.MESSAGE_ERROR);
	    e.printStackTrace();
	    return null;
	}
	return area;
    }

    @Override public boolean canCloseSection(Environment environment)
    {
	if (account == null || area == null)
	    return true;
	try {
	    if (!area.save())
		return false;
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	    environment.getLuwrain().message("Во время сохранения введённых изменений произошла непредвиденная ошибка", Luwrain.MESSAGE_ERROR);
	}
	area = null;
	return true;
    }

    @Override public boolean onTreeInsert(Environment environment)
    {
	MailAccountsSection.addNew(storing, environment);
	return true;
    }

    @Override public boolean onTreeDelete(Environment environment)
    {
	String title;
	try {
	    title = account.getTitle();
	}
	catch(Exception e)
	{
	    e.printStackTrace();
	    title = "";
	}
	final YesNoPopup popup = new YesNoPopup(environment.getLuwrain(),
						"Удаление учётной записи", "Вы действительно хотите удалить учётную запись" + (!title.isEmpty()?" \"" + title + "\"?":"?"),//FIXME:
						false, 0);
	environment.getLuwrain().popup(popup);
	if (popup.closing.cancelled() || !popup.result())
	    return true;
	try {
	    storing.deleteAccount(account);
	}
	catch(Exception e)
	{
	    e.printStackTrace();
	    environment.getLuwrain().message("При удалении учётной записи произошла непредвиденная ошибка", Luwrain.MESSAGE_ERROR);
	    return true;
	}
	System.out.println("deleted");
	account = null;
	area = null;
	environment.refreshSectionsTree();
	return true;
    }

    @Override public boolean isSectionEnabled()
    {
	return account != null;
    }

    @Override public boolean equals(Object o)
    {
	if (account == null ||
	    o == null || !(o instanceof MailAccountSection))
	    return false;
	final MailAccountSection sect = (MailAccountSection)o;
	return account.equals(sect.account);
    }

    @Override public String toString()
    {
	try {
	    return account.getTitle();
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	    return "#Mail storing error!#";
	}
    }

    @Override public Set<Section.Flags> getSectionFlags()
    {
	return EnumSet.of(Flags.HAS_INSERT, Flags.HAS_DELETE);
    }
}
