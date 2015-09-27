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

public class MailSection extends EmptySection
{
    private MailStoring storing = null;
    private MailAccountsSection accounts;
    private MailRulesSection rules;

    public MailSection(Factory factory)
    {
	NullCheck.notNull(factory, "factory");
	storing = factory.createMailStoring();
    }

    @Override public int getDesiredRoot()
    {
	return BasicSections.APPLICATIONS;
    }

    @Override public Section[] getChildSections()
    {
	if (accounts == null)
	    accounts = new MailAccountsSection(storing);
	if (rules == null)
	    rules = new MailRulesSection(storing);
	return new Section[]{
	    accounts,
	    rules,
	};
    }

    @Override public boolean isSectionEnabled()
    {
	return storing != null;
    }

    @Override public String toString()
    {
	return "Почта";
    }

    @Override public void refreshChildSubsections()
    {
	if (accounts != null)
	    accounts.refreshChildSubsections();
	if (rules != null)
	    rules.refreshChildSubsections();
    }
}
