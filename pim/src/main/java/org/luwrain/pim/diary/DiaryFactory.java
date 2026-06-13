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

/**
 * Factory for accessing the diary database. Manages the lifecycle of
 * an MVStore storage, opening and closing the {@code diary.mvdb} database
 * file in the specified directory. Provides methods to obtain an instance
 * of {@link DiaryPersistence}, through which all work with events
 * ({@link org.luwrain.pim.diary.persistence.Event}) and to-do items
 * ({@link org.luwrain.pim.diary.persistence.Todo}) is performed.
 *
 * <p>Typical usage:</p>
 * <pre>{@code
 * try (var factory = new DiaryFactory(Paths.get("/home/user/luwrain/diary"))) {
 *     var persistence = (DiaryPersistence) factory.newInstance();
 *     var eventDao = persistence.getEventDAO();
 *     var todoDao = persistence.getTodoDAO();
 *     // work with DAOs...
 * }
 * }</pre>
 *
 * @see DiaryPersistence
 * @see org.luwrain.pim.diary.persistence.Event
 * @see org.luwrain.pim.diary.persistence.Todo
 */
public final class DiaryFactory implements AutoCloseable
{
    static private final Logger log = LogManager.getLogger();

    /** Path to the directory containing the diary database file. */
    public final Path path;

    /** Execution queues for asynchronous operations. */
    final ExecQueues queues = new ExecQueues();

    private final MVStore store;
    private final MVMap<Long, org.luwrain.pim.diary.persistence.Event> eventsMap;
    private final MVMap<Long, org.luwrain.pim.diary.persistence.Todo> todosMap;
    private final MVMap<String, Long> keysMap;

    /**
     * Creates a new diary factory. If the {@code path} directory does not
     * exist, it will be created along with all intermediate directories.
     * The database file {@code diary.mvdb} is opened (or created if missing)
     * inside the specified directory.
     *
     * @param path path to the directory where the {@code diary.mvdb} file
     *             is stored (or will be created)
     * @throws IOException if the directory could not be created or the
     *                     database could not be opened
     * @throws NullPointerException if {@code path} is {@code null}
     */
    public DiaryFactory(Path path) throws IOException
    {
	this.path = requireNonNull(path, "path can't be null");
	createDirectories(path);
	final String dbFile = path.resolve("diary.mvdb").toString();
	log.trace("Opening the diary database in " + dbFile);
	this.store = MVStore.open(dbFile);
	this.eventsMap = store.openMap("events");
	this.todosMap = store.openMap("todos");
	this.keysMap = store.openMap("keys");
    }

    /**
     * Creates a new {@link DiaryPersistence} instance bound to the current
     * storage. Each call returns a new object, but all of them operate on
     * the same database file and execution queues.
     *
     * @return a new {@link DiaryPersistence} instance
     */
    public Object newInstance()
    {
	return new DiaryPersistence(queues, eventsMap, todosMap, keysMap);
    }

    /**
     * Closes the execution queues and the database file. After this method
     * is called, further work with {@link DiaryPersistence} instances
     * obtained from this factory is no longer possible.
     */
    @Override public void close()
    {
	queues.close();
	store.close();
	log.trace("The diary database closed");
    }
}
