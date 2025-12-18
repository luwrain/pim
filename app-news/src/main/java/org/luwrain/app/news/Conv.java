// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.news;

import java.net.*;

import org.luwrain.core.*;
import org.luwrain.popups.*;

final class Conv
{
    private final Luwrain luwrain;
    private final Strings strings;

    Conv(App app)
    {
	this.luwrain = app.getLuwrain();
	this.strings = app.getStrings();
    }

    boolean confirmGroupDeleting(GroupWrapper wrapper)
    {
	return Popups.confirmDefaultNo(luwrain, strings.groupDeletingPopupName(), strings.groupDeletingPopupText(wrapper.group.getName()));
    }

    String newGroupName()
    {
	final String res = Popups.text(luwrain, strings.groupAddingPopupName(), strings.groupAddingPopupPrefix(), "", (line)->{
		if (line.trim().isEmpty())
		{
		    luwrain.message(strings.groupAddingNameMayNotBeEmpty(), Luwrain.MessageType.ERROR);
		    return false;
		}
		return true;
	    });
	if (res == null)
	    return null;
	return res.trim();
    }
}
