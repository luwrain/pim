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

import java.util.Properties;
import java.net.URL;
import java.io.IOException;

public class RegistryKeys
{
    private static final String REGISTRY_KEYS_RESOURCE = "org/luwrain/pim/registry-keys.properties";

    private Properties properties;

    public RegistryKeys()
    {
	URL url = ClassLoader.getSystemResource(REGISTRY_KEYS_RESOURCE);
	if (url == null)
	    return;
	properties = new Properties();
	try {
	    properties.load(url.openStream());
	}
	catch (IOException e)
	{
	    e.printStackTrace();
	}
    }

    public String contactsType()
    {
	return getProperty("contacts.Type");
    }

    public String contactsDriver()
    {
	return getProperty("contacts.Driver");
    }

    public String contactsUrl()
    {
	return getProperty("contacts.URL");
    }

    public String contactsLogin()
    {
	return getProperty("contacts.Login");
    }

    public String contactsPasswd()
    {
	return getProperty("contacts.Passwd");
    }

    public String contactsFolders()
    {
	return getProperty("contacts.Folders");
    }

    public String mailType()
    {
	return getProperty("mail.Type");
    }

    public String mailDriver()
    {
	return getProperty("mail.Driver");
    }

    public String mailUrl()
    {
	return getProperty("mail.URL");
    }

    public String mailLogin()
    {
	return getProperty("mail.Login");
    }

    public String mailPasswd()
    {
	return getProperty("mail.Passwd");
    }

    public String mailFolders()
    {
	return getProperty("mail.Folders");
    }

    public String newsType()
    {
	return getProperty("news.type");
    }

    public String newsDriver()
    {
	return getProperty("news.driver");
    }

    public String newsUrl()
    {
	return getProperty("news.url");
    }

    public String newsLogin()
    {
	return getProperty("news.login");
    }

    public String newsPasswd()
    {
	return getProperty("news.passwd");
    }

    public String newsGroups()
    {
	return getProperty("news.groups");
    }

    private String getProperty(String name)
    {
	if (name == null)
	    throw new NullPointerException("name may not be null");
	if (name.isEmpty())
	    throw new IllegalArgumentException("name may not be empty");
	if (properties == null)
	    throw new NullPointerException("properties object is null");
	final String value = properties.getProperty(name);
	if (value == null)
	    throw new NullPointerException("property value is null");
	return value;
    }
};
