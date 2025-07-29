/*
   Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>
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

import java.nio.file.*;
import org.apache.logging.log4j.*;
import org.h2.mvstore.*;


import org.luwrain.core.*;
import org.luwrain.pim.storage.*;

import static java.util.Objects.*;

public final class MailFactory implements ObjFactory
{
    static private final Logger log = LogManager.getLogger();

    final Path path;
    final ExecQueues queues = new ExecQueues();
    private final MVStore store;
    private final MVMap<String, String> messagesMap;

    public MailFactory(Path path)
    {
	this.path = requireNonNull(path, "path can't be null");
	final String dbFile = path.resolve("mail.mvdb").toString();
	log.trace("Opening the mail database in " + dbFile);
	this.store = MVStore.open(dbFile);
	this.messagesMap = store.openMap("messages");
    }

    @Override public String getExtObjName()
    {
	return MailPersistence.class.getName();
    }

    @Override public Object newObject(String name)
    {
	return new MailPersistence(queues);
    }
}
