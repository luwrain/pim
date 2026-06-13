/*
 * SPDX-License-Identifier: BUSL-1.1
 * Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>
 */

/**
 * Persistent storage layer for the personal diary and calendar subsystem.
 *
 * <p>This package contains the data model, DAO interfaces, and storage
 * engine for calendar events (VEVENT) and to-do items (VTODO) as
 * defined by the iCalendar standard
 * (<a href="https://tools.ietf.org/html/rfc5545">RFC 5545</a>).
 * All data is persisted in an H2 {@code MVStore} database through
 * the {@link org.luwrain.pim.diary.persistence.DiaryPersistence
 * DiaryPersistence} engine.</p>
 *
 * <h2>Data model</h2>
 *
 * <ul>
 *   <li>{@link org.luwrain.pim.diary.persistence.Event Event} —
 *   represents a calendar event (VEVENT). Contains all standard
 *   VEVENT properties: timing ({@code DTSTART}, {@code DTEND},
 *   {@code DURATION}), location ({@code LOCATION}, {@code GEO}),
 *   classification ({@code CLASS}, {@code STATUS}, {@code PRIORITY},
 *   {@code TRANSP}), organiser and attendee information, recurrence
 *   rules ({@code RRULE}), categories, and attachments.</li>
 *
 *   <li>{@link org.luwrain.pim.diary.persistence.Todo Todo} —
 *   represents a to-do item (VTODO). Contains all standard VTODO
 *   properties: timing ({@code DTSTART}, {@code DUE},
 *   {@code DURATION}, {@code COMPLETED}), progress
 *   ({@code PERCENT-COMPLETE}), location, classification, organiser
 *   information, recurrence rules, related parent references
 *   ({@code RELATED-TO}), categories, resources, and attachments.</li>
 * </ul>
 *
 * <p>Both model classes use {@code lombok.Data @Data} for automatic
 * generation of getters, setters, {@code toString()}, and
 * {@code equals()}/{@code hashCode()} (the latter two are overridden
 * to compare solely by {@code id}).</p>
 *
 * <h2>DAO layer</h2>
 *
 * <ul>
 *   <li>{@link org.luwrain.pim.diary.persistence.EventDAO EventDAO} —
 *   Data Access Object interface for events. Provides {@code add},
 *   {@code delete}, {@code getAll}, and {@code update} operations.</li>
 *
 *   <li>{@link org.luwrain.pim.diary.persistence.TodoDAO TodoDAO} —
 *   Data Access Object interface for to-do items. Provides the same
 *   CRUD operations as {@code EventDAO}.</li>
 * </ul>
 *
 * <p>Concrete implementations of both DAOs are returned by
 * {@link org.luwrain.pim.diary.persistence.DiaryPersistence#getEventDAO()
 * DiaryPersistence.getEventDAO()} and
 * {@link org.luwrain.pim.diary.persistence.DiaryPersistence#getTodoDAO()
 * DiaryPersistence.getTodoDAO()} respectively. All operations are
 * executed asynchronously through the {@link org.luwrain.pim.ExecQueues}
 * execution queues to serialise access to the underlying
 * {@code MVStore}.</p>
 *
 * <h2>Storage engine</h2>
 *
 * <p>{@link org.luwrain.pim.diary.persistence.DiaryPersistence
 * DiaryPersistence} is the central storage engine. It holds references
 * to three {@code MVMap} instances:</p>
 *
 * <dl>
 *   <dt>{@code eventsMap} ({@code MVMap<Long, Event>})</dt>
 *   <dd>Maps numeric identifiers to event objects.</dd>
 *
 *   <dt>{@code todosMap} ({@code MVMap<Long, Todo>})</dt>
 *   <dd>Maps numeric identifiers to to-do objects.</dd>
 *
 *   <dt>{@code keysMap} ({@code MVMap<String, Long>})</dt>
 *   <dd>Maps fully qualified class names to the last issued numeric
 *   identifier. This allows {@code Event} and {@code Todo} to have
 *   independent ID sequences, both starting from 0.</dd>
 * </dl>
 *
 * <p>The execution priority of storage operations can be adjusted at
 * runtime via
 * {@link org.luwrain.pim.diary.persistence.DiaryPersistence#setPriority(
 * org.luwrain.pim.ExecQueues.Priority) DiaryPersistence.setPriority()}.
 * The default is {@code Priority.MEDIUM}.</p>
 *
 * <h2>Identifier generation</h2>
 *
 * <p>New numeric identifiers are produced by
 * {@link org.luwrain.pim.diary.persistence.DiaryPersistence#getNewKey(Class)
 * DiaryPersistence.getNewKey(Class)}. Each class ({@code Event.class},
 * {@code Todo.class}) has its own counter stored in the {@code keys}
 * map. The first identifier for each class is 0, and each subsequent
 * call increments the counter by 1.</p>
 *
 * @see org.luwrain.pim.diary.persistence.Event
 * @see org.luwrain.pim.diary.persistence.Todo
 * @see org.luwrain.pim.diary.persistence.EventDAO
 * @see org.luwrain.pim.diary.persistence.TodoDAO
 * @see org.luwrain.pim.diary.persistence.DiaryPersistence
 * @see org.luwrain.pim.diary.DiaryFactory
 * @see org.luwrain.pim.ExecQueues
 */
package org.luwrain.pim.diary.persistence;
