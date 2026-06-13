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
 * Фабрика для доступа к базе данных дневника. Управляет жизненным циклом
 * MVStore-хранилища, открывая и закрывая файл базы данных {@code diary.mvdb}
 * в указанном каталоге. Предоставляет методы для получения экземпляра
 * {@link DiaryPersistence}, через который выполняется вся работа с событиями
 * ({@link org.luwrain.pim.diary.persistence.Event}) и задачами
 * ({@link org.luwrain.pim.diary.persistence.Todo}).
 *
 * <p>Типичное использование:</p>
 * <pre>{@code
 * try (var factory = new DiaryFactory(Paths.get("/home/user/luwrain/diary"))) {
 *     var persistence = (DiaryPersistence) factory.newInstance();
 *     var eventDao = persistence.getEventDAO();
 *     var todoDao = persistence.getTodoDAO();
 *     // работа с DAO...
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

    /** Путь к каталогу, содержащему файл базы данных дневника. */
    public final Path path;

    /** Очереди исполнения для асинхронных операций. */
    final ExecQueues queues = new ExecQueues();

    private final MVStore store;
    private final MVMap<Long, org.luwrain.pim.diary.persistence.Event> eventsMap;
    private final MVMap<Long, org.luwrain.pim.diary.persistence.Todo> todosMap;
    private final MVMap<String, Long> keysMap;

    /**
     * Создаёт новую фабрику дневника. Если каталог {@code path} не существует,
     * он будет создан вместе со всеми промежуточными каталогами. Файл базы
     * данных {@code diary.mvdb} открывается (или создаётся, если отсутствует)
     * внутри указанного каталога.
     *
     * @param path путь к каталогу, в котором хранится (или будет создан)
     *             файл {@code diary.mvdb}
     * @throws IOException если не удалось создать каталог или открыть базу данных
     * @throws NullPointerException если {@code path} равен {@code null}
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
     * Создаёт новый экземпляр {@link DiaryPersistence}, связанный с текущим
     * хранилищем. Каждый вызов возвращает новый объект, но все они работают
     * с одним и тем же файлом базы данных и очередями исполнения.
     *
     * @return новый экземпляр {@link DiaryPersistence}
     */
    public Object newInstance()
    {
	return new DiaryPersistence(queues, eventsMap, todosMap, keysMap);
    }

    /**
     * Закрывает очереди исполнения и файл базы данных. После вызова этого
     * метода дальнейшая работа с экземплярами {@link DiaryPersistence},
     * полученными от этой фабрики, невозможна.
     */
    @Override public void close()
    {
	queues.close();
	store.close();
	log.trace("The diary database closed");
    }
}
