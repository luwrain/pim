// Copyright 2015 Roman Volovodov <gr.rPman@gmail.com>

package org.luwrain.pim.workers;

import org.luwrain.core.*;
import org.luwrain.pim.fetching.*;

public class News implements Worker
{
    static public String NAME = "luwrain.pim.fetch.news";

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
	try {
	    	final org.luwrain.pim.fetching.News newsFetching = new org.luwrain.pim.fetching.News(control, strings);
	    newsFetching.fetch();
	}
	catch(InterruptedException e)
	{
	    Log.debug(LOG_COMPONENT, "the worker \'" + NAME + "\' has been interrupted");
	    return;
	}
		catch(Throwable e)
	{
	    Log.error(LOG_COMPONENT, "the worker \'" + NAME + "\' failed:" + e.getClass().getName() + ":" + e.getMessage());
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
