// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.pim.tasks;

import java.io.*;
import java.nio.file.*;
import org.apache.logging.log4j.*;
import org.h2.mvstore.*;

import org.luwrain.core.*;
import org.luwrain.pim.*;
import org.luwrain.pim.tasks.persist.*;

import static java.util.Objects.*;
import static java.nio.file.Files.*;

public final class TasksFactory implements AutoCloseable
{
    static private final Logger log = LogManager.getLogger();

    final Path path;
    final ExecQueues queues = new ExecQueues();
    private final MVStore store;
    private final MVMap<Integer, TaskList> listsMap;
    private final MVMap<Long, Task> tasksMap;
    private final MVMap<String, Long> keysMap;

    public TasksFactory(Path path) throws IOException
    {
	this.path = requireNonNull(path, "path can't be null");
	createDirectories(path);
	final String dbFile = path.resolve("tasks.mvdb").toString();
	log.trace("Opening the tasks database in " + dbFile);
	this.store = MVStore.open(dbFile);
	this.listsMap = store.openMap("lists");
	this.tasksMap = store.openMap("tasks");
	this.keysMap = store.openMap("keys");
    }

    public Object newInstance()
    {
	return new TasksPersistence(queues, listsMap, tasksMap, keysMap);
    }

    @Override public void close()
    {
	queues.close();
	store.close();
	log.trace("The tasks database closed");
    }
}
