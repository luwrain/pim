/*
   Copyright 2012-2017 Michael Pozhidaev <michael.pozhidaev@gmail.com>
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

package org.luwrain.pim.mail.mem;

import java.util.*;

import org.luwrain.core.*;
import org.luwrain.pim.*;
import org.luwrain.pim.mail.*;

class Account extends MailAccount implements StoredMailAccount
{
    final int id;

    Account(int id)
    {
	if (id < 0)
	    throw new IllegalArgumentException("id (" + id + ") may not be negative");
	this.id = id;
    }

    @Override public Type getType()
    {
	return type;
    }

    @Override public void setType(Type value)
    {
	NullCheck.notNull(value, "value");
	type = value;
    }

    @Override public String getTitle()
    {
	return title;
    }

    @Override public void setTitle(String value)
    {
	NullCheck.notNull(value, "value");
	title = value;
    }

    @Override public String getHost()
    {
	return host;
    }

    @Override public void setHost(String value)
    {
	NullCheck.notNull(value, "value");
	host = value;
    }

    @Override public int getPort()
    {
	return port;
    }

    @Override public void setPort(int value)
    {
	if (value < 0)
	    throw new IllegalArgumentException("value (" + value + " may not be negative");
	port = value;
    }

    @Override public String getLogin()
    {
	return login;
    }

    @Override public void setLogin(String value)
    {
	NullCheck.notNull(value, "value");
	this.login = login;
    }

    @Override public String getPasswd()
    {
	return passwd;
    }

        @Override public void setPasswd(String value)
    {
	NullCheck.notNull(value, "value");
	passwd = value;
    }

    @Override public String getTrustedHosts()
    {
	return trustedHosts;
    }

    @Override public void setTrustedHosts(String value)
    {
	NullCheck.notNull(value, "value");
	trustedHosts = value;
    }

    @Override public Set<Flags> getFlags()
    {
	return flags;
    } 

    @Override public void setFlags(Set<Flags> value)
    {
	NullCheck.notNull(value, "value");
	flags = value;
    }

    @Override public String getSubstName()
    {
	return substName;
    }

    @Override public void setSubstName(String value)
    {
	NullCheck.notNull(value, "value");
	substName = value;
    }

    @Override public String getSubstAddress()
    {
	return substAddress;
    }

    @Override public void setSubstAddress(String value)
    {
	NullCheck.notNull(value, "value");
	substAddress = value;
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
