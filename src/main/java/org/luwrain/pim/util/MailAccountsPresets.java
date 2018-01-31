/*
   Copyright 2012-2018 Michael Pozhidaev <michael.pozhidaev@gmail.com>

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

package org.luwrain.pim.util;

import java.util.*;
import java.io.*;

import org.luwrain.core.*;

public final class MailAccountsPresets
{
    static private final String LOG_COMPONENT = "pim";
    static private final String PROPERTIES_PATH = "org/luwrain/pim/presets.mail.properties";

    static public final class Smtp
    {
	public final String title;
	public final String[] suffixes;
	public final String host;
	public final int port;
	public final boolean ssl;
	public final boolean tls;

	Smtp(String title,
	     String[] suffixes,
	     String host,
	     int port,
	     boolean ssl,
	     boolean tls)
	{
	    NullCheck.notNull(title, "title");
	    NullCheck.notNullItems(suffixes, "suffixes");
	    NullCheck.notNull(host, "host");
	    if (port < 0)
		throw new IllegalArgumentException("port (" + port + ") may not be negative");
	    this.title = title;
	    this.suffixes = suffixes;
	    this.host = host;
	    this.port = port;
	    this.ssl = ssl;
	    this.tls = tls;
	}
    }

    public Map<String, Smtp> load() throws IOException
    {
	final Properties prop = new Properties();
	prop.load(this.getClass().getResourceAsStream(PROPERTIES_PATH));
	final Map<String, Smtp> res = new HashMap();
	final String smtpList = prop.getProperty("smtp.list");
	for (String p: smtpList.split(":", -1))
	{
	    if (p.trim().isEmpty())
		continue;
	    final Smtp smtp = loadSmtp(prop, p.trim());
	    if (smtp != null)
		res.put(p.trim(), smtp); else
		Log.error(LOG_COMPONENT, PROPERTIES_PATH + ":error reading SMTP entry \'" + p + "\'");
	}
	return res;
    }

    private Smtp loadSmtp(Properties prop, String name)
    {
	NullCheck.notNull(prop, "prop");
	NullCheck.notEmpty(name, "name");
	final String prefix = "smtp." + name + ".";
	final String title = prop.getProperty(prefix + "title");
	if (title == null || title.trim().isEmpty())
	    return null;
	final String host = prop.getProperty(prefix + "host");
	if (host == null || host.trim().isEmpty())
	    return null;
	final String portStr = prop.getProperty(prefix + "port");
	final String sslStr = prop.getProperty(prop + "ssl");
	final String tlsStr = prop.getProperty(prefix + "tls");
	if (portStr == null || portStr.trim().isEmpty() ||
	    sslStr == null || tlsStr == null)
	    return null;
	final int port;
	try {
	    port = Integer.parseInt(portStr);
	}
	catch(NumberFormatException e)
	{
	    return null;
	}
	final String suffixesStr = prop.getProperty(prefix + "suffixes");
	if (suffixesStr == null)
	    return null;
	final List<String> suffixes = new LinkedList();
	for(String s: suffixesStr.split(":", -1))
	    if (!s.trim().isEmpty())
		suffixes.add(s.trim());
	if (suffixes.isEmpty())
	    return null;
	return new Smtp(title,
			suffixes.toArray(new String[suffixes.size()]),
			host,
			port,
			sslStr.trim().toLowerCase().equals("yes"),
			tlsStr.trim().toLowerCase().equals("yes"));
    }
}
