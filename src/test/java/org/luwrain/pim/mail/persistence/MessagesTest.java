
package org.luwrain.pim.mail.persistence;

import java.util.*;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import org.luwrain.pim.mail.persistence.model.*;
import static org.luwrain.pim.mail.persistence.MailPersistence.*;

public class MessagesTest
{
    @Test public void main() throws Exception
    {
	final var dao = getMessageDAO();
	assertNotNull(dao);
	var m = new MessageMetadata();
			m.setToAddr(Arrays.asList("info@luwrain.org"));
	dao.add(m);
	/*
	final var res = dao.getAll();
	assertNotNull(res);
	assertEquals(1, res.size());
	ac = res.get(0);
	assertNotNull(ac);
	assertEquals("LUWRAIN", ac.getName());
	*/
    }
}
