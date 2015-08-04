/*
   Copyright 2012-2015 Michael Pozhidaev <michael.pozhidaev@gmail.com>

   This file is part of the Luwrain.

   Luwrain is free software; you can redistribute it and/or
   modify it under the terms of the GNU General Public
   License as published by the Free Software Foundation; either
   version 3 of the License, or (at your option) any later version.

   Luwrain is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
   General Public License for more details.
*/

package org.luwrain.pim;

import java.sql.*;

import org.luwrain.core.*;
import org.luwrain.util.RegistryAutoCheck;

import org.luwrain.pim.news.NewsStoring;
import org.luwrain.pim.news.NewsStoringSql;

public class Extension extends org.luwrain.core.extensions.EmptyExtension
{
    private NewsStoring newsStoring;
    private Registry registry;
    private Connection newsJdbcCon;
    private String newsUrl = "";
    private String newsLogin = "";
    private String newsPasswd = "";
    private org.luwrain.pim.binder.Factory binderFactory;
    private org.luwrain.pim.mail.Factory mailFactory;

    @Override public String init(Luwrain luwrain)
    {
	this.registry = luwrain.getRegistry();
	binderFactory = new org.luwrain.pim.binder.Factory(registry);
	mailFactory = new org.luwrain.pim.mail.Factory(registry);
	String res = initDefaultNewsCon();
	if (res != null)
	    return res;
	newsStoring = new NewsStoringSql(registry, newsJdbcCon, newsUrl, newsLogin, newsPasswd);
	return null;
    }

    @Override public SharedObject[] getSharedObjects(Luwrain luwrain)
    {
	final NewsStoring n = newsStoring;
	final org.luwrain.pim.binder.Factory b = binderFactory;
	final org.luwrain.pim.mail.Factory m = mailFactory;

	return new SharedObject[]{
	    new SharedObject(){
		private NewsStoring newsStoring = n;
		@Override public String getName()
		{
		    return "luwrain.pim.news";
		}
		@Override public Object getSharedObject()
		{
		    return newsStoring;
		}
	    },
	    new SharedObject(){
		private org.luwrain.pim.binder.Factory binderFactory = b;
		@Override public String getName()
		{
		    return "luwrain.pim.binder";
		}
		@Override public Object getSharedObject()
		{
		    return binderFactory;
		}
	    },
	    new SharedObject(){
		private org.luwrain.pim.mail.Factory mailFactory = m;
		@Override public String getName()
		{
		    return "luwrain.pim.mail";
		}
		@Override public Object getSharedObject()
		{
		    return mailFactory;
		}
	    },
	};
    }

    private String initDefaultNewsCon()
    {
	RegistryKeys keys = new RegistryKeys();
	RegistryAutoCheck check = new RegistryAutoCheck(registry);
	final String type = check.stringNotEmpty(keys.newsType(), "");
	if (type.isEmpty())
	    return "no proper registry value " + keys.newsType();
	if (!type.equals("jdbc"))
	    return "unknown news storing \'" + type + "\'";
	final String driver = check.stringNotEmpty(keys.newsDriver(), "");
	if (driver.isEmpty())
	    return "no proper value " + keys.newsDriver();
	newsUrl = check.stringNotEmpty(keys.newsUrl(), "");
	if (newsUrl.isEmpty())
	    return "no proper value " + keys.newsUrl();
	newsLogin = check.stringAny(keys.newsLogin(), "");
	newsPasswd = check.stringAny(keys.newsPasswd(), "");
	try {
	    Class.forName (driver).newInstance ();
	    newsJdbcCon = DriverManager.getConnection (newsUrl, newsLogin, newsPasswd);
	}
	catch(Exception e)
	{
	    e.printStackTrace();
	    return "creating news JDBC connection:" + e.getMessage();
	}
	return null;
    }

    @Override public void close()
    {
	if (newsJdbcCon != null)
	{
	    try {
		Statement st = newsJdbcCon.createStatement();
		st.executeUpdate("shutdown");
	    }
	    catch(SQLException e)
	    {
		e.printStackTrace();
	    }
	    newsJdbcCon = null;
	}
    }
}
