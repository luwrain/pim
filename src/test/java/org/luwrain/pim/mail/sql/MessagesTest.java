/*
   Copyright 2012-2017 Michael Pozhidaev <michael.pozhidaev@gmail.com>

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

package org.luwrain.pim.mail.sql;

import java.sql.*;
import java.io.*;

import org.junit.*;

import org.luwrain.core.*;
import org.luwrain.pim.*;
import org.luwrain.pim.mail.*;

public class MessagesTest extends Assert
{
    /*
    static private final File testDb = new File("/tmp/lwr-pim-test.sqlite");

    @Test public void emptyMessageAdding() throws Exception
    {
	final Connection con = SQL.connect("org.sqlite.JDBC", "jdbc:sqlite:" + testDb.getAbsolutePath(), "", "");
	assertNotNull(con);
	assertTrue(SQL.initProc(con, "sqlite", "/org/luwrain/pim/mail.sqlite"));
	final DummyRegistry registry = new DummyRegistry();
	final DummyExecQueue execQueue = new DummyExecQueue();
	final Folder folder = new Folder(registry, 1);
	final Messages messages = new Messages(execQueue, con);
	final Message message = new Message(con);
	messages.save(folder, message);
	final Statement st = con.createStatement();
	assertNotNull(st);
	final ResultSet rs = st.executeQuery("SELECT * FROM mail_message");
	assertNotNull(rs);
	assertTrue(rs.next());
	con.close();
    }

        @Test public void MessageAddingLoadingDeleting() throws Exception
    {
	final Connection con = SQL.connect("org.sqlite.JDBC", "jdbc:sqlite:" + testDb.getAbsolutePath(), "", "");
	assertNotNull(con);
	assertTrue(SQL.initProc(con, "sqlite", "/org/luwrain/pim/mail.sqlite"));
	final DummyRegistry registry = new DummyRegistry();
	final DummyExecQueue execQueue = new DummyExecQueue();
	final Folder folder = new Folder(registry, 1);
	final Messages messages = new Messages(execQueue, con);
	final Message message = new Message(con);
	messages.save(folder, message);

	final StoredMailMessage[] loaded = messages.load(folder);
	assertNotNull(loaded);
	assertTrue(loaded.length == 1);
	messages.delete(loaded[0]);
	final StoredMailMessage[] loadedEmpty = messages.load(folder);
	assertNotNull(loadedEmpty);
	assertTrue(loadedEmpty.length == 0);
	final Statement st = con.createStatement();
	assertNotNull(st);
	final ResultSet rs = st.executeQuery("SELECT * FROM mail_message");
	assertNotNull(rs);
	assertFalse(rs.next());
	con.close();
    }


    @After @Before public void deleteTestDb()
    {
	if (testDb.exists())
   	    testDb.delete();
    }
    */
}
