
//LWR_API 1.0

package org.luwrain.pim.workers;

import org.luwrain.core.*;
import org.luwrain.pim.fetching.*;

public class Pop3 implements Worker
{
    static protected final String LOG_COMPONENT = "pim";
    static public String NAME = "luwrain.pim.fetch.pop3";

    protected final Luwrain luwrain;
    protected final org.luwrain.pim.fetching.Control control;

    public Pop3(Luwrain luwrain)
    {
	NullCheck.notNull(luwrain, "luwrain");
	this.luwrain = luwrain;
	this.control = new DefaultControl(luwrain);
    }

        public Pop3(org.luwrain.pim.fetching.Control control)
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
	    	final org.luwrain.pim.mail.protocols.Pop3 pop3Fetching = new org.luwrain.pim.mail.protocols.Pop3(control, strings);
		Log.debug(LOG_COMPONENT, "starting " + NAME);
		pop3Fetching.fetch();
	}
	catch(InterruptedException e)
	{
	    Log.debug(LOG_COMPONENT, "the worker \'" + NAME + "\' has been interrupted");
	    return;
	}
	catch(Throwable e)
	{
	    Log.error(LOG_COMPONENT, "the worker " + NAME + " failed: " + e.getClass().getName() + ":" + e.getMessage());
	    e.printStackTrace();
	    luwrain.message("Произошла ошибка", Luwrain.MessageType.ERROR);
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
