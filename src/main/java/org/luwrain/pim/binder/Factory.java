/*
   Copyright 2012-2015 Michael Pozhidaev <michael.pozhidaev@gmail.com>
   Copyright 2015 Roman Volovodov <gr.rPman@gmail.com>

   This file is part of the LUWRAIN.

   LUWRAIN is free software; you can redistribute it and/or
   modify it under the terms of the GNU General Public
   License as published by the Free Software Foundation; either
   version 3 of the License, or (at your option) any later version.

   LUWRAIN is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
   General Public License for more details.
*/

package org.luwrain.pim.binder;

import java.sql.*;

import org.luwrain.pim.RegistryKeys;
import org.luwrain.core.Registry;
import org.luwrain.util.RegistryAutoCheck;

public class Factory
{
    private Registry registry;
    private RegistryKeys registryKeys = new RegistryKeys();

    public Factory(Registry registry)
    {
	this.registry = registry;
	if (registry == null)
	    throw new NullPointerException("registry may not be null");
    }

    public BinderStoring createBinderStoring()
    {
	RegistryAutoCheck check = new RegistryAutoCheck(registry);
	final String type = check.stringNotEmpty(registryKeys.binderType(), "");
	if (!type.equals("jdbc"))
	    return null;
	final String driver = check.stringNotEmpty(registryKeys.binderDriver(), "");
	final String url = check.stringNotEmpty(registryKeys.binderUrl(), "");
	final String login = check.stringAny(registryKeys.binderLogin(), "");
	final String passwd = check.stringAny(registryKeys.binderPasswd(), "");
	if (driver.isEmpty() || url.isEmpty())
	    return null;
	try {
	    Class.forName (driver).newInstance ();
	    return new BinderStoringSql(DriverManager.getConnection (url, login, passwd));
	}
	catch(Exception e)
	{
	    e.printStackTrace();
	    return null;
	}
    }
}
