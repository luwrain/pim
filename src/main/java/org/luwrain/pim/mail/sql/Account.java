/*
   Copyright 2012-2020 Michael Pozhidaev <msp@luwrain.org>
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

    final int id;

    Account(int id, Accounts accounts)
    {
	NullCheck.notNull(accounts, "accounts");
	this.id = id;
    }

    @Override public void setType(Type type) throws PimException
    {
	NullCheck.notNull(type, "type");
	//	sett.setType(getTypeStr(type));
	super.setType(type);
    }

    @Override public void setTitle(String title) throws PimException
    {
	NullCheck.notNull(title, "title");
	//	sett.setTitle(title);
	super.setTitle(title);
    }

    @Override public void setHost(String host) throws PimException
    {
	NullCheck.notNull(host, "host");
	//	sett.setHost(host);
	super.setHost(host);
    }

    @Override public void setPort(int port) throws PimException
    {
	if (port < 0)
	    throw new IllegalArgumentException("port (" + String.valueOf(port) + ") may not be negative");
	//	sett.setPort(port);
	super.setPort(port);
    }

    @Override public void setLogin(String login) throws PimException
    {
	NullCheck.notNull(login, "login");
	//	sett.setLogin(login);
	super.setLogin(login);
    }

    @Override public void setPasswd(String passwd) throws PimException
    {
	NullCheck.notNull(passwd, "passwd");
	//	sett.setPasswd(passwd);
	super.setPasswd(passwd);
    }

    @Override public void setTrustedHosts(String trustedHosts) throws PimException
    {
	NullCheck.notNull(trustedHosts, "trustedHosts");
	//	sett.setTrustedHosts(trustedHosts);
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
	//	sett.setEnabled(enabled);
	//	sett.setSsl(ssl);
	//	sett.setTls(tls);
	//	sett.setDefault(def);
	//	sett.setLeaveMessages(leaveMessages);
	super.setFlags(flags);
    }

    @Override public void setSubstName(String substName) throws PimException
    {
	NullCheck.notNull(substName, "substName");
	//	sett.setSubstName(substName);
	super.setSubstName(substName);
    }

    @Override public void setSubstAddress(String substAddress) throws PimException
    {
	NullCheck.notNull(substAddress, "substAddress");
	//	sett.setSubstAddress(substAddress);
	super.setSubstAddress(substAddress);
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
