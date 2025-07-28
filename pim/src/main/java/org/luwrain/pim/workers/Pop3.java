
package org.luwrain.pim.workers;

import org.apache.logging.log4j.*;

import org.luwrain.core.*;
import org.luwrain.pim.mail.*;
import org.luwrain.pim.mail.persistence.model.*;
import org.luwrain.pim.mail.persistence.dao.*;

import static org.luwrain.core.NullCheck.*;
import static org.luwrain.pim.mail.persistence.MailPersistence.*;
import static org.luwrain.pim.Hooks.*;

public class Pop3 implements Worker
{
        static protected final Logger log = LogManager.getLogger();

    static public String
	NAME = "luwrain.pim.fetch.pop3";

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
	    log.debug("fetching POP3 mail from " + accounts.size() + " accounts");
	    for(final var a: accounts)
	    {
		//		log.debug("Fetching from: " + a.getName());
		final var decoder = new MessageDecoder();
		final var pop3 = new org.luwrain.pim.mail.proto.Pop3(a);
		pop3.getMessages((message, extData) -> {
			//			log("fetching message " + extData.msgNum + " of " + extData.totalMsgCount + " from " + a.getName());
			decoder.onMessage(message);
			mailIncoming(luwrain, message);
			//			log(message.getMetadata().getSubject());
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
	    log.error(NAME, e);
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
