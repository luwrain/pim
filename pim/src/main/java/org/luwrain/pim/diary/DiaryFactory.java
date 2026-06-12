// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.pim.diary;

import java.io.*;
import java.nio.file.*;
import org.apache.logging.log4j.*;
import org.h2.mvstore.*;

import org.luwrain.core.*;
import org.luwrain.pim.*;
import org.luwrain.pim.diary.persistence.*;

import static java.util.Objects.*;
import static java.nio.file.Files.*;

public final class DiaryFactory implements AutoCloseable
{
    static private final Logger log = LogManager.getLogger();

    final Path path;
    final ExecQueues queues = new ExecQueues();
    private final MVStore store;
    private final MVMap<Long, org.luwrain.pim.diary.persistence.Event> eventsMap;
    private final MVMap<String, Long> keysMap;

    public DiaryFactory(Path path) throws IOException
    {
	this.path = requireNonNull(path, "path can't be null");
	createDirectories(path);
	final String dbFile = path.resolve("diary.mvdb").toString();
	log.trace("Opening the diary database in " + dbFile);
	this.store = MVStore.open(dbFile);
	this.eventsMap = store.openMap("events");
	this.keysMap = store.openMap("keys");
    }

    public Object newInstance()
    {
	return new DiaryPersistence(queues, eventsMap, keysMap);
    }

    @Override public void close()
    {
	queues.close();
	store.close();
	log.trace("The diary database closed");
    }
}
