/*
   Copyright 2012-2022 Michael Pozhidaev <msp@luwrain.org>
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

package org.luwrain.pim.mail;

import java.io.*;

import org.luwrain.core.*;
import org.luwrain.pim.*;
import org.luwrain.pim.storage.*;

public final class Factory implements AutoCloseable
{
static final String
    LOG_COMPONENT = "mail";

    static private final String
	DATA_DIR = "luwrain.pim.mail",
		MESSAGES_DIR = "luwrain.pim.mail.messages",
	DATA_FILE = "mail.nitrite";

    private final Luwrain luwrain;
    private final ExecQueues execQueues = new ExecQueues();
    private final NitriteStorage<org.luwrain.pim.mail.nitrite.Message> storage;

    public Factory(Luwrain luwrain)
    {
	NullCheck.notNull(luwrain, "luwrain");
	this.luwrain = luwrain;
	this.execQueues.start();
	this.storage = new NitriteStorage<>(new File(luwrain.getAppDataDir(DATA_DIR).toFile(), DATA_FILE), org.luwrain.pim.mail.nitrite.Message.class);
    }

    public MailStoring newMailStoring(boolean highPriority)
    {
	return new org.luwrain.pim.mail.nitrite.Storing(luwrain.getRegistry(), storage, execQueues, highPriority, luwrain.getAppDataDir(MESSAGES_DIR).toFile());
    }

    @Override public void close()
    {
	storage.close();
	execQueues.cancel();
    }
}
