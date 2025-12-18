// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.pim.binder;

import java.sql.*;

import org.luwrain.core.Registry;
//import org.luwrain.util.RegistryAutoCheck;

public class Factory
{
    private Registry registry;

    public Factory(Registry registry)
    {
	this.registry = registry;
	if (registry == null)
	    throw new NullPointerException("registry may not be null");
    }

    public BinderStoring createBinderStoring()
    {
	/*
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
	*/
	return null;
    }
}
