/*
   Copyright 2012-2024 Michael Pozhidaev <msp@luwrain.org>

   This file is part of LUWRAIN.

   LUWRAIN is free software; you can redistribute it and/or
   modify it under the terms of the GNU General Public
   License as published by the Free Software Foundation; either
   version 3 of the License, or (at your option) any later version.

   LUWRAIN is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
   General Public License for more details.
*/

package org.luwrain.pim.diary.persistence;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import org.luwrain.pim.diary.persistence.model.*;
import static org.luwrain.pim.diary.persistence.DiaryPersistence.*;

public class EventsTest
{
    @Test public void main() throws Exception
    {
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
    }
}
