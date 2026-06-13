// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.pim.diary.persistence;

import java.util.*;

/**
 * Data Access Object interface for events.
 * Provides basic CRUD operations for {@link Event} objects.
 *
 * <p>An implementation of this interface is obtained via
 * {@link DiaryPersistence#getEventDAO()}.</p>
 *
 * @see Event
 * @see DiaryPersistence
 * @see TodoDAO
 */
public interface EventDAO
{
    /**
     * Adds a new event to storage. The {@link Event#id} is assigned
     * automatically before saving.
     *
     * @param event the event to add; must not be {@code null}
     * @return the assigned identifier of the new event
     * @throws NullPointerException if {@code event} is {@code null}
     */
    long add(Event event);

    /**
     * Deletes an event from storage. Matching is done by
     * {@link Event#id}.
     *
     * @param event the event to delete; must not be {@code null}
     * @throws NullPointerException if {@code event} is {@code null}
     * @throws IllegalArgumentException if {@link Event#id} is negative
     */
    void delete(Event event);

    /**
     * Returns the list of all events stored in the database.
     *
     * @return list of all events; never {@code null}, but may be empty
     */
    List<Event> getAll();

    /**
     * Updates an existing event. The record to update is found by
     * {@link Event#id}.
     *
     * @param event the event with new data; must not be {@code null}
     * @throws NullPointerException if {@code event} is {@code null}
     * @throws IllegalArgumentException if {@link Event#id} is negative
     */
    void update(Event event);
}
