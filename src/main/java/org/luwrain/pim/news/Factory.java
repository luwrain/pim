/*
   Copyright 2012-2019 Michael Pozhidaev <msp@luwrain.org>
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
    static private final String LOG_COMPONENT = "pim-news";
    static private final String SQLITE_INIT_RESOURCE = "org/luwrain/pim/news.sqlite";

    static public final String DEFAULT_TYPE = "jdbc";
    static public final String DEFAULT_DRIVER = "org.sqlite.JDBC";
    static public final String DEFAULT_URL = "jdbc:sqlite:$userdata/sqlite/news.db";
    static public final String DEFAULT_INIT_PROC = "sqlite";

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
	final String type = sett.getType(DEFAULT_TYPE).trim().toLowerCase();
	if (type.isEmpty())
	{
	    Log.error(LOG_COMPONENT, "news storing type may not be empty");
	    return null;
	}
	switch(type)
	{
	case "jdbc":
	    {
	    	final String driver = sett.getDriver(DEFAULT_DRIVER);
		final String url = org.luwrain.pim.SQL.prepareUrl(luwrain, sett.getUrl(DEFAULT_URL));
		if (driver.isEmpty() || url.isEmpty())
		{
		    Log.error(LOG_COMPONENT, "in news storing settings for JDBC the driver and url values may not be empty");
		    return null;
		}
		final String login = sett.getLogin("");
		final String passwd = sett.getPasswd("");
		final String initProc = sett.getInitProc(DEFAULT_INIT_PROC);
		this.con = org.luwrain.pim.SQL.connect(driver, url, login, passwd);
		if (this.con == null)
		    return null;
		if (!org.luwrain.pim.SQL.initProc(con, initProc, SQLITE_INIT_RESOURCE))
		{
		    try {
			this.con.close();
		    }
		    catch(SQLException e)
		    {
		    }
		    this.con = null;
		    return null;
		}
		return new org.luwrain.pim.news.sql.Storing(registry, this.con, execQueues, highPriority);
	    }
	default:
	    Log.error(LOG_COMPONENT, "unknown news storing type \'" + type + "\'");
	    return null;
	}
    }

    public void close()
    {
	execQueues.cancel();
	if (con != null)
	{
	    try {
		con.close();
	    }
	    catch(SQLException e)
	    {
		Log.error(LOG_COMPONENT, "unable to close news JDBC connection normally:" + e.getClass().getName() + ":" + e.getMessage());
	    }
	    con = null;
	}
    }
}
