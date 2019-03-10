/*
   Copyright 2012-2019 Michael Pozhidaev <michael.pozhidaev@gmail.com>
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

package org.luwrain.pim.mail.sql;

import java.util.*;

import org.luwrain.core.*;
import org.luwrain.pim.*;
import org.luwrain.pim.mail.*;

class Account extends MailAccount implements StoredMailAccount
{
private final Registry registry;
    private final org.luwrain.pim.mail.Settings.Account settings;
    final int id;

    Account(Registry registry, int id)
    {
	NullCheck.notNull(registry, "registry");
	this.registry = registry;
	this.id = id;
	this.settings = org.luwrain.pim.mail.Settings.createAccount(registry, Registry.join(org.luwrain.pim.mail.Settings.ACCOUNTS_PATH, "" + id));
    }

    @Override public Type getType()
    {
	return type;
    }

    @Override public void setType(Type value)
    {
	NullCheck.notNull(value, "value");
	settings.setType(getTypeStr(value));
	type = value;
    }

    @Override public String getTitle()
    {
	return title;
    }

    @Override public void setTitle(String value)
    {
	NullCheck.notNull(value, "value");
	settings.setTitle(value);
	title = value;
    }

    @Override public String getHost()
    {
	return host;
    }

    @Override public void setHost(String value)
    {
	NullCheck.notNull(value, "value");
	settings.setHost(value);
	host = value;
    }

    @Override public int getPort()
    {
	return port;
    }

    @Override public void setPort(int value)
    {
	settings.setPort(value);
	port = value;
    }

    @Override public String getLogin()
    {
	return login;
    }

    @Override public void setLogin(String value)
    {
	NullCheck.notNull(value, "value");
	settings.setLogin(value);
	login = value;
    }

    @Override public String getPasswd()
    {
	return passwd;
    }

    @Override public void setPasswd(String value)
    {
	NullCheck.notNull(value, "value");
	settings.setPasswd(value);
	passwd = value;
    }

    @Override public String getTrustedHosts()
    {
	return trustedHosts;
    }

    @Override public void setTrustedHosts(String value)
    {
	NullCheck.notNull(value, "value");
	settings.setTrustedHosts(value);
	trustedHosts = value;
    }

    @Override public Set<Flags> getFlags()
    {
	return flags;
    } 

    @Override public void setFlags(Set<Flags> value)
    {
	NullCheck.notNull(value, "value");
	final boolean enabled = value.contains(Flags.ENABLED);
	final boolean ssl =  value.contains(Flags.SSL);
	final boolean tls =  value.contains(Flags.TLS);
	final boolean def =  value.contains(Flags.DEFAULT);
	final boolean leaveMessages =  value.contains(Flags.LEAVE_MESSAGES);
	settings.setEnabled(enabled);
	settings.setSsl(ssl);
	settings.setTls(tls);
	settings.setDefault(def);
	settings.setLeaveMessages(leaveMessages);
	flags = value;
    }

    @Override public String getSubstName()
    {
	return substName;
    }

    @Override public void setSubstName(String value)
    {
	NullCheck.notNull(value, "value");
	settings.setSubstName(value);
	substName = value;
    }

    @Override public String getSubstAddress()
    {
	return substAddress;
    }

    @Override public void setSubstAddress(String value)
    {
	NullCheck.notNull(value, "value");
	settings.setSubstAddress(value);
	substAddress = value;
    }

    boolean load()
    {
	final String typeStr = settings.getType("").trim().toLowerCase();
	title = settings.getTitle("");
	host = settings.getHost("").trim();
	port = settings.getPort(port);
	login = settings.getLogin("");
	passwd = settings.getPasswd("");
	trustedHosts = settings.getTrustedHosts("");
	substName = settings.getSubstName("");
	substAddress = settings.getSubstAddress("");
	final LinkedList<Flags> f = new LinkedList<Flags>();
	if (settings.getEnabled(true))
	    f.add(Flags.ENABLED);
	if (settings.getSsl(false))
	    f.add(Flags.SSL);
	if (settings.getTls(false))
	    f.add(Flags.TLS);
	if (settings.getDefault(true))
	    f.add(Flags.DEFAULT);
	if (settings.getLeaveMessages(true))
	    f.add(Flags.LEAVE_MESSAGES);
	flags = EnumSet.copyOf(f);
	switch(typeStr)
	{
	case "pop3":
	    type = Type.POP3;
	    break;
	case "smtp":
	    type = Type.SMTP;
	    break;
	default:
	    return false;
	}
	Log.debug("pim", "mail group \"" + title + "\" loaded, type " + type + ", flags " + flags);
	return true;
    }

    static String getTypeStr(Type type)
    {
	NullCheck.notNull(type, "type");
	switch(type)
	{
	case POP3:
	    return "pop3";
	case SMTP:
	    return "smtp";
	default:
	    return "";
	}
    }
}
