// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.pim.contacts;

import java.io.*;

import org.luwrain.core.*;
import org.luwrain.pim.*;

public final class Factory
{
    static public final String LOG_COMPONENT = "pim-contacts";

    private final Luwrain luwrain;
    //    private final ExecQueues execQueues = new ExecQueues();

    public Factory(Luwrain luwrain)
    {
	NullCheck.notNull(luwrain, "luwrain");
	this.luwrain = luwrain;
    }

    public ContactsStoring newContactsStoring(boolean highPriority)
    {
	    return new org.luwrain.pim.contacts.json.Storing(new File(luwrain.getAppDataDir("luwrain.pim.contacts").toFile(), "contacts.json"));
    }

    public void close()
    {
	//	execQueues.cancel();
    }
}
