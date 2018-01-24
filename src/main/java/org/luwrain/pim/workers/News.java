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

public class News implements Worker
{
    static public String NAME = "luwrain.pim.workers.news";

    static protected final String LOG_COMPONENT = "pim-workers";

    protected final Luwrain luwrain;
    protected final org.luwrain.pim.fetching.Control control;

    public News(Luwrain luwrain)
    {
	NullCheck.notNull(luwrain, "luwrain");
	this.luwrain = luwrain;
	this.control = new DefaultControl(luwrain);
    }

        public News(org.luwrain.pim.fetching.Control control)
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
	final org.luwrain.pim.fetching.News newsFetching = new org.luwrain.pim.fetching.News(control, strings);
	try {
	    newsFetching.fetch();
	}
	catch(InterruptedException e)
	{
	    Log.debug(LOG_COMPONENT, "the worker \'" + NAME + "\' has been interrupted");
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
