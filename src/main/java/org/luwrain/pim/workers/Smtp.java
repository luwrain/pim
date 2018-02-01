/*
   Copyright 2012-2018 Michael Pozhidaev <michael.pozhidaev@gmail.com>

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

//LWR_API 1.0

package org.luwrain.pim.workers;

import org.luwrain.core.*;
import org.luwrain.pim.fetching.*;

public class Smtp implements Worker
{
    static protected final String LOG_COMPONENT = "pim-workers";
    static public String NAME = "luwrain.pim.workers.smtp";

    protected final Luwrain luwrain;
    protected final org.luwrain.pim.fetching.Control control;

    public Smtp(Luwrain luwrain)
    {
	NullCheck.notNull(luwrain, "luwrain");
	this.luwrain = luwrain;
	this.control = new DefaultControl(luwrain);
    }

        public Smtp(org.luwrain.pim.fetching.Control control)
    {
	NullCheck.notNull(control, "control");
	this.control = control;
	this.luwrain = control.luwrain();
    }

    @Override public void run()
    {
	final org.luwrain.pim.fetching.Strings strings = (org.luwrain.pim.fetching.Strings)luwrain.i18n().getStrings(org.luwrain.pim.fetching.Strings.NAME);
	if (strings == null)
	{
	    Log.error(LOG_COMPONENT, "unable to launch the worker \'" + NAME + "\' since there is no strings object with the name \'" + org.luwrain.pim.fetching.Strings.NAME + "\'");
	    return;
	}
	try {
	    	final org.luwrain.pim.fetching.Smtp smtpFetching = new org.luwrain.pim.fetching.Smtp(control, strings);
	    smtpFetching.fetch();
	    luwrain.message("Прикольно", Luwrain.MessageType.DONE);
	}
	catch(InterruptedException e)
	{
	    Log.debug(LOG_COMPONENT, "the worker \'" + NAME + "\' has been interrupted");
	    return;
	}
	catch(Throwable e)
	{
	    Log.error(LOG_COMPONENT, "the worker \'" + NAME + "\' failed:" + e.getClass().getName() + ":" + e.getMessage());
luwrain.message("Неприкольно " + e.getClass().getName() + ":" + e.getMessage(), Luwrain.MessageType.ERROR);
	    return;
	}
    }

    @Override public String getExtObjName()
    {
	return NAME;
    }

    @Override public int getFirstLaunchDelay()
    {
	return 0;
    }

    @Override public int getLaunchPeriod()
    {
	return 0;
    }
}
