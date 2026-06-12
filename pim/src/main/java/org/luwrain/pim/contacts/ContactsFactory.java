// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.pim.contacts;

import java.io.*;
import java.nio.file.*;
import org.apache.logging.log4j.*;
import org.h2.mvstore.*;

import org.luwrain.core.*;
import org.luwrain.pim.*;
import org.luwrain.pim.contacts.persistence.*;

import static java.util.Objects.*;
import static java.nio.file.Files.*;

public final class ContactsFactory implements AutoCloseable
{
    static private final Logger log = LogManager.getLogger();

    final Path path;
    final ExecQueues queues = new ExecQueues();
    private final MVStore store;
    private final MVMap<Long, Contact> contactsMap;
    private final MVMap<String, Long> keysMap;

    public ContactsFactory(Path path) throws IOException
    {
	this.path = requireNonNull(path, "path can't be null");
	createDirectories(path);
	final String dbFile = path.resolve("contacts.mvdb").toString();
	log.trace("Opening the contacts database in " + dbFile);
	this.store = MVStore.open(dbFile);
	this.contactsMap = store.openMap("contacts");
	this.keysMap = store.openMap("keys");
    }

    public Object newInstance()
    {
	return new ContactsPersistence(queues, contactsMap, keysMap);
    }

    @Override public void close()
    {
	queues.close();
	store.close();
	log.trace("The contacts database closed");
    }
}
