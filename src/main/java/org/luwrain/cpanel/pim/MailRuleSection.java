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

class MailRuleSection extends EmptySection
{
    private MailStoring storing;
    private StoredMailRule rule;
    private MailRuleArea area;

    MailRuleSection(MailStoring storing, StoredMailRule rule)
    {
	this.storing = storing;
	this.rule = rule;
	NullCheck.notNull(storing, "storing");
	NullCheck.notNull(rule, "rule");
    }

    @Override public SectionArea getSectionArea(Environment environment)
    {
	try {
	    if (area == null)
		area = new MailRuleArea(environment, storing, rule);
	}
	catch (Exception e)
	{
	    environment.getLuwrain().message("Невозможно получить параметры правила фильтрации", Luwrain.MESSAGE_ERROR);//FIXME:
	    e.printStackTrace();
	    return null;
	}
	return area;
    }

    @Override public boolean canCloseSection(Environment environment)
    {
	if (area == null)
	    return true;
	try {
	    area.save();
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	    environment.getLuwrain().message("Во время сохранения введённых параметров произошла непредвиденная ошибка", Luwrain.MESSAGE_ERROR);//FIXME:
	}
	area = null;
	return true;
    }

    @Override public boolean onTreeInsert(Environment environment)
    {
	if (!MailRulesSection.addNew(storing))
	{
	    environment.getLuwrain().message("При добавлении нового правила произошла непредвиденная ошибка", Luwrain.MESSAGE_ERROR);//FIXME:
	    return true;
	}
	environment.refreshSectionsTree();
	return true;
    }

    @Override public boolean onTreeDelete(Environment environment)
    {
	final YesNoPopup popup = new YesNoPopup(environment.getLuwrain(),
						"Удаление правила фильтрации", "Вы действительно хотите удалить выделенное правило фильтрации?",//FIXME:
						false, 0);
	environment.getLuwrain().popup(popup);
	if (popup.closing.cancelled() || !popup.result())
	    return true;
	try {
	    storing.deleteRule(rule);
	}
	catch(Exception e)
	{
	    e.printStackTrace();
	    environment.getLuwrain().message("При удалении правила фильтрации произошла непредвиденная ошибка", Luwrain.MESSAGE_ERROR);
	    return true;
	}
	rule = null;
	area = null;
	environment.refreshSectionsTree();
	return true;
    }

    @Override public boolean isSectionEnabled()
    {
	return rule != null;
    }

    @Override public boolean equals(Object o)
    {
	if (rule == null ||
	    o == null || !(o instanceof MailRuleSection))
	    return false;
	final MailRuleSection sect = (MailRuleSection)o;
	return rule.equals(sect.rule);
    }

    @Override public String toString()
    {
	try {
	    return rule.getHeaderRegex();
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
