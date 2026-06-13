/*
 * SPDX-License-Identifier: BUSL-1.1
 * Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>
 */

/**
 * Personal diary and calendar subsystem.
 *
 * <p>This package provides the entry point for managing calendar events
 * and to-do items within the LUWRAIN PIM framework. It is backed by an
 * H2 {@code MVStore} database whose lifecycle is managed by the
 * {@link org.luwrain.pim.diary.DiaryFactory DiaryFactory} class.</p>
 *
 * <h2>Core classes</h2>
 *
 * <ul>
 *   <li>{@link org.luwrain.pim.diary.DiaryFactory DiaryFactory} —
 *   the factory that opens and closes the {@code diary.mvdb} database
 *   file. It implements {@link java.lang.AutoCloseable} and should be
 *   used in a try-with-resources block. Its
 *   {@link org.luwrain.pim.diary.DiaryFactory#newInstance() newInstance()}
 *   method returns a {@link org.luwrain.pim.diary.persistence.DiaryPersistence
 *   DiaryPersistence} instance through which all data operations are
 *   performed.</li>
 * </ul>
 *
 * <h2>Sub-packages</h2>
 *
 * <dl>
 *   <dt>{@code org.luwrain.pim.diary.persistence}</dt>
 *   <dd>Persistent storage layer: the {@code DiaryPersistence} engine,
 *   DAO interfaces ({@code EventDAO}, {@code TodoDAO}), and the
 *   data model classes ({@code Event}, {@code Todo}) representing
 *   VEVENT and VTODO iCalendar components respectively.</dd>
 * </dl>
 *
 * <h2>Lifecycle</h2>
 *
 * <p>A {@link org.luwrain.pim.diary.DiaryFactory DiaryFactory}
 * instance is created by
 * {@link org.luwrain.pim.PimObjFactory PimObjFactory} on first access
 * to the diary subsystem. The factory opens the {@code diary.mvdb}
 * file in the directory configured via the LUWRAIN variable
 * {@code var:luwrain.pim}. The file contains three MVStore maps:
 * {@code events} for VEVENT data, {@code todos} for VTODO data, and
 * {@code keys} for generating unique numeric identifiers. The factory
 * is closed automatically when
 * {@link org.luwrain.pim.PimObjFactory#close() PimObjFactory.close()}
 * is called during LUWRAIN shutdown.</p>
 *
 * @see org.luwrain.pim.diary.DiaryFactory
 * @see org.luwrain.pim.diary.persistence
 * @see org.luwrain.pim.PimObjFactory
 */
package org.luwrain.pim.diary;
