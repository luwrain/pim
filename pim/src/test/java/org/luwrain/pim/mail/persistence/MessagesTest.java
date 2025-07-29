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
	/*
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
