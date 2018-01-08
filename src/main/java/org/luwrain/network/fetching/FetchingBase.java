
package org.luwrain.network.fetching;

import org.luwrain.core.*;

class FetchingBase
{
    protected final WorkerControl control;
    protected final Luwrain luwrain;
    protected final Registry registry;
    protected final Strings strings;

    FetchingBase(WorkerControl control, Strings strings)
    {
	NullCheck.notNull(control, "control");
	NullCheck.notNull(strings, "strings");
	this.control = control;
	this.strings = strings;
	this.luwrain = control.luwrain();
	this.registry = luwrain.getRegistry();
    }

    protected void message(String text)
    {
	NullCheck.notNull(text, "text");
	control.message(text);
    }

    void checkInterrupted() throws InterruptedException
    {
	control.checkInterrupted();
    }
}
