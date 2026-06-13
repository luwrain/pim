// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.pim.diary.persistence;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Тест операций с событиями через {@link EventDAO}.
 * В настоящее время отключён ({@code @Disabled}), так как требует
 * запущенного MVStore-окружения, которое настраивается в
 * {@link DiaryPersistence}.
 *
 * @see Event
 * @see EventDAO
 * @see DiaryPersistence
 */
@Disabled
public class EventsTest
{
    /**
     * Основной тест добавления и чтения события.
     * Создаёт событие с заголовком и комментарием, сохраняет через
     * {@link EventDAO#add(Event)}, затем проверяет, что оно корректно
     * читается обратно.
     *
     * @throws Exception если тест завершается с ошибкой
     */
    @Test public void main() throws Exception
    {
	/*
	final var dao = getEventDAO();
	assertNotNull(dao);
	var ev = new Event();
	ev.setTitle("LUWRAIN title");
	ev.setComment("LUWRAIN comment");
	dao.add(ev);
	final var res = dao.getAll();
	assertNotNull(res);
	assertEquals(1, res.size());
	ev = res.get(0);
	assertNotNull(ev);
	assertEquals("LUWRAIN title", ev.getTitle());
		assertEquals("LUWRAIN comment", ev.getComment());
	*/
    }
}
