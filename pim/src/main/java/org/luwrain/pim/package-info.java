/*
 * SPDX-License-Identifier: BUSL-1.1
 * Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>
 */

/**
 * The root package of the LUWRAIN Personal Information Manager (PIM) subsystem.
 *
 * <p>This package provides the foundation for managing personal data
 * within the LUWRAIN platform. It integrates several subcomponents
 * — mail, news (RSS/Atom feeds), contacts, and a personal diary —
 * into a unified framework backed by persistent storage using H2
 * {@code MVStore} databases.</p>
 *
 * <h2>Core classes</h2>
 *
 * <ul>
 *   <li>{@link org.luwrain.pim.Extension Extension} — the LUWRAIN
 *   extension entry point. It initialises the entire PIM subsystem:
 *   creates a {@link org.luwrain.pim.PimObjFactory}, starts background
 *   workers for fetching news, POP3 mail, and SMTP sending, and
 *   registers control-panel factories for mail settings.</li>
 *
 *   <li>{@link org.luwrain.pim.PimObjFactory PimObjFactory} — the main
 *   object factory that produces persistence-layer instances for each
 *   PIM domain. It implements {@link org.luwrain.core.ObjFactory} and
 *   resolves requests by the fully qualified name of the desired
 *   persistence interface
 *   (e.g. {@link org.luwrain.pim.mail.persistence.MailPersistence MailPersistence},
 *   {@link org.luwrain.pim.news.persist.NewsPersistence NewsPersistence},
 *   {@link org.luwrain.pim.contacts.persistence.ContactsPersistence ContactsPersistence},
 *   {@link org.luwrain.pim.diary.persistence.DiaryPersistence DiaryPersistence}).</li>
 *
 *   <li>{@link org.luwrain.pim.ExecQueues ExecQueues} — a background
 *   execution queue with {@code HIGH} and {@code MEDIUM} priority
 *   levels. All factory classes use their own queue instance to
 *   serialise database operations and avoid contention on the
 *   underlying {@code MVStore}.</li>
 *
 *   <li>{@link org.luwrain.pim.Hooks Hooks} — defines scripting
 *   hooks exposed to LUWRAIN scripts. The primary hook is
 *   {@code luwrain.mail.incoming}, which fires whenever a new mail
 *   message is received.</li>
 *
 *   <li>{@link org.luwrain.pim.PimException PimException} — a
 *   runtime exception used throughout the PIM subsystem to wrap
 *   checked exceptions and signal PIM-specific error conditions.</li>
 * </ul>
 *
 * <h2>Sub-packages</h2>
 *
 * <dl>
 *   <dt>{@code org.luwrain.pim.mail}</dt>
 *   <dd>Mail subsystem: factory, message model, MIME decoding,
 *   POP3 and SMTP protocol clients, persistent storage for accounts,
 *   folders, and message metadata, scripting objects, and a registry
 *   of popular mail servers.</dd>
 *
 *   <dt>{@code org.luwrain.pim.news}</dt>
 *   <dd>News (RSS/Atom) subsystem: factory, media resource model,
 *   persistent storage for feed groups and articles.</dd>
 *
 *   <dt>{@code org.luwrain.pim.contacts}</dt>
 *   <dd>Contacts subsystem: factory, persistent storage for contacts,
 *   folders, phone numbers, email addresses, and postal addresses.</dd>
 *
 *   <dt>{@code org.luwrain.pim.diary}</dt>
 *   <dd>Personal diary/calendar subsystem: factory and persistent
 *   storage for events.</dd>
 *
 *   <dt>{@code org.luwrain.pim.fetching}</dt>
 *   <dd>Fetching infrastructure: {@code Control} interface for
 *   progress reporting and interruption checking, base classes for
 *   mail and news fetching operations, mail-connection parameter
 *   helpers, and a {@code FetchingException} type.</dd>
 *
 *   <dt>{@code org.luwrain.pim.workers}</dt>
 *   <dd>Background workers that implement
 *   {@link org.luwrain.core.Worker}. They periodically invoke the
 *   fetching layer to pull news articles, receive POP3 mail, and
 *   send queued SMTP messages.</dd>
 *
 *   <dt>{@code org.luwrain.pim.binder}</dt>
 *   <dd>Binder subsystem for managing stored cases (currently
 *   mostly stubbed out).</dd>
 *
 *   <dt>{@code org.luwrain.pim.publisher}</dt>
 *   <dd>Publisher subsystem for managing outgoing publications.</dd>
 * </dl>
 *
 * <h2>Lifecycle</h2>
 *
 * <p>When LUWRAIN starts, it instantiates
 * {@link org.luwrain.pim.Extension Extension}, which creates the
 * {@link org.luwrain.pim.PimObjFactory PimObjFactory} and the three
 * background workers. The factory uses the LUWRAIN variable path
 * {@code var:luwrain.pim} as the base directory for all database
 * files — one {@code .mvdb} file per domain (mail, news, contacts,
 * diary). On shutdown, {@link org.luwrain.pim.Extension#close()}
 * gracefully closes the factory, which in turn closes each
 * domain-specific factory and its execution queue.</p>
 *
 * <p>Persistence objects are obtained lazily: other LUWRAIN
 * components call
 * {@code luwrain.createInstance(MailPersistence.class)} (or the
 * equivalent for other domains), which delegates to
 * {@link org.luwrain.pim.PimObjFactory#newObject(String)
 * PimObjFactory.newObject()}. The factory creates the corresponding
 * domain factory and its {@code MVStore} on first access.</p>
 *
 * @see org.luwrain.pim.mail
 * @see org.luwrain.pim.news
 * @see org.luwrain.pim.contacts
 * @see org.luwrain.pim.diary
 * @see org.luwrain.pim.fetching
 * @see org.luwrain.pim.workers
 * @see org.luwrain.pim.binder
 * @see org.luwrain.pim.publisher
 */
package org.luwrain.pim;
