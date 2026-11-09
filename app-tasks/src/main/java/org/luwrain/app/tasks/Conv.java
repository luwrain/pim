// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.tasks;

import org.luwrain.core.*;
import org.luwrain.popups.*;

import static org.luwrain.popups.Popups.*;

final class Conv
{
    private final Luwrain luwrain;
    private final Strings strings;

    Conv(App app)
    {
	this.luwrain = app.getLuwrain();
	this.strings = app.getStrings();
    }

    String newTaskTitle()
    {
	final var res = textNotEmpty(luwrain,
				     strings.createTaskPopupName(),
				     strings.createTaskPopupPrefix(),
				     "");
	if (res == null)
	    return null;
	return res.trim();
    }

    boolean confirmDeleteTask(String title)
    {
	return confirmDefaultNo(luwrain,
		       strings.deleteTaskPopupName(),
		       strings.deleteTaskPopupText() + " " + title);
    }
}
