/*
   Copyright 2012-2020 Michael Pozhidaev <msp@luwrain.org>

   This file is part of LUWRAIN.

   LUWRAIN is free software; you can redistribute it and/or
   modify it under the terms of the GNU General Public
   License as published by the Free Software Foundation; either
   version 3 of the License, or (at your option) any later version.

   LUWRAIN is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
   General Public License for more details.
*/

package org.luwrain.settings.mail;

import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.controls.*;
import org.luwrain.cpanel.*;
import org.luwrain.pim.mail.*;

class Rule extends FormArea implements SectionArea
{
    private ControlPanel controlPanel;
    private MailStoring storing;
    private StoredMailRule rule;

    Rule(ControlPanel controlPanel,
	 MailStoring storing, 
	 StoredMailRule rule) throws Exception
    {
	super(new DefaultControlContext(controlPanel.getCoreInterface()));
	this.storing = storing;
	this.rule = rule;
	this.controlPanel = controlPanel;
	NullCheck.notNull(storing, "storing");
	NullCheck.notNull(rule, "rule");
	NullCheck.notNull(controlPanel, "controlPanel");
	addEdit("header-regex", "Регулярное выражение для заголовка:", rule.getHeaderRegex(), null, true);
	addUniRef("dest-folder-uniref", "Почтовая группа:", rule.getDestFolderUniRef(), null, true);
    }

    void save() throws Exception
    {
	rule.setHeaderRegex(getEnteredText("header-regex"));
	final UniRefInfo uniRefInfo = getUniRefInfo("dest-folder-uniref");
	if (uniRefInfo != null)
	    rule.setDestFolderUniRef(uniRefInfo.toString()); else
	    rule.setDestFolderUniRef("");
    }

    @Override public boolean onInputEvent(InputEvent event)
    {
	NullCheck.notNull(event, "event");
	if (controlPanel.onInputEvent(event))
	    return true;
	return super.onInputEvent(event);
    }

    @Override public boolean onSystemEvent(SystemEvent event)
    {
	NullCheck.notNull(event, "event");
	if (controlPanel.onSystemEvent(event))
	    return true;
	return super.onSystemEvent(event);
    }

    @Override public boolean saveSectionData()
    {
	return true;
    }
}
