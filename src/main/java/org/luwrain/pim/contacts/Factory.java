/*
   Copyright 2012-2021 Michael Pozhidaev <msp@luwrain.org>
   Copyright 2015 Roman Volovodov <gr.rPman@gmail.com>

   This file is part of LUWRAIN.

   LUWRAIN is free software; you can redistribute it and/or
   modify it under the terms of the GNU General Public
   License as published by the Free Software Foundation; either
   version 3 of the License, or (at your option) any later version.

   LUWRAIN is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
   General Public License for more details.
*/

package org.luwrain.pim.contacts;

import java.io.*;

import org.luwrain.core.*;
import org.luwrain.pim.*;

public final class Factory
{
    static private final String LOG_COMPONENT = "pim-contacts";

    private final Luwrain luwrain;
    private final ExecQueues execQueues = new ExecQueues();

    public Factory(Luwrain luwrain)
    {
	NullCheck.notNull(luwrain, "luwrain");
	this.luwrain = luwrain;
    }

    public ContactsStoring newContactsStoring(boolean highPriority)
    {
	try {
	return new org.luwrain.pim.contacts.json.Storing(new File("/tmp"));
	}
	catch(IOException e)
	{
	    throw new RuntimeException(e);
	}
    }

    public void close()
    {
	execQueues.cancel();
    }
}
