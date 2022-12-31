
//LWR_API 1.0

package org.luwrain.pim.workers;

import org.luwrain.core.*;
import org.luwrain.pim.fetching.*;

public class Smtp implements Worker
{
    static protected final String LOG_COMPONENT = "pim-workers";
    static public String NAME = "luwrain.pim.fetch.smtp";

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
	    	final org.luwrain.pim.mail.protocols.Smtp smtpFetching = new org.luwrain.pim.mail.protocols.Smtp(control, strings);
	    final org.luwrain.pim.mail.protocols.Smtp.Result res = smtpFetching.send();
	    if (res.total > 0)
	    {
		if (res.total == res.sent)
	    luwrain.message("Отправлено сообщений: " + res.sent, Luwrain.MessageType.DONE); else
		    luwrain.message("Произошла ошибка при отправке сообщений", Luwrain.MessageType.ERROR);
	    }
	}
	catch(InterruptedException e)
	{
	    Log.debug(LOG_COMPONENT, "the worker \'" + NAME + "\' has been interrupted");
	    return;
	}
	catch(Throwable e)
	{
	    Log.error(LOG_COMPONENT, "the worker " + NAME + " failed:" + e.getClass().getName() + ":" + e.getMessage());
	    e.printStackTrace();
	    luwrain.message("Произошла ошибка при отправке сообщений", Luwrain.MessageType.ERROR);
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
