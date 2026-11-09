// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.pim.tasks.persist;

import java.util.*;
import java.util.function.*;

/**
 * Data access object for {@link Task} instances.
 *
 * <p>This interface defines the operations for loading, creating, updating
 * and deleting tasks, as well as for querying various aggregate information
 * about the tasks stored in a {@link TaskList}.</p>
 */
public interface TaskDAO
{
    /**
     * Loads all tasks belonging to the specified task list.
     *
     * @param list the task list whose tasks should be loaded
     * @return the list of tasks belonging to the task list
     * @throws NullPointerException if {@code list} is {@code null}
     */
    List<Task> load(TaskList list);

    /**
     * Loads the tasks belonging to the specified task list that match the
     * given filter.
     *
     * @param list the task list whose tasks should be loaded
     * @param filter the predicate used to select tasks; if {@code null},
     *               all tasks of the task list are returned
     * @return the list of matching tasks
     * @throws NullPointerException if {@code list} is {@code null}
     */
    List<Task> load(TaskList list, Predicate<Task> filter);

    /**
     * Updates the stored representation of the specified task.
     *
     * @param task the task to update
     * @throws NullPointerException if {@code task} is {@code null}
     * @throws IllegalArgumentException if the task has a negative identifier
     */
    void update(Task task);

    /**
     * Adds a new task to the specified task list.
     *
     * @param list the task list the task should be added to
     * @param task the task to add
     * @return the identifier assigned to the newly added task
     * @throws NullPointerException if {@code list} or {@code task} is {@code null}
     */
    long add(TaskList list, Task task);

    /**
     * Deletes the specified task from the specified task list.
     *
     * @param list the task list the task belongs to
     * @param task the task to delete
     * @throws NullPointerException if {@code task} is {@code null}
     * @throws IllegalArgumentException if the task has a negative identifier
     */
    void delete(TaskList list, Task task);

    /**
     * Counts the tasks with the specified status in each of the specified
     * task lists.
     *
     * @param lists the task lists to count tasks for
     * @param status the status to count tasks for
     * @return a list where the element at index {@code i} is the number of
     *         tasks with the given status in the task list at index {@code i}
     *         of the input
     * @throws NullPointerException if {@code lists} or {@code status} is {@code null}
     */
    List<Integer> countByStatusInLists(List<TaskList> lists, Task.Status status);

    /**
     * Counts the tasks with the specified status in the specified task list.
     *
     * @param list the task list to count tasks for
     * @param status the status to count tasks for
     * @return the number of tasks with the given status in the task list
     * @throws NullPointerException if {@code list} or {@code status} is {@code null}
     */
    int countByStatusInList(TaskList list, Task.Status status);

    /**
     * Counts the tasks with the specified priority in the specified task list.
     *
     * @param list the task list to count tasks for
     * @param priority the priority to count tasks for
     * @return the number of tasks with the given priority in the task list
     * @throws NullPointerException if {@code list} or {@code priority} is {@code null}
     */
    int countByPriorityInList(TaskList list, Task.Priority priority);
}
