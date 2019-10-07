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

package org.luwrain.pim.mail.sql;

import java.util.*;

import org.luwrain.core.*;
import org.luwrain.pim.*;
import org.luwrain.pim.mail.*;

final class Account extends MailAccount
{
    static private final String LOG_COMPONENT = "pim-mail";//FIXME:
    
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

    @Override public void setType(Type type) throws PimException
    {
	NullCheck.notNull(type, "type");
	settings.setType(getTypeStr(type));
	super.setType(type);
    }

    @Override public void setTitle(String title) throws PimException
    {
	NullCheck.notNull(title, "title");
	settings.setTitle(title);
	super.setTitle(title);
    }

    @Override public void setHost(String host) throws PimException
    {
	NullCheck.notNull(host, "host");
	settings.setHost(host);
	super.setHost(host);
    }

    @Override public void setPort(int port) throws PimException
    {
	if (port < 0)
	    throw new IllegalArgumentException("port (" + String.valueOf(port) + ") may not be negative");
	settings.setPort(port);
	super.setPort(port);
    }

    @Override public void setLogin(String login) throws PimException
    {
	NullCheck.notNull(login, "login");
	settings.setLogin(login);
	super.setLogin(login);
    }

    @Override public void setPasswd(String passwd) throws PimException
    {
	NullCheck.notNull(passwd, "passwd");
	settings.setPasswd(passwd);
	super.setPasswd(passwd);
    }

    @Override public void setTrustedHosts(String trustedHosts) throws PimException
    {
	NullCheck.notNull(trustedHosts, "trustedHosts");
	settings.setTrustedHosts(trustedHosts);
	super.setTrustedHosts(trustedHosts);
    }

    @Override public void setFlags(Set<Flags> flags) throws PimException
    {
	NullCheck.notNull(flags, "flags");
	final boolean enabled = flags.contains(Flags.ENABLED);
	final boolean ssl = flags.contains(Flags.SSL);
	final boolean tls =  flags.contains(Flags.TLS);
	final boolean def =  flags.contains(Flags.DEFAULT);
	final boolean leaveMessages =  flags.contains(Flags.LEAVE_MESSAGES);
	settings.setEnabled(enabled);
	settings.setSsl(ssl);
	settings.setTls(tls);
	settings.setDefault(def);
	settings.setLeaveMessages(leaveMessages);
	super.setFlags(flags);
    }

    @Override public void setSubstName(String substName) throws PimException
    {
	NullCheck.notNull(substName, "substName");
	settings.setSubstName(substName);
	super.setSubstName(substName);
    }

    @Override public void setSubstAddress(String substAddress) throws PimException
    {
	NullCheck.notNull(substAddress, "substAddress");
	settings.setSubstAddress(substAddress);
	super.setSubstAddress(substAddress);
    }

    boolean load()
    {
	try {
	    final String typeStr = settings.getType("").trim().toLowerCase();
	    super.setTitle(settings.getTitle(""));
	    super.setHost(settings.getHost("").trim());
	    super.setPort(settings.getPort(getPort()));
	    super.setLogin(settings.getLogin(""));
	    super.setPasswd(settings.getPasswd(""));
	    super.setTrustedHosts(settings.getTrustedHosts(""));
	    super.setSubstName(settings.getSubstName(""));
	    super.setSubstAddress(settings.getSubstAddress(""));
	    final List<Flags> f = new LinkedList();
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
	    super.setFlags(EnumSet.copyOf(f));
	    switch(typeStr)
	    {
	    case "pop3":
		super.setType(Type.POP3);
		break;
	    case "smtp":
		super.setType(Type.SMTP);
		break;
	    default:
		return false;
	    }
	    Log.debug(LOG_COMPONENT, "mail account \'" + getTitle() + "\' loaded, type " + getType() + ", flags " + getFlags());
	    return true;
	}
	catch(PimException e)
	{
	    Log.warning(LOG_COMPONENT, "unable to load the mail account with ID=" + String.valueOf(id) + ": "+ e.getClass().getName() + ":" + e.getMessage());
	    return false;
	}
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
