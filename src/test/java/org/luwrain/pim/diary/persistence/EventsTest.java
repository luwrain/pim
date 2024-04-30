
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
