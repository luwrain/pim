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

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import org.luwrain.pim.mail.persistence.model.*;
import static org.luwrain.pim.mail.persistence.MailPersistence.*;

public class AccountsTest
{
    @Test public void main() throws Exception
    {
	//	deleteAllAccounts();
	final var dao = getAccountDAO();
	assertNotNull(dao);
	var ac = new Account();
	ac.setName("LUWRAIN");
	dao.add(ac);
	final var res = dao.getAll();
	assertNotNull(res);
	assertEquals(1, res.size());
	ac = res.get(0);
	assertNotNull(ac);
	assertEquals("LUWRAIN", ac.getName());
    }

    @Test public void update() throws Exception
    {
	//	deleteAllAccounts();
	final var dao = getAccountDAO();
	assertNotNull(dao);
	var ac = new Account();
	ac.setName("LUWRAIN");
	dao.add(ac);
	final int id = ac.getId();
	var res = dao.getAll();
	assertNotNull(res);
	assertEquals(1, res.size());
	ac = res.get(0);
	assertNotNull(ac);
	assertEquals(id, ac.getId());
	assertEquals("LUWRAIN", ac.getName());
	ac.setName("Proba");
	dao.update(ac);
	res = dao.getAll();
	assertNotNull(res);
	assertEquals(1, res.size());
	ac = res.get(0);
	assertNotNull(ac);
	assertEquals(id, ac.getId());
	assertEquals("Proba", ac.getName());
    }
}
