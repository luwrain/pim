// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.pim.diary.persistence;

import java.util.*;

/**
 * Интерфейс доступа к данным событий (Data Access Object).
 * Предоставляет базовые CRUD-операции для объектов {@link Event}.
 *
 * <p>Реализация этого интерфейса создаётся через
 * {@link DiaryPersistence#getEventDAO()}.</p>
 *
 * @see Event
 * @see DiaryPersistence
 * @see TodoDAO
 */
public interface EventDAO
{
    /**
     * Добавляет новое событие в хранилище. Идентификатор {@link Event#id}
     * присваивается автоматически перед сохранением.
     *
     * @param event событие для добавления; не может быть {@code null}
     * @return присвоенный идентификатор нового события
     * @throws NullPointerException если {@code event} равен {@code null}
     */
    long add(Event event);

    /**
     * Удаляет событие из хранилища. Сравнение происходит по идентификатору
     * {@link Event#id}.
     *
     * @param event событие для удаления; не может быть {@code null}
     * @throws NullPointerException если {@code event} равен {@code null}
     * @throws IllegalArgumentException если {@link Event#id} отрицательный
     */
    void delete(Event event);

    /**
     * Возвращает список всех событий, хранящихся в базе.
     *
     * @return список всех событий; никогда не {@code null},
     *         но может быть пустым
     */
    List<Event> getAll();

    /**
     * Обновляет существующее событие. Поиск обновляемой записи происходит
     * по идентификатору {@link Event#id}.
     *
     * @param event событие с новыми данными; не может быть {@code null}
     * @throws NullPointerException если {@code event} равен {@code null}
     * @throws IllegalArgumentException если {@link Event#id} отрицательный
     */
    void update(Event event);
}
