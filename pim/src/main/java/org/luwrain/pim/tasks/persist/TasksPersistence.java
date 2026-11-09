// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.pim.tasks.persist;

import java.util.*;
import java.util.function.*;
import java.util.concurrent.*;
import org.apache.logging.log4j.*;
import org.h2.mvstore.*;

import org.luwrain.pim.*;

import static java.util.Objects.*;
import static org.luwrain.pim.ExecQueues.*;

/**
 * Persistent storage for task lists and tasks.
 *
 * <p>This class provides access to the underlying H2 MVStore maps that hold
 * {@link TaskList} and {@link Task} instances, as well as the map used to
 * generate unique identifiers. All data access is performed through the
 * {@link #getTaskListDAO()} and {@link #getTaskDAO()} methods, which return
 * data access objects whose operations are executed on the configured
 * {@link ExecQueues} runner to preserve thread safety.</p>
 */
public final class TasksPersistence
{
    static private final Logger log = LogManager.getLogger();

    /** The execution queues used to run data access operations. */
    final ExecQueues queues;

    /** The priority used when scheduling data access operations. */
    private Priority priority = Priority.MEDIUM;

    /** The runner that executes data access operations on the queues. */
    private Runner runner;

    /** The map storing task lists, keyed by task list identifier. */
    private final MVMap<Integer, TaskList> listsMap;

    /** The map storing tasks, keyed by task identifier. */
    private final MVMap<Long, Task> tasksMap;

    /** The map storing the last assigned identifiers, keyed by class name. */
    private final MVMap<String, Long> keysMap;

    /**
     * Creates a new persistence instance backed by the specified maps.
     *
     * @param queues the execution queues used to run data access operations
     * @param listsMap the map storing task lists
     * @param tasksMap the map storing tasks
     * @param keysMap the map storing the last assigned identifiers
     * @throws NullPointerException if any argument is {@code null}
     */
    public TasksPersistence(ExecQueues queues,
			     MVMap<Integer, TaskList> listsMap,
			     MVMap<Long, Task> tasksMap,
			     MVMap<String, Long> keysMap)
    {
	this.queues = requireNonNull(queues, "queues can't be null");
	this.listsMap = requireNonNull(listsMap, "listsMap can't be null");
	this.tasksMap = requireNonNull(tasksMap, "tasksMap can't be null");
	this.keysMap = requireNonNull(keysMap, "keysMap can't be null");
	this.runner = new Runner(queues, priority);
    }

    /**
     * Returns the data access object for {@link TaskList} instances.
     *
     * @return the task list data access object
     */
    public TaskListDAO getTaskListDAO()
    {
	return new TaskListDAO()
	{
	    @Override public List<TaskList> load()
	    {
		return runner.run(() -> listsMap.entrySet().stream()
						    .map( e -> e.getValue() )
						    .toList());
	    }

	    @Override public int add(TaskList list)
	    {
		requireNonNull(list, "list can't be null");
		return runner.run(() -> {
			    final int newId = getNewKey(TaskList.class).intValue();
			    list.setId(newId);
			    log.trace("Adding " + list);
			    listsMap.put(Integer.valueOf(newId), list);
			    return Integer.valueOf(newId);
		}).intValue();
	    }

	    @Override public void delete(TaskList list)
	    {
		requireNonNull(list, "list can't be null");
		if (list.getId() < 0)
		    throw new IllegalArgumentException("A task list can't have negative ID");
		log.trace("Removing " + list);
		runner.run(new FutureTask<Object>( () -> listsMap.remove(Integer.valueOf(list.getId())) , null));
	    }

	    @Override public void update(TaskList list)
	    {
		requireNonNull(list, "list can't be null");
		if (list.getId() < 0)
		    throw new IllegalArgumentException("A task list can't have negative ID");
		log.trace("Updating " + list);
		runner.run(new FutureTask<Object>( () -> listsMap.put(Integer.valueOf(list.getId()), list) , null));
	    }
	};
    }

    /**
     * Returns the data access object for {@link Task} instances.
     *
     * @return the task data access object
     */
    public TaskDAO getTaskDAO()
    {
	return new TaskDAO()
	{
	    @Override public List<Task> load(TaskList list)
	    {
		requireNonNull(list, "list");
		return runner.run(() -> tasksMap.entrySet().stream()
					  .filter(e -> e.getValue().getListId() == list.getId())
					  .map( e -> e.getValue() )
					  .toList());
	    }

	    @Override public List<Task> load(TaskList list, Predicate<Task> filter)
	    {
		requireNonNull(list, "list");
		if (filter == null)
		    return load(list);
		return runner.run(() -> tasksMap.entrySet().stream()
					  .filter(e -> e.getValue().getListId() == list.getId())
					  .filter(e -> filter.test(e.getValue()))
					  .map( e -> e.getValue() )
					  .toList());
	    }

	    @Override public void update(Task task)
	    {
		requireNonNull(task, "task can't be null");
		if (task.getId() < 0)
		    throw new IllegalArgumentException("A task can't have negative ID");
		log.trace("Updating " + task);
		runner.run(new FutureTask<Object>( () -> tasksMap.put(Long.valueOf(task.getId()), task) , null));
	    }

	    @Override public long add(TaskList list, Task task)
	    {
		requireNonNull(list, "list can't be null");
		requireNonNull(task, "task");
		return runner.run(() -> {
			    final long newId = getNewKey(Task.class).longValue();
			    task.setId(newId);
			    task.setListId(list.getId());
			    tasksMap.put(Long.valueOf(newId), task);
			    return Long.valueOf(newId);
		}).longValue();
	    }

	    @Override public void delete(TaskList list, Task task)
	    {
		requireNonNull(task, "task can't be null");
		if (task.getId() < 0)
		    throw new IllegalArgumentException("A task can't have negative ID");
		log.trace("Removing " + task);
		runner.run(new FutureTask<Object>( () -> tasksMap.remove(Long.valueOf(task.getId())) , null));
	    }

	    @Override public List<Integer> countByStatusInLists(List<TaskList> lists, Task.Status status)
	    {
		requireNonNull(lists, "lists can't be null");
		requireNonNull(status, "status can't be null");
		return runner.run(() -> {
		    final var counts = new HashMap<Integer, Integer>();
		    for (final var e : tasksMap.entrySet())
		    {
			final var t = e.getValue();
			if (t.getStatus() != status)
			    continue;
			counts.merge(Integer.valueOf(t.getListId()), Integer.valueOf(1), Integer::sum);
		    }
		    final var res = new ArrayList<Integer>(lists.size());
		    for (final var l : lists)
			res.add(counts.getOrDefault(Integer.valueOf(l.getId()), Integer.valueOf(0)));
		    return res;
		});
	    }

	    @Override public int countByStatusInList(TaskList list, Task.Status status)
	    {
		requireNonNull(list, "list can't be null");
		requireNonNull(status, "status can't be null");
		return runner.run(() -> {
		    int count = 0;
		    for (final var e : tasksMap.entrySet())
		    {
			final var t = e.getValue();
			if (t.getListId() == list.getId() && t.getStatus() == status)
			    count++;
		    }
		    return Integer.valueOf(count);
		}).intValue();
	    }

	    @Override public int countByPriorityInList(TaskList list, Task.Priority priority)
	    {
		requireNonNull(list, "list can't be null");
		requireNonNull(priority, "priority can't be null");
		return runner.run(() -> {
		    int count = 0;
		    for (final var e : tasksMap.entrySet())
		    {
			final var t = e.getValue();
			if (t.getListId() == list.getId() && t.getPriority() == priority)
			    count++;
		    }
		    return Integer.valueOf(count);
		}).intValue();
	    }
	};
    }

    /**
     * Generates and returns the next unique identifier for the given class.
     *
     * @param c the class for which an identifier is requested
     * @return the newly assigned identifier
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
     * Sets the priority used when scheduling data access operations.
     *
     * @param priority the new priority
     * @throws NullPointerException if {@code priority} is {@code null}
     */
    public void setPriority(Priority priority)
    {
	this.priority = requireNonNull(priority, "priority can't be null");
	this.runner = new Runner(queues, priority);
    }
}
