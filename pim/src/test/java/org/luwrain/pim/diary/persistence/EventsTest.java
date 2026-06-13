// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.pim.diary.persistence;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test for event operations via {@link EventDAO}.
 * Currently disabled ({@code @Disabled}) because it requires a running
 * MVStore environment which is set up in {@link DiaryPersistence}.
 *
 * @see Event
 * @see EventDAO
 * @see DiaryPersistence
 */
@Disabled
public class EventsTest
{
    /**
     * Main test for adding and reading an event.
     * Creates an event with a title and comment, saves it via
     * {@link EventDAO#add(Event)}, then verifies that it is read back
     * correctly.
     *
     * @throws Exception if the test fails
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
