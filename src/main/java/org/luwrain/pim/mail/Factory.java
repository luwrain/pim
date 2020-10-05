/*
   Copyright 2012-2020 Michael Pozhidaev <msp@luwrain.org>
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

import org.dizitart.no2.*;

import org.luwrain.core.*;
import org.luwrain.pim.*;

public final class Factory
{
    static private final String LOG_COMPONENT = "pim-mail";

    private final Luwrain luwrain;
    private final Registry registry;
    private final Settings.Storing sett;
    private final ExecQueues execQueues = new ExecQueues();
    private final File file;
    private final File messagesDir;
    private Nitrite db = null;

    public Factory(Luwrain luwrain)
    {
	NullCheck.notNull(luwrain, "luwrain");
	this.luwrain = luwrain;
	this.registry = luwrain.getRegistry();
	this.sett = Settings.createStoring(registry);
	this.execQueues.start();
	this.file = new File(luwrain.getAppDataDir("luwrain.pim.mail").toFile(), "mail.nitrite");
		this.messagesDir = luwrain.getAppDataDir("luwrain.pim.mail.messages").toFile();
    }

    public MailStoring newMailStoring(boolean highPriority)
    {
	if (db != null)
	    return new org.luwrain.pim.mail.nitrite.Storing(registry, db, execQueues, highPriority, messagesDir);
			this.db = Nitrite.builder()
        .compressed()
        .filePath(file)
        .openOrCreate("luwrain", "passwd");
			return new org.luwrain.pim.mail.nitrite.Storing(registry, this.db, execQueues, highPriority, messagesDir);
    }

    public void close()
    {
	execQueues.cancel();
	if (db != null)
	    db.close();
}
}
