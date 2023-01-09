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

package org.luwrain.settings.mail.folders;

import java.util.*;

import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.controls.*;
import org.luwrain.popups.Popups;
import org.luwrain.cpanel.*;
import org.luwrain.pim.*;
import org.luwrain.pim.mail.*;
import org.luwrain.settings.mail.*;

class Area extends FormArea implements SectionArea
{
    private final ControlPanel controlPanel;
    private final Luwrain luwrain;
    private final MailStoring storing;
    private final MailFolder folder;
    private final org.luwrain.settings.mail.Strings strings;

    Area(ControlPanel controlPanel, org.luwrain.settings.mail.Strings strings,
	 MailStoring storing, MailFolder folder) throws PimException
    {
	super(new DefaultControlContext(controlPanel.getCoreInterface()), strings.folderFormName(folder.getTitle()));
	NullCheck.notNull(controlPanel, "controlPanel");
	NullCheck.notNull(strings, "strings");
	NullCheck.notNull(storing, "storing");
	NullCheck.notNull(folder, "folder");
	this.storing = storing;
	this.strings = strings;
	this.folder = folder;
	this.controlPanel = controlPanel;
	this.luwrain = controlPanel.getCoreInterface();
	fillForm();
    }

    private void fillForm() throws PimException
    {
	addEdit("title", "Имя почтовой группы:", folder.getTitle());
    }

    @Override public boolean saveSectionData()
    {
	try {
	    final int orderIndex;
	    try {
		String value = getEnteredText("order-index");
		if (value.trim().isEmpty())
		    value = "0";
		orderIndex = Integer.parseInt(value);
	    }
	    catch(NumberFormatException e)
	    {
		luwrain.message(strings.mailFolderFormBadOrderIndex(getEnteredText("order-index")), Luwrain.MessageType.ERROR);
		return false;
	    }
	    folder.setTitle(getEnteredText("title"));
	    return true;
	}
	catch(Exception e)
	{
	    luwrain.crash(e);
	    return false;
	}
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
}
