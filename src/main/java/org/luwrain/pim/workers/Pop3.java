
package org.luwrain.pim.workers;

import org.luwrain.core.*;
import org.luwrain.pim.mail2.*;
import org.luwrain.pim.mail2.persistence.model.*;
import org.luwrain.pim.mail2.persistence.dao.*;

import static org.luwrain.core.NullCheck.*;
import static org.luwrain.pim.mail2.persistence.MailPersistence.*;

public class Pop3 implements Worker
{
    static public String
	NAME = "luwrain.pim.fetch.pop3";

    static protected final String
	LOG_COMPONENT = "pim";

    protected final Luwrain luwrain;

        public Pop3(Luwrain luwrain)
    {
	notNull(luwrain, "luwrain");
	this.luwrain = luwrain;
    }

    @Override public void run()
    {
	try {
	    final var accountDAO = getAccountDAO();
	    final var accounts = accountDAO.getAll();
	    log("fetching POP3 mail from " + accounts.size() + " accounts");
	    for(final var a: accounts)
	    {
		log("Fetching from: " + a.getName());
		final var decoder = new MessageDecoder();

		final var pop3 = new org.luwrain.pim.mail2.proto.Pop3(a);
		pop3.getMessages((message, extData) -> {
			//			log("fetching message " + extData.msgNum + " of " + extData.totalMsgCount + " from " + a.getName());
			decoder.onMessage(message);
			log(message.getMetadata().getSubject());
		    });
	    }
	}
	/*
	catch(InterruptedException e)
	{
	    Log.debug(LOG_COMPONENT, "the worker \'" + NAME + "\' has been interrupted");
	    return;
	}
	*/
	catch(Throwable e)
	{
	    Log.error(LOG_COMPONENT, "the worker " + NAME + " failed: " + e.getClass().getSimpleName() + ": " + e.getMessage());
	    e.printStackTrace();
	}
    }

    protected void log(String message)
    {
	Log.debug(LOG_COMPONENT, message);
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
