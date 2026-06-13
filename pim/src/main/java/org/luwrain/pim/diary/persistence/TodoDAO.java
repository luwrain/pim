// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.pim.diary.persistence;

import java.util.*;

/**
 * Data Access Object interface for to-do items.
 * Provides basic CRUD operations for {@link Todo} objects.
 *
 * <p>An implementation of this interface is obtained via
 * {@link DiaryPersistence#getTodoDAO()}.</p>
 *
 * @see Todo
 * @see DiaryPersistence
 * @see EventDAO
 */
public interface TodoDAO
{
    /**
     * Adds a new to-do item to storage. The {@link Todo#id} is assigned
     * automatically before saving.
     *
     * @param todo the to-do to add; must not be {@code null}
     * @return the assigned identifier of the new to-do
     * @throws NullPointerException if {@code todo} is {@code null}
     */
    long add(Todo todo);

    /**
     * Deletes a to-do item from storage. Matching is done by
     * {@link Todo#id}.
     *
     * @param todo the to-do to delete; must not be {@code null}
     * @throws NullPointerException if {@code todo} is {@code null}
     * @throws IllegalArgumentException if {@link Todo#id} is negative
     */
    void delete(Todo todo);

    /**
     * Returns the list of all to-do items stored in the database.
     *
     * @return list of all to-do items; never {@code null}, but may be empty
     */
    List<Todo> getAll();

    /**
     * Updates an existing to-do item. The record to update is found by
     * {@link Todo#id}.
     *
     * @param todo the to-do with new data; must not be {@code null}
     * @throws NullPointerException if {@code todo} is {@code null}
     * @throws IllegalArgumentException if {@link Todo#id} is negative
     */
    void update(Todo todo);
}
