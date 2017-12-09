/*
   Copyright 2012-2017 Michael Pozhidaev <michael.pozhidaev@gmail.com>
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

import java.sql.*;

import org.luwrain.core.*;

public class SQL
{
    static private final String LOG_COMPONENT = "pim";

    static public Connection connect(String driver, String url, String login, String passwd, String initProc)
    {
	NullCheck.notEmpty(driver, "driver");
	NullCheck.notEmpty(url, "url");
	NullCheck.notNull(login, "login");
	NullCheck.notNull(passwd, "passwd");
	NullCheck.notNull(initProc, "initProc");
	try {
	    Class.forName (driver).newInstance ();
	    final Connection con = DriverManager.getConnection (url, login, passwd);
	    if (con == null)
		return con;
	    switch(initProc.toLowerCase().trim())
	    {
	    case "sqlite-wal":
		{
		    final java.sql.ResultSet rs = con.createStatement().executeQuery("PRAGMA journal_mode = WAL;");
		    while (rs.next());
		}
	    }
	    return con;
	}
	catch(Throwable e)
	{
	    Log.error(LOG_COMPONENT, "unable to get JDBC connection with the driver " + driver + " for " + url + ":" + e.getClass().getName() + ":" + e.getMessage());
	    return null;
	}
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
