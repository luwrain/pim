// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.pim.workers;

import org.apache.logging.log4j.*;

import org.luwrain.core.*;
import org.luwrain.pim.fetching.*;

import static java.util.Objects.*;

public class News implements Worker
{
    static public String NAME = "luwrain.pim.news.fetch";
    static private final Logger log = LogManager.getLogger();

    protected final Luwrain luwrain;
    protected final org.luwrain.pim.fetching.Control control;

    public News(Luwrain luwrain)
    {
	this.luwrain = requireNonNull(luwrain, "luwrain can't be null");
	this.control = new DefaultControl(luwrain);
    }

        public News(org.luwrain.pim.fetching.Control control)
    {
	this.control = requireNonNull(control, "control can't be null");
	this.luwrain = control.luwrain();
    }

    @Override public void run()
    {
	log.trace("Starting fetching news ( the {} worker)", NAME);
	final var strings = luwrain.i18n().getStrings(org.luwrain.pim.fetching.Strings.class);
	if (strings == null)
	{
	    log.error("Unable to launch the {} worker: there is no strings object {}", NAME, org.luwrain.pim.fetching.Strings.class.getName());
	    return;
	}
	try {
	    	final org.luwrain.pim.fetching.News newsFetching = new org.luwrain.pim.fetching.News(control, strings);
	    newsFetching.fetch();
	}
	catch(InterruptedException e)
	{
	    log.debug("The worker {} has been interrupted", NAME);
	    Thread.currentThread().interrupt();
	    return;
	}
		catch(Throwable e)
	{
	    log.error("The worker {} failed", NAME, e);
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
	return 60;
    }
}
