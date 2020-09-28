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

package org.luwrain.pim;

import java.sql.*;
import java.io.*;

import org.junit.*;

import org.luwrain.core.*;

public class SqliteCreatingTest extends Assert
{
    static private final File testDb = new File("/tmp/lwr-pim-test.sqlite");

    @Ignore @Test public void creatingNews() throws Exception
    {
	final Connection con = SQL.connect("org.sqlite.JDBC", "jdbc:sqlite:" + testDb.getAbsolutePath(), "", "");
	assertNotNull(con);
	assertTrue(SQL.initProc(con, "sqlite", "/org/luwrain/pim/news.sqlite"));
	final Statement st = con.createStatement();
	final ResultSet rs = st.executeQuery("SELECT * FROM news_article");
	assertFalse(rs.next());
	con.close();
    }

    @Ignore @Test public void creatingMail() throws Exception
    {
	final Connection con = SQL.connect("org.sqlite.JDBC", "jdbc:sqlite:" + testDb.getAbsolutePath(), "", "");
	assertNotNull(con);
	assertTrue(SQL.initProc(con, "sqlite", "/org/luwrain/pim/mail.sqlite"));
	Statement st = con.createStatement();
	ResultSet rs = st.executeQuery("SELECT * FROM mail_message");
	assertFalse(rs.next());
	st = con.createStatement();
	rs = st.executeQuery("SELECT * FROM mail_message_field");
	assertFalse(rs.next());
	con.close();
    }

        @Ignore @Test public void creatingContacts() throws Exception
    {
	final Connection con = SQL.connect("org.sqlite.JDBC", "jdbc:sqlite:" + testDb.getAbsolutePath(), "", "");
	assertNotNull(con);
	assertTrue(SQL.initProc(con, "sqlite", "/org/luwrain/pim/contacts.sqlite"));
	Statement st = con.createStatement();
	ResultSet rs = st.executeQuery("SELECT * FROM contact");
	assertFalse(rs.next());
	st = con.createStatement();
	rs = st.executeQuery("SELECT * FROM contact_value");
	assertFalse(rs.next());
	con.close();
    }


    

    @After @Before public void deleteTestDb()
    {
	if (testDb.exists())
   	    testDb.delete();
    }
}
