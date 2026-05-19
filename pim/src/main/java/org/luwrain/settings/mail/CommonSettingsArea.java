// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

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
	if (controlPanel.onInputEvent(this, event))
	    return true;
	return super.onInputEvent(event);
    }

    @Override public boolean onSystemEvent(SystemEvent event)
    {
	if (controlPanel.onSystemEvent(this, event))
	    return true;
	return super.onSystemEvent(event);
    }
}
