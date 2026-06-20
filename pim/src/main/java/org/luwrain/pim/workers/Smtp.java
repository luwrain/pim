// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>
// Copyright 2015 Roman Volovodov <gr.rPman@gmail.com>

package org.luwrain.pim.workers;

import org.apache.logging.log4j.*;

import org.luwrain.core.*;
import org.luwrain.pim.*;
import org.luwrain.pim.mail.proto.*;
import org.luwrain.pim.mail.persistence.*;

import static java.util.Objects.*;

/**
 * Worker that sends outgoing mail via SMTP.
 *
 * Iterates over all SMTP accounts in the persistence layer,
 * locates messages that are pending to be sent,
 * and delivers them using the SMTP protocol implementation from
 * {@link org.luwrain.pim.mail.proto.Smtp}.
 */
public class Smtp implements Worker
{
    static private final Logger log = LogManager.getLogger();

    static public final String
	NAME = "luwrain.pim.fetch.smtp";

    private final Luwrain luwrain;

    public Smtp(Luwrain luwrain)
    {
	this.luwrain = requireNonNull(luwrain, "luwrain can't be null");
    }

    @Override public void run()
    {
	try {
	    final var persist = luwrain.createInstance(MailPersistence.class);
	    if (persist == null)
	    {
		log.error("No MailPersistence available, unable to send messages");
		return;
	    }
	    final var accountDAO = persist.getAccountDAO();
	    final var allAccounts = accountDAO.getAll();
	    if (allAccounts.isEmpty())
	    {
		log.debug("No mail accounts configured");
		return;
	    }
	    for (final var account : allAccounts)
	    {
		if (account.getType() != Account.Type.SMTP)
		    continue;
		if (!account.isEnabled())
		{
		    log.debug("SMTP account \"" + account.getName() + "\" is disabled, skipping");
		    continue;
		}
		log.debug("Processing SMTP account: " + account.getName());
		try {
		    final var smtp = new org.luwrain.pim.mail.proto.Smtp(account, persist, luwrain);
		    final var result = smtp.send();
		    log.debug("SMTP account \"" + account.getName() + "\": sent " + result.sent + " of " + result.total + " messages");
		    if (!result.errors.isEmpty())
		    {
			for (final var entry : result.errors.entrySet())
			    log.error("Failed to send message " + entry.getKey() + " via " + account.getName() + ": " + entry.getValue().getMessage());
		    }
		}
		catch (PimException e)
		{
		    log.error("Unable to send messages via SMTP account " + account.getHost() + ":" + account.getPort() + ": " + e.getMessage());
		}
		catch (InterruptedException e)
		{
		    log.debug("SMTP worker interrupted");
		    Thread.currentThread().interrupt();
		    return;
		}
	    }
	}
	catch (Throwable e)
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
