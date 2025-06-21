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

package org.luwrain.pim.news;

import java.io.*;

import org.luwrain.core.*;
import org.luwrain.pim.*;
import org.luwrain.pim.storage.*;

public final class Factory implements AutoCloseable
{
    static private final String
	DATA_DIR = "luwrain.pim.news",
	DATA_FILE = "news.nitrite";

    private final Luwrain luwrain;
    private final ExecQueues execQueues = new ExecQueues();
    private final NitriteStorage<org.luwrain.pim.news.nitrite.Article> storage;

    public Factory(Luwrain luwrain)
    {
	NullCheck.notNull(luwrain, "luwrain");
	this.luwrain = luwrain;
	this.execQueues.start();
	this.storage = new NitriteStorage<>(new File(luwrain.getAppDataDir(DATA_DIR).toFile(), DATA_FILE), org.luwrain.pim.news.nitrite.Article.class);
    }

    public NewsStoring newNewsStoring(boolean highPriority)
    {
	return new org.luwrain.pim.news.nitrite.Storing(luwrain.getRegistry(), storage, execQueues, highPriority);
    }

    @Override public void close()
    {
	storage.close();
	execQueues.cancel();
    }
}
