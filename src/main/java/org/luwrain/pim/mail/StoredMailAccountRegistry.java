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

package org.luwrain.pim.mail;

import org.luwrain.core.Registry;
import org.luwrain.core.NullCheck;
import org.luwrain.util.*;
import org.luwrain.pim.RegistryKeys;

class StoredMailAccountRegistry extends MailAccount implements StoredMailAccount, Comparable
{
    public Registry registry;
    public long id;

    private final RegistryKeys registryKeys = new RegistryKeys();

    StoredMailAccountRegistry(Registry registry, long id)
    {
	this.registry = registry;
	this.id = id;
	NullCheck.notNull(registry, "registry");
    }

    @Override public int getType() throws Exception
    {
	return type;
    }

    @Override public void setType(int value) throws Exception
    {
	//FIXME:
    }

    @Override public String getTitle() throws Exception
    {
	return title;
    }

    @Override public void setTitle(String value) throws Exception
    {
	//FIXME:
    }

    @Override public String getHost() throws Exception
    {
	return host;
    }

    @Override public void setHost(String value) throws Exception
    {
	//FIXME:
    }

    @Override public int getPort() throws Exception
    {
	return port;
    }

    @Override public void setPort(int value) throws Exception
    {
	//FIXME:
    }

    @Override public String getLogin() throws Exception
    {
	return login;
    }

    @Override public void setLogin(String value) throws Exception
    {
	//FIXME:
    }

    @Override public String getPasswd() throws Exception
    {
	return passwd;
    }

    @Override public void setPasswd(String value) throws Exception
    {
	//FIXME:
    }

    @Override public int getFlags() throws Exception
    {
	return flags;
    } 

    @Override public void setFlags(int value) throws Exception
    {
	//FIXME:
    }

    @Override public String getSubstName() throws Exception
    {
	return substName;
    }

    @Override public void setSubstName(String value) throws Exception
    {
	//FIXME:
    }

    @Override public String getSubstAddress() throws Exception
    {
	return substAddress;
    }

    @Override public void setSubstAddress(String value) throws Exception
    {
	//FIXME:
    }

    boolean load()
    {
	final RegistryAutoCheck check = new RegistryAutoCheck(registry);
	final String path = RegistryPath.join(registryKeys.mailAccounts(), "" + id);
	final String typeStr = check.stringNotEmpty(RegistryPath.join(path, "type"), "").toLowerCase().trim();
	title = check.stringNotEmpty(RegistryPath.join(path, "title"), "");
	host =  check.stringNotEmpty(RegistryPath.join(path, "host"), "");
	port = check.intPositive(RegistryPath.join(path, "port"), -1);
	login = check.stringNotEmpty(RegistryPath.join(path, "login"), "");
	passwd = check.stringNotEmpty(RegistryPath.join(path, "passwd"), "");
	substName = check.stringNotEmpty(RegistryPath.join(path, "subst-name"), "");
	substAddress = check.stringNotEmpty(RegistryPath.join(path, "subst-address"), "");
	flags = 0;
	if (check.bool(RegistryPath.join(path, "ssl"), false))
	    flags |= FLAG_SSL;
	if (check.bool(RegistryPath.join(path, "tls"), false))
	    flags |= FLAG_TLS;
	switch(typeStr)
	{
	case "pop3":
	    type = POP3;
	    break;
	case "smtp":
	    type = SMTP;
	    break;
	default:
	    return false;
	}
	if (title.isEmpty())
	    return false;
	if (port < 0)
	    port = 0;
	return true;
    }

    @Override public String toString()
    {
	return title;
    }

    @Override public int compareTo(Object o)
    {
	if (o == null || !(o instanceof StoredMailAccountRegistry))
	    return 0;
	final StoredMailAccountRegistry account = (StoredMailAccountRegistry)o;
	return title.compareTo(account.title);
    }
}
