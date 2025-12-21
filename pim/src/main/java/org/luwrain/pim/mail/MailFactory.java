// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>
// Copyright 2015 Roman Volovodov <gr.rPman@gmail.com>

package org.luwrain.pim.mail;

import java.io.*;
import java.nio.file.*;
import org.apache.logging.log4j.*;
import org.h2.mvstore.*;

import org.luwrain.core.*;
import org.luwrain.pim.*;
import org.luwrain.pim.mail.persistence.*;
import org.luwrain.pim.mail.persistence.*;

import static java.util.Objects.*;
import static java.nio.file.Files.*;

public final class MailFactory implements AutoCloseable
{
    static private final Logger log = LogManager.getLogger();

    final Path path;
    final ExecQueues queues = new ExecQueues();
    private final MVStore store;
    private final MVMap<Integer, Account> accountsMap;
            private final MVMap<Integer, Folder> foldersMap;
        private final MVMap<Long, MessageMetadata> messagesMap;
    private final MVMap<String, Long> keysMap;

    public MailFactory(Path path) throws IOException
    {
	this.path = requireNonNull(path, "path can't be null");
	createDirectories(path);
	final String dbFile = path.resolve("mail.mvdb").toString();
	log.trace("Opening the mail database in " + dbFile);
	this.store = MVStore.open(dbFile);
	this.messagesMap = store.openMap("messages");
		this.foldersMap = store.openMap("folders");
				this.accountsMap = store.openMap("accounts");
				this.keysMap = store.openMap("keys");
    }

public Object newInstance()
    {
	return new MailPersistence(queues, accountsMap, foldersMap, messagesMap, keysMap);
    }

    @Override public void close()
    {
	queues.close();
	store.close();
    }
}
