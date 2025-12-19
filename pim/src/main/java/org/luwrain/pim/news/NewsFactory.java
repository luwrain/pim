// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>
// Copyright 2015 Roman Volovodov <gr.rPman@gmail.com>

package org.luwrain.pim.news;

import java.io.*;
import java.nio.file.*;
import org.apache.logging.log4j.*;
import org.h2.mvstore.*;

import org.luwrain.core.*;
import org.luwrain.pim.*;
import org.luwrain.pim.news.persist.*;

import static java.util.Objects.*;
import static java.nio.file.Files.*;

public final class NewsFactory implements AutoCloseable
{
    static private final Logger log = LogManager.getLogger();

    final Path path;
    final ExecQueues queues = new ExecQueues();
    private final MVStore store;
            private final MVMap<Integer, Group> groupsMap;
        private final MVMap<Long, Article> articlesMap;
    private final MVMap<String, Long> keysMap;

    public NewsFactory(Path path) throws IOException
    {
	this.path = requireNonNull(path, "path can't be null");
	createDirectories(path);
	final String dbFile = path.resolve("news.mvdb").toString();
	log.trace("Opening the news database in " + dbFile);
	this.store = MVStore.open(dbFile);
	this.articlesMap = store.openMap("articles");
		this.groupsMap = store.openMap("groups");
				this.keysMap = store.openMap("keys");
    }

    public Object newInstance()
    {
	return new NewsPersistence(queues, groupsMap, articlesMap, keysMap);
    }

@Override public void close()
    {
	queues.close();
	store.close();
    }
}


