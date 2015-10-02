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

class MailRulesSection extends EmptySection
{
        private MailRuleSection[] childSections = null;
    private MailStoring storing;

    MailRulesSection(MailStoring storing)
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
	if (!addNew(storing))
	{
	    environment.getLuwrain().message("При добавлении нового правила произошла непредвиденная ошибка", Luwrain.MESSAGE_ERROR);//FIXME:
	    return true;
	}
	environment.refreshSectionsTree();
	return true;
    }

    @Override public String toString()
    {
	return "Правила сортировки";
    }

    @Override public void refreshChildSubsections()
    {
	if (childSections == null)
	    return;
	childSections = null;
	constructChildSections();
    }

    private void constructChildSections()
    {
	if (childSections != null)
	    return;
	try {
	    final StoredMailRule[] rules = storing.getRules();
	    if (rules == null || rules.length < 1)
	    {
		childSections = new MailRuleSection[0];
		return;
	    }
	    childSections = new MailRuleSection[rules.length];
	    for(int i = 0;i < rules.length;++i)
		childSections[i] = new MailRuleSection(storing, rules[i]);
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	    childSections = new MailRuleSection[0];
	}
    }

    static boolean addNew(MailStoring storing)
    {
	final MailRule rule = new MailRule();
	rule.action = MailRule.ACTION_MOVE_TO_FOLDER;
	rule.headerRegex = "^From:.*";
	rule.destFolderUniRef = "";
	try {
	    storing.saveRule(rule);
	    return true;
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	    return false;
	}
    }

    @Override public int getSectionFlags()
    {
	return FLAG_HAS_INSERT;
    }
}
