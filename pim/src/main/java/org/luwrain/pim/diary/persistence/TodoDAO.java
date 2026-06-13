// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.pim.diary.persistence;

import java.util.*;

/**
 * Интерфейс доступа к данным задач (Data Access Object).
 * Предоставляет базовые CRUD-операции для объектов {@link Todo}.
 *
 * <p>Реализация этого интерфейса создаётся через
 * {@link DiaryPersistence#getTodoDAO()}.</p>
 *
 * @see Todo
 * @see DiaryPersistence
 * @see EventDAO
 */
public interface TodoDAO
{
    /**
     * Добавляет новую задачу в хранилище. Идентификатор {@link Todo#id}
     * присваивается автоматически перед сохранением.
     *
     * @param todo задача для добавления; не может быть {@code null}
     * @return присвоенный идентификатор новой задачи
     * @throws NullPointerException если {@code todo} равен {@code null}
     */
    long add(Todo todo);

    /**
     * Удаляет задачу из хранилища. Сравнение происходит по идентификатору
     * {@link Todo#id}.
     *
     * @param todo задача для удаления; не может быть {@code null}
     * @throws NullPointerException если {@code todo} равен {@code null}
     * @throws IllegalArgumentException если {@link Todo#id} отрицательный
     */
    void delete(Todo todo);

    /**
     * Возвращает список всех задач, хранящихся в базе.
     *
     * @return список всех задач; никогда не {@code null},
     *         но может быть пустым
     */
    List<Todo> getAll();

    /**
     * Обновляет существующую задачу. Поиск обновляемой записи происходит
     * по идентификатору {@link Todo#id}.
     *
     * @param todo задача с новыми данными; не может быть {@code null}
     * @throws NullPointerException если {@code todo} равен {@code null}
     * @throws IllegalArgumentException если {@link Todo#id} отрицательный
     */
    void update(Todo todo);
}
