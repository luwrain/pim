/*
   Copyright 2012-2019 Michael Pozhidaev <michael.pozhidaev@gmail.com>
   Copyright 2015 Roman Volovodov <gr.rPman@gmail.com>

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

import java.io.*;
import    java.sql.*;

import org.luwrain.core.*;

public class SQL
{
    static private final String LOG_COMPONENT = "pim";

    static public Connection connect(String driver, String url, String login, String passwd)
    {
	NullCheck.notEmpty(driver, "driver");
	NullCheck.notEmpty(url, "url");
	NullCheck.notNull(login, "login");
	NullCheck.notNull(passwd, "passwd");
	try {
	    Class.forName (driver, true, Thread.currentThread().getContextClassLoader()).newInstance ();
return  DriverManager.getConnection (url, login, passwd);
	}
	catch(Throwable e)
	{
	    Log.error(LOG_COMPONENT, "unable to get JDBC connection with the driver " + driver + " for " + url + ":" + e.getClass().getName() + ":" + e.getMessage());
	    return null;
	}
    }

    static public boolean initProc(Connection con, String type, String sqliteResource)
    {
	NullCheck.notNull(con, "con");
	NullCheck.notNull(type, "type");
	NullCheck.notNull(sqliteResource, "sqliteResource");
	if (type.trim().isEmpty())
	    return true;
	switch(type.trim().toLowerCase())
	{
	case "sqlite":
	    try {
		final InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(sqliteResource);
		if (is == null)
		{
		    Log.error(LOG_COMPONENT, "no resource " + sqliteResource + " needed for sqlite initialization");
		    return false;
		}
		try {
		    final BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		    StringBuilder b = new StringBuilder();
		    String line = reader.readLine();
		    while (line != null)
		    {
			if (line.trim().isEmpty())
			{
			    execSql(con, new String(b));
			    b = new StringBuilder();
			} else
			    b.append(line + " ");
			line = reader.readLine();
		    }
		    execSql(con, new String(b));
		}
		finally {
		    is.close();
		}
		//Enabling wal mode
		final java.sql.ResultSet rs = con.createStatement().executeQuery("PRAGMA journal_mode = WAL;");
		while (rs.next());
		return true;
	    }
	    catch(IOException | SQLException e)
	    {
		Log.error(LOG_COMPONENT, "unable to perform sqlite initialization:" + e.getClass().getName() + ":" + e.getMessage());
		return false;
	    }
	default:
	    Log.error(LOG_COMPONENT, "unknown SQL init procedure type:" + type);
	    return false;
	}
    }

        static private void execSql(Connection con, String query) throws SQLException
    {
	NullCheck.notNull(con, "con");
	NullCheck.notNull(query, "query");
	if (query.trim().isEmpty())
	    return;
	final Statement st = con.createStatement();
	st.executeUpdate(query);
    }

    static public String prepareUrl(Luwrain luwrain, String url)
    {
	NullCheck.notEmpty(url, "url");
	NullCheck.notNull(luwrain, "luwrain");
	String userData = "";
	try {
	    userData = luwrain.getFileProperty("luwrain.dir.userdata").toPath().toUri().toURL().toString();
	    if (userData.startsWith("file:"))
		userData = userData.substring("file:".length());
	}
	catch(java.net.MalformedURLException e)
	{
	    userData = luwrain.getFileProperty("luwrain.dir.userdata").toPath().toString();
	}
	if (userData.length() > 1 && userData.endsWith("/"))
	    userData = userData.substring(0, userData.length() - 1);
	String res = url;
	res = res.replaceAll("\\$userdata", userData);
	return res;
    }
}
