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
import org.luwrain.core.events.*;
import org.luwrain.controls.*;
import org.luwrain.popups.*;
import org.luwrain.cpanel.*;
import org.luwrain.pim.mail.*;

class MailRuleSection extends EmptySection
{
    static private class Area extends FormArea
    {
	private Environment environment;
	private MailStoring storing;
	private StoredMailRule rule;

	Area(Environment environment,
	     MailStoring storing, 
	     StoredMailRule rule) throws Exception
	{
	    super(new DefaultControlEnvironment(environment.getLuwrain()));
	    this.storing = storing;
	    this.rule = rule;
	    this.environment = environment;
	    NullCheck.notNull(storing, "storing");
	    NullCheck.notNull(rule, "rule");
	    NullCheck.notNull(environment, "environment");
	    addEdit("header-regex", "Регулярное выражение для заголовка:", rule.getHeaderRegex(), null, true);
	}

	@Override public boolean onKeyboardEvent(KeyboardEvent event)
	{
	    NullCheck.notNull(event, "event");
	    if (event.isCommand() && !event.isModified())
		switch(event.getCommand())
		{
		case KeyboardEvent.TAB:
		    environment.gotoSectionsTree();
		}
	    return super.onKeyboardEvent(event);
	}

	@Override public boolean onEnvironmentEvent(EnvironmentEvent event)
	{
	    NullCheck.notNull(event, "event");
	    switch(event.getCode())
	    {
	    case EnvironmentEvent.CLOSE:
		environment.close();
		return true;
	    default:
		return super.onEnvironmentEvent(event);
	    }
	}

	@Override public String getAreaName()
	{
	    return "Настройка правила фильтрации";//FIXME:
    }
    }

    private MailStoring storing;
    private StoredMailRule rule;
    private Area area = null;

    MailRuleSection(MailStoring storing, StoredMailRule rule)
    {
	this.storing = storing;
	this.rule = rule;
	NullCheck.notNull(storing, "storing");
	NullCheck.notNull(rule, "rule");
    }

    @Override public Area getSectionArea(Environment environment)
    {
	try {
	    if (area == null)
		area = new Area(environment, storing, rule);
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
	return true;
    }

    @Override public boolean onTreeInsert(Environment environment)
    {
	return truefalse
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
	environment.refreshSectionsTree();
	return true;
    }

    @Override public boolean isSectionEnabled()
    {
	System.out.println("enabled");
	return rule != null;
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
}
