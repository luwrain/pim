// Copyright 2015 Roman Volovodov <gr.rPman@gmail.com>

package org.luwrain.pim.workers;

import org.luwrain.core.*;

public class DefaultControl implements org.luwrain.pim.fetching.Control
{
    protected final Luwrain luwrain;

    public DefaultControl(Luwrain luwrain)
    {
	NullCheck.notNull(luwrain, "luwrain");
	this.luwrain = luwrain;
    }

    @Override public Luwrain luwrain()
    {
	return luwrain;
    }

    @Override public void checkInterrupted() throws InterruptedException
    {
	if (Thread.currentThread().isInterrupted())
	    throw new InterruptedException();
    }

    @Override public void message(String text)
    {
	//Doing nothing, because workers aren't supposed to be launched in foreground
    }
}
