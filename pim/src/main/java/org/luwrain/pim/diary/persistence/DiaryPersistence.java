// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.pim.diary.persistence;

import java.util.*;

import org.apache.logging.log4j.*;
import org.h2.mvstore.*;

import org.luwrain.pim.*;

import static java.util.Objects.*;
import static org.luwrain.pim.ExecQueues.*;

/**
 * Ядро хранения данных дневника. Управляет MVStore-картами событий
 * и задач, а также предоставляет реализации {@link EventDAO} и
 * {@link TodoDAO}, выполняющие все операции через очереди исполнения
 * {@link ExecQueues}. Генерация новых числовых идентификаторов
 * для событий и задач ведётся раздельно через общую карту ключей.
 *
 * <p>Экземпляры этого класса создаются фабрикой
 * {@link org.luwrain.pim.diary.DiaryFactory}.</p>
 *
 * @see Event
 * @see EventDAO
 * @see Todo
 * @see TodoDAO
 * @see org.luwrain.pim.diary.DiaryFactory
 */
public final class DiaryPersistence
{
    static private final Logger log = LogManager.getLogger();

    /** Очереди исполнения для асинхронных операций. */
    final ExecQueues queues;

    /** Текущий приоритет операций с хранилищем. */
    private Priority priority = Priority.MEDIUM;

    /** Исполнитель, оборачивающий операции в раннер с приоритетом. */
    private Runner runner = null;

    /** Карта событий: идентификатор → {@link Event}. */
    private final MVMap<Long, Event> eventsMap;

    /** Карта задач: идентификатор → {@link Todo}. */
    private final MVMap<Long, Todo> todosMap;

    /**
     * Карта ключей: полное имя класса → последний выданный числовой
     * идентификатор. Используется для генерации новых id через
     * {@link #getNewKey(Class)}.
     */
    private final MVMap<String, Long> keysMap;

    /**
     * Создаёт экземпляр хранилища дневника.
     *
     * @param queues    очереди исполнения для выполнения операций с картами
     * @param eventsMap карта MVStore для хранения событий
     * @param todosMap  карта MVStore для хранения задач
     * @param keysMap   карта MVStore для генерации идентификаторов
     * @throws NullPointerException если любой из параметров равен {@code null}
     */
    public DiaryPersistence(ExecQueues queues,
			    MVMap<Long, Event> eventsMap,
			    MVMap<Long, Todo> todosMap,
			    MVMap<String, Long> keysMap)
    {
	this.queues = requireNonNull(queues, "queues can't be null");
	this.eventsMap = requireNonNull(eventsMap, "eventsMap can't be null");
	this.todosMap = requireNonNull(todosMap, "todosMap can't be null");
	this.keysMap = requireNonNull(keysMap, "keysMap can't be null");
	this.runner = new Runner(queues, priority);
    }

    /**
     * Возвращает реализацию {@link EventDAO} для работы с событиями.
     * Все операции (добавление, удаление, выборка, обновление) выполняются
     * асинхронно через очереди исполнения с текущим приоритетом.
     *
     * @return объект доступа к данным событий
     */
    public EventDAO getEventDAO()
    {
	return new EventDAO(){
	    @Override public long add(Event event)
	    {
		requireNonNull(event, "event can't be null");
		return runner.run(() -> {
		    final long newId = getNewKey(Event.class).longValue();
		    event.setId(newId);
		    log.trace("Adding " + event);
		    eventsMap.put(Long.valueOf(newId), event);
		    return Long.valueOf(newId);
		}).longValue();
	    }

	    @Override public void delete(Event event)
	    {
		requireNonNull(event, "event can't be null");
		if (event.getId() < 0)
		    throw new IllegalArgumentException("An event can't have negative ID");
		log.trace("Deleting " + event);
		runner.run(() -> eventsMap.remove(Long.valueOf(event.getId())));
	    }

	    @Override public List<Event> getAll()
	    {
		return runner.run(() -> eventsMap.entrySet().stream()
				  .map(e -> e.getValue())
				  .toList());
	    }

	    @Override public void update(Event event)
	    {
		requireNonNull(event, "event can't be null");
		if (event.getId() < 0)
		    throw new IllegalArgumentException("An event can't have negative ID");
		log.trace("Updating " + event);
		runner.run(() -> {
		    eventsMap.put(Long.valueOf(event.getId()), event);
		    return null;
		});
	    }
	};
    }

    /**
     * Возвращает реализацию {@link TodoDAO} для работы с задачами.
     * Все операции (добавление, удаление, выборка, обновление) выполняются
     * асинхронно через очереди исполнения с текущим приоритетом.
     *
     * @return объект доступа к данным задач
     */
    public TodoDAO getTodoDAO()
    {
	return new TodoDAO(){
	    @Override public long add(Todo todo)
	    {
		requireNonNull(todo, "todo can't be null");
		return runner.run(() -> {
		    final long newId = getNewKey(Todo.class).longValue();
		    todo.setId(newId);
		    log.trace("Adding " + todo);
		    todosMap.put(Long.valueOf(newId), todo);
		    return Long.valueOf(newId);
		}).longValue();
	    }

	    @Override public void delete(Todo todo)
	    {
		requireNonNull(todo, "todo can't be null");
		if (todo.getId() < 0)
		    throw new IllegalArgumentException("A todo can't have negative ID");
		log.trace("Deleting " + todo);
		runner.run(() -> todosMap.remove(Long.valueOf(todo.getId())));
	    }

	    @Override public List<Todo> getAll()
	    {
		return runner.run(() -> todosMap.entrySet().stream()
				  .map(e -> e.getValue())
				  .toList());
	    }

	    @Override public void update(Todo todo)
	    {
		requireNonNull(todo, "todo can't be null");
		if (todo.getId() < 0)
		    throw new IllegalArgumentException("A todo can't have negative ID");
		log.trace("Updating " + todo);
		runner.run(() -> {
		    todosMap.put(Long.valueOf(todo.getId()), todo);
		    return null;
		});
	    }
	};
    }

    /**
     * Выдаёт новый уникальный числовой идентификатор для указанного класса.
     * Идентификаторы независимы для каждого класса: {@code Event} и
     * {@code Todo} имеют собственные последовательности.
     *
     * @param c класс, для которого требуется новый идентификатор
     * @return новый идентификатор, начинающийся с 0 и увеличивающийся на 1
     *         при каждом вызове
     */
    Long getNewKey(Class c)
    {
	final var res = keysMap.get(c.getName());
	if (res == null)
	{
	    keysMap.put(c.getName(), Long.valueOf(0));
	    return Long.valueOf(0);
	}
	final var newVal = Long.valueOf(res.longValue() + 1);
	keysMap.put(c.getName(), newVal);
	return newVal;
    }

    /**
     * Устанавливает приоритет выполнения операций с хранилищем.
     * При изменении приоритета создаётся новый {@link Runner},
     * так что все последующие операции будут выполняться с новым приоритетом.
     *
     * @param priority новый приоритет; не может быть {@code null}
     * @throws NullPointerException если {@code priority} равен {@code null}
     */
    public void setPriority(Priority priority)
    {
	this.priority = requireNonNull(priority, "priority can't be null");
	this.runner = new Runner(queues, priority);
    }
}
