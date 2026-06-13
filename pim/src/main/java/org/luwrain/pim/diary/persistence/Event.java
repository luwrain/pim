// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.pim.diary.persistence;

import java.util.*;
import java.io.*;
import lombok.*;

/**
 * Модель события календаря, соответствующая компоненту VEVENT стандарта
 * iCalendar (<a href="https://tools.ietf.org/html/rfc5545">RFC 5545</a>).
 * Каждое событие обладает уникальным идентификатором {@link #id},
 * присваиваемым при сохранении. Все временные поля хранятся в виде
 * {@code long} — количество миллисекунд с начала эпохи Unix.
 *
 * <p>Сравнение двух событий через {@link #equals(Object)} и
 * {@link #hashCode()} основано исключительно на {@link #id}.</p>
 *
 * @see Todo
 * @see EventDAO
 * @see DiaryPersistence
 */
@Data
public class Event implements Serializable
{
    /**
     * Внутренний числовой идентификатор события.
     * Присваивается автоматически при добавлении в хранилище через
     * {@link EventDAO#add(Event)}.
     */
    private long id;

    // VEVENT core identification

    /**
     * Глобально уникальный идентификатор компонента (свойство UID
     * стандарта iCalendar). Должен быть уникальным в рамках всей
     * календарной системы.
     */
    private String uid;

    /**
     * Дата-время создания iCalendar-представления события (свойство DTSTAMP).
     * В миллисекундах с начала эпохи Unix.
     */
    private long dtStamp;

    /**
     * Номер ревизии компонента (свойство SEQUENCE). Начинается с 0 и
     * увеличивается при каждом значимом изменении, требующем уведомления
     * участников.
     */
    private int seq;

    // VEVENT timing

    /**
     * Краткий заголовок события (свойство SUMMARY).
     */
    private String title;

    /**
     * Полное описание события (свойство DESCRIPTION).
     * Может содержать многострочный текст.
     */
    private String comment;

    /**
     * Дата и время начала события (свойство DTSTART).
     * В миллисекундах с начала эпохи Unix.
     */
    private long dateTime;

    /**
     * Продолжительность события в минутах (свойство DURATION).
     */
    private int durationMin;

    /**
     * Дата и время окончания события (свойство DTEND).
     * В миллисекундах с начала эпохи Unix. Может быть {@code null},
     * если вместо этого используется {@link #durationMin}.
     */
    private Long dtEnd;

    /**
     * Дата и время создания события в календарной системе (свойство CREATED).
     * В миллисекундах с начала эпохи Unix. Может быть {@code null}.
     */
    private Long created;

    /**
     * Дата и время последнего изменения (свойство LAST-MODIFIED).
     * В миллисекундах с начала эпохи Unix. Может быть {@code null}.
     */
    private Long lastModified;

    // VEVENT location and geo

    /**
     * Текстовое описание места проведения события (свойство LOCATION).
     */
    private String location;

    /**
     * Географические координаты в виде строки {@code "широта;долгота"}
     * (свойство GEO). Широта и долгота задаются в градусах.
     */
    private String geo;

    // VEVENT classification

    /**
     * Класс доступа к событию (свойство CLASS).
     * Допустимые значения: {@code "PUBLIC"}, {@code "PRIVATE"},
     * {@code "CONFIDENTIAL"}.
     */
    private String clazz;

    /**
     * Признак прозрачности события для поиска свободного времени
     * (свойство TRANSP). Значение {@code "OPAQUE"} означает, что событие
     * блокирует время, {@code "TRANSPARENT"} — что время остаётся доступным.
     */
    private String transp;

    /**
     * Статус события (свойство STATUS). Допустимые значения:
     * {@code "TENTATIVE"}, {@code "CONFIRMED"}, {@code "CANCELLED"}.
     */
    private String status;

    /**
     * Приоритет события (свойство PRIORITY). Значение от 1 (наивысший)
     * до 9 (низший); 0 означает неопределённый приоритет.
     * {@code null} — приоритет не задан.
     */
    private Integer priority;

    // VEVENT organizer and attendees

    /**
     * Организатор события (свойство ORGANIZER). Обычно указывается в виде
     * URI {@code mailto:user@example.com} либо в виде общего имени CN.
     */
    private String organizer;

    /**
     * Контактная информация, связанная с событием (свойство CONTACT).
     */
    private String contact;

    // VEVENT URL and resources

    /**
     * URL, связанный с событием (свойство URL).
     */
    private String url;

    /**
     * Список ссылок на вложения (свойство ATTACH). Каждый элемент —
     * идентификатор или путь к связанному ресурсу.
     */
    private List<String> references;

    /**
     * Список категорий события (свойство CATEGORIES).
     */
    private List<String> categories;

    /**
     * Правило повторения события (свойство RRULE).
     * Строка в формате RRULE согласно RFC 5545.
     */
    private String rrule;

    /**
     * Сравнивает два события по идентификатору {@link #id}.
     *
     * @param o объект для сравнения
     * @return {@code true}, если {@code o} является событием с тем же id
     */
    @Override public boolean equals(Object o)
    {
	if (o != null && o instanceof Event e)
	    return id == e.id;
	return false;
    }

    /**
     * Возвращает хеш-код, основанный на {@link #id}.
     *
     * @return хеш-код события
     */
    @Override public int hashCode()
    {
	return Long.hashCode(id);
    }

    /**
     * Возвращает строковое представление события — его заголовок,
     * либо пустую строку, если заголовок не задан.
     *
     * @return заголовок события или пустая строка
     */
    @Override public String toString()
    {
	return title != null?title:"";
    }
}
