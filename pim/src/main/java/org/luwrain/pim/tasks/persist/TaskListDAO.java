// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.pim.tasks.persist;

import java.util.*;

/**
 * Data access object for {@link TaskList} instances.
 *
 * <p>This interface defines the operations for loading, creating, updating
 * and deleting task lists.</p>
 */
public interface TaskListDAO
{
    /**
     * Adds a new task list to the storage.
     *
     * @param list the task list to add
     * @return the identifier assigned to the newly added task list
     * @throws NullPointerException if {@code list} is {@code null}
     */
    int add(TaskList list);

    /**
     * Deletes the specified task list from the storage.
     *
     * @param list the task list to delete
     * @throws NullPointerException if {@code list} is {@code null}
     * @throws IllegalArgumentException if the task list has a negative identifier
     */
    void delete(TaskList list);

    /**
     * Loads all task lists from the storage.
     *
     * @return the list of all stored task lists
     */
    List<TaskList> load();

    /**
     * Updates the stored representation of the specified task list.
     *
     * @param list the task list to update
     * @throws NullPointerException if {@code list} is {@code null}
     * @throws IllegalArgumentException if the task list has a negative identifier
     */
    void update(TaskList list);
}
