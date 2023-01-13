/*
   Copyright 2012-2023 Michael Pozhidaev <msp@luwrain.org>

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

import java.util.*;

import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.controls.*;
import org.luwrain.popups.Popups;
import org.luwrain.cpanel.*;
import org.luwrain.pim.*;
import org.luwrain.pim.mail.*;
import org.luwrain.settings.mail.*;

 final class CommonSettingsArea extends FormArea implements SectionArea
{
final ControlPanel controlPanel;
final Luwrain luwrain;
    final org.luwrain.settings.mail.Strings strings;

    CommonSettingsArea(ControlPanel controlPanel, org.luwrain.settings.mail.Strings strings)
    {
	super(new DefaultControlContext(controlPanel.getCoreInterface()), "Общие настройки");
		this.controlPanel = controlPanel;
	this.strings = strings;
	this.luwrain = controlPanel.getCoreInterface();
	fillForm();
    }

    private void fillForm()
    {
    }

    @Override public boolean saveSectionData()
    {
	return false;
    }

    @Override public boolean onInputEvent(InputEvent event)
    {
	if (controlPanel.onInputEvent(event))
	    return true;
	return super.onInputEvent(event);
    }

    @Override public boolean onSystemEvent(SystemEvent event)
    {
	if (controlPanel.onSystemEvent(event))
	    return true;
	return super.onSystemEvent(event);
    }
}
