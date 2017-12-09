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

package org.luwrain.pim.news;

import java.sql.*;

import org.luwrain.core.*;
import org.luwrain.pim.util.*;

public final class Factory
{
    private final Luwrain luwrain;
    private final Registry registry;
    private final Settings.Storing sett;
    private final ExecQueues execQueues = new ExecQueues();
    private Connection con = null;

    public Factory(Luwrain luwrain)
    {
	NullCheck.notNull(luwrain, "luwrain");
	this.luwrain = luwrain;
	this.registry = luwrain.getRegistry();
	this.sett = Settings.createStoring(registry);
	this.execQueues.start();
    }

    public NewsStoring newNewsStoring(boolean highPriority)
    {
	if (con != null)
	    return new org.luwrain.pim.news.sql.Storing(registry, con, execQueues, highPriority);
	final String type = sett.getType("");
	if (type.trim().isEmpty())
	{
	    Log.error("pim", "news storing type may not be empty");
	    return null;
	}
	if (!type.equals("jdbc"))
	{
	    Log.error("pim", "unknown storing type \'" + sett.getType("") + "\' for news");
	    return null;
	}
	final String driver = sett.getDriver("");
	final String url = org.luwrain.pim.SQL.prepareUrl(luwrain, sett.getUrl(""));
	final String login = sett.getLogin("");
	final String passwd = sett.getPasswd("");
	if (driver.isEmpty() || url.isEmpty())
	{
	    Log.error("pim", "driver and url may not be empty in news storing settings");
	    return null;
	}
	try {
	    Class.forName (driver).newInstance ();
con = DriverManager.getConnection (url, login, passwd);
if (sett.getInitProc("").toLowerCase().equals("sqlite-wal"))
{
    final java.sql.ResultSet rs = con.createStatement().executeQuery("PRAGMA journal_mode = WAL;");
    while (rs.next());
}
return new org.luwrain.pim.news.sql.Storing(registry, con, execQueues, highPriority);
	}
	catch(ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException e)
	{
	    Log.error("pim", "unable to get JDBC connection for news:" + e.getClass().getName() + ":" + e.getMessage());
	    e.printStackTrace();
	    return null;
	}
    }

    public void close()
    {
	execQueues.cancel();
	if (con == null)
	    return;
	try {
    con.close();
    }
    catch(SQLException e)
    {
	    Log.error("pim", "unable to close mail JDBC connection normally:" + e.getMessage());
	    e.printStackTrace();
    }
	con = null;
    }
}
