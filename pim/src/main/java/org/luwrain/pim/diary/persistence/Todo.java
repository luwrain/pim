// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.pim.diary.persistence;

import java.util.*;
import java.io.*;
import lombok.*;

/**
 * Модель задачи (to-do), соответствующая компоненту VTODO стандарта
 * iCalendar (<a href="https://tools.ietf.org/html/rfc5545">RFC 5545</a>).
 * Каждая задача обладает уникальным идентификатором {@link #id},
 * присваиваемым при сохранении. Все временные поля, кроме {@link #dtStamp},
 * хранятся как {@link Long} — количество миллисекунд с начала эпохи Unix
 * — и могут быть {@code null}, если значение не задано.
 *
 * <p>Сравнение двух задач через {@link #equals(Object)} и
 * {@link #hashCode()} основано исключительно на {@link #id}.</p>
 *
 * @see Event
 * @see TodoDAO
 * @see DiaryPersistence
 */
@Data
public class Todo implements Serializable
{
    /**
     * Внутренний числовой идентификатор задачи.
     * Присваивается автоматически при добавлении в хранилище через
     * {@link TodoDAO#add(Todo)}.
     */
    private long id;

    // VTODO core identification

    /**
     * Глобально уникальный идентификатор компонента (свойство UID
     * стандарта iCalendar). Должен быть уникальным в рамках всей
     * календарной системы.
     */
    private String uid;

    /**
     * Дата-время создания iCalendar-представления задачи (свойство DTSTAMP).
     * В миллисекундах с начала эпохи Unix.
     */
    private long dtStamp;

    /**
     * Номер ревизии компонента (свойство SEQUENCE). Начинается с 0 и
     * увеличивается при каждом значимом изменении.
     */
    private int seq;

    // VTODO content

    /**
     * Краткий заголовок задачи (свойство SUMMARY).
     */
    private String title;

    /**
     * Полное описание задачи (свойство DESCRIPTION).
     * Может содержать многострочный текст.
     */
    private String comment;

    // VTODO timing

    /**
     * Дата и время начала задачи (свойство DTSTART).
     * В миллисекундах с начала эпохи Unix. Может быть {@code null},
     * если время начала не задано.
     */
    private Long dtStart;

    /**
     * Крайний срок выполнения задачи (свойство DUE).
     * В миллисекундах с начала эпохи Unix. Может быть {@code null},
     * если срок не задан.
     */
    private Long due;

    /**
     * Продолжительность задачи в минутах (свойство DURATION).
     * Может быть {@code null}, если продолжительность не задана.
     */
    private Integer durationMin;

    /**
     * Дата и время завершения задачи (свойство COMPLETED).
     * В миллисекундах с начала эпохи Unix. Может быть {@code null},
     * если задача ещё не завершена.
     */
    private Long completed;

    /**
     * Дата и время создания задачи в календарной системе (свойство CREATED).
     * В миллисекундах с начала эпохи Unix. Может быть {@code null}.
     */
    private Long created;

    /**
     * Дата и время последнего изменения (свойство LAST-MODIFIED).
     * В миллисекундах с начала эпохи Unix. Может быть {@code null}.
     */
    private Long lastModified;

    // VTODO progress

    /**
     * Процент завершения задачи (свойство PERCENT-COMPLETE).
     * Целое число от 0 до 100. Может быть {@code null}, если не задан.
     */
    private Integer percentComplete;

    // VTODO location and geo

    /**
     * Текстовое описание места выполнения задачи (свойство LOCATION).
     */
    private String location;

    /**
     * Географические координаты в виде строки {@code "широта;долгота"}
     * (свойство GEO). Широта и долгота задаются в градусах.
     */
    private String geo;

    // VTODO classification

    /**
     * Класс доступа к задаче (свойство CLASS).
     * Допустимые значения: {@code "PUBLIC"}, {@code "PRIVATE"},
     * {@code "CONFIDENTIAL"}.
     */
    private String clazz;

    /**
     * Статус задачи (свойство STATUS). Допустимые значения:
     * {@code "NEEDS-ACTION"} (требует действия),
     * {@code "COMPLETED"} (завершена),
     * {@code "IN-PROCESS"} (в процессе выполнения),
     * {@code "CANCELLED"} (отменена).
     */
    private String status;

    /**
     * Приоритет задачи (свойство PRIORITY). Значение от 1 (наивысший)
     * до 9 (низший); 0 означает неопределённый приоритет.
     * {@code null} — приоритет не задан.
     */
    private Integer priority;

    // VTODO organizer and attendees

    /**
     * Организатор задачи (свойство ORGANIZER). Обычно указывается в виде
     * URI {@code mailto:user@example.com} либо в виде общего имени CN.
     */
    private String organizer;

    /**
     * Контактная информация, связанная с задачей (свойство CONTACT).
     */
    private String contact;

    // VTODO URL and resources

    /**
     * URL, связанный с задачей (свойство URL).
     */
    private String url;

    /**
     * Список ссылок на вложения (свойство ATTACH). Каждый элемент —
     * идентификатор или путь к связанному ресурсу.
     */
    private List<String> references;

    /**
     * Список категорий задачи (свойство CATEGORIES).
     */
    private List<String> categories;

    /**
     * Список ресурсов, задействованных в задаче (свойство RESOURCES).
     * Например, оборудование, помещения и т.п.
     */
    private List<String> resources;

    /**
     * Правило повторения задачи (свойство RRULE).
     * Строка в формате RRULE согласно RFC 5545.
     */
    private String rrule;

    /**
     * Идентификатор связанного родительского компонента (свойство RELATED-TO).
     * Используется для построения иерархии задач.
     */
    private String relatedTo;

    /**
     * Сравнивает две задачи по идентификатору {@link #id}.
     *
     * @param o объект для сравнения
     * @return {@code true}, если {@code o} является задачей с тем же id
     */
    @Override public boolean equals(Object o)
    {
	if (o != null && o instanceof Todo t)
	    return id == t.id;
	return false;
    }

    /**
     * Возвращает хеш-код, основанный на {@link #id}.
     *
     * @return хеш-код задачи
     */
    @Override public int hashCode()
    {
	return Long.hashCode(id);
    }

    /**
     * Возвращает строковое представление задачи — её заголовок,
     * либо пустую строку, если заголовок не задан.
     *
     * @return заголовок задачи или пустая строка
     */
    @Override public String toString()
    {
	return title != null?title:"";
    }
}
