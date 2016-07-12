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

class StoredMailAccountRegistry extends MailAccount implements StoredMailAccount
{
    private final RegistryKeys registryKeys = new RegistryKeys();
    public Registry registry;
    public int id;

    StoredMailAccountRegistry(Registry registry, int id)
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
	if (!registry.setString(Registry.join(getPath(), "type"), getTypeStr(value)))
	    updateError("type");
	type = value;
    }

    @Override public String getTitle() throws Exception
    {
	return title != null?title:"";
    }

    @Override public void setTitle(String value) throws Exception
    {
	NullCheck.notNull(value, "value");
	if (!registry.setString(Registry.join(getPath(), "title"), value))
	    updateError("title");
	title = value;
    }

    @Override public String getHost() throws Exception
    {
	return host != null?host:"";
    }

    @Override public void setHost(String value) throws Exception
    {
	NullCheck.notNull(value, "value");
	if (!registry.setString(Registry.join(getPath(), "host"), value))
	    updateError("host");
	host = value;
    }

    @Override public int getPort() throws Exception
    {
	return port;
    }

    @Override public void setPort(int value) throws Exception
    {
	if (!registry.setInteger(Registry.join(getPath(), "port"), value))
	    updateError("port");
	port = value;
    }

    @Override public String getLogin() throws Exception
    {
	return login != null?login:"";
    }

    @Override public void setLogin(String value) throws Exception
    {
	NullCheck.notNull(value, "value");
	if (!registry.setString(Registry.join(getPath(), "login"), value))
	    updateError("login");
	login = value;
    }

    @Override public String getPasswd() throws Exception
    {
	return passwd != null?passwd:"";
    }

    @Override public void setPasswd(String value) throws Exception
    {
	NullCheck.notNull(value, "value");
	if (!registry.setString(Registry.join(getPath(), "passwd"), value))
	    updateError("passwd");
	passwd = value;
    }

    @Override public String getTrustedHosts() throws Exception
    {
	return trustedHosts != null?trustedHosts:"";
    }

    @Override public void setTrustedHosts(String value) throws Exception
    {
	NullCheck.notNull(value, "value");
	if (!registry.setString(Registry.join(getPath(), "trusted-hosts"), value))
	    updateError("trusted-hosts");
	trustedHosts = value;
    }


    @Override public int getFlags() throws Exception
    {
	return flags;
    } 

    @Override public void setFlags(int value) throws Exception
    {
	final boolean enabled = (value & FLAG_ENABLED) != 0;
	final boolean ssl = (value & FLAG_SSL) != 0;
	final boolean tls = (value & FLAG_TLS) != 0;
	final boolean def = (value & FLAG_DEFAULT) != 0;
	final boolean leaveMessages = (value & FLAG_LEAVE_MESSAGES) != 0;
	if (!registry.setBoolean(Registry.join(getPath(), "enabled"), enabled))
	    updateError("enabled");
	if (!registry.setBoolean(Registry.join(getPath(), "ssl"), ssl))
	    updateError("ssl");
	if (!registry.setBoolean(Registry.join(getPath(), "tls"), tls))
	    updateError("tls");
	if (!registry.setBoolean(Registry.join(getPath(), "default"), def))
	    updateError("default");
	if (!registry.setBoolean(Registry.join(getPath(), "leave-messages"), leaveMessages))
	    updateError("default");
	flags = value;
    }

    @Override public String getSubstName() throws Exception
    {
	return substName != null?substName:"";
    }

    @Override public void setSubstName(String value) throws Exception
    {
	NullCheck.notNull(value, "value");
	if (!registry.setString(Registry.join(getPath(), "subst-name"), value))
	    updateError("subst-name");
	substName = value;
    }

    @Override public String getSubstAddress() throws Exception
    {
	return substAddress != null?substAddress:"";
    }

    @Override public void setSubstAddress(String value) throws Exception
    {
	NullCheck.notNull(value, "value");
	if (!registry.setString(Registry.join(getPath(), "subst-address"), value))
	    updateError("subst-address");
	substAddress = value;
    }

    boolean load()
    {
	final RegistryAutoCheck check = new RegistryAutoCheck(registry);
	final String path = getPath();
	final String typeStr = check.stringNotEmpty(Registry.join(path, "type"), "").toLowerCase().trim();
	title = check.stringNotEmpty(Registry.join(path, "title"), "");
	host =  check.stringNotEmpty(Registry.join(path, "host"), "");
	port = check.intPositive(Registry.join(path, "port"), -1);
	login = check.stringNotEmpty(Registry.join(path, "login"), "");
	passwd = check.stringNotEmpty(Registry.join(path, "passwd"), "");
	trustedHosts = check.stringNotEmpty(Registry.join(path, "trusted-hosts"), "");
	substName = check.stringNotEmpty(Registry.join(path, "subst-name"), "");
	substAddress = check.stringNotEmpty(Registry.join(path, "subst-address"), "");
	flags = 0;
	if (check.bool(Registry.join(path, "enabled"), false))
	    flags |= FLAG_ENABLED;
	if (check.bool(Registry.join(path, "ssl"), false))
	    flags |= FLAG_SSL;
	if (check.bool(Registry.join(path, "tls"), false))
	    flags |= FLAG_TLS;
	if (check.bool(Registry.join(path, "default"), false))
	    flags |= FLAG_DEFAULT;
	if (check.bool(Registry.join(path, "leave-messages"), true))
	    flags |= FLAG_LEAVE_MESSAGES;
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

    private String getPath()
    {
	return Registry.join(registryKeys.mailAccounts(), "" + id);
    }

    static String getTypeStr(int code)
    {
	switch(code)
	{
	case POP3:
	    return "pop3";
	case SMTP:
	    return "smtp";
	default:
	    return "";
	}
    }

    void updateError(String param) throws Exception
    {
	throw new Exception("Unable to update in the registry " + getPath() + "/" + param);
    }
}
