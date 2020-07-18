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

import com.google.gson.annotations.*;

import org.luwrain.core.*;
import org.luwrain.pim.*;
import org.luwrain.pim.mail.*;

final class Account extends MailAccount
{
    transient Accounts accounts = null;

    @SerializedName("id")
    int id = 0;

    @Override public void setType(Type type) throws PimException
    {
	NullCheck.notNull(type, "type");
	super.setType(type);
	accounts.saveAll();
    }

    @Override public void setTitle(String title) throws PimException
    {
	NullCheck.notNull(title, "title");
	super.setTitle(title);
	accounts.saveAll();
    }

    @Override public void setHost(String host) throws PimException
    {
	NullCheck.notNull(host, "host");
	super.setHost(host);
	accounts.saveAll();
    }

    @Override public void setPort(int port) throws PimException
    {
	if (port < 0)
	    throw new IllegalArgumentException("port (" + String.valueOf(port) + ") may not be negative");
	super.setPort(port);
	accounts.saveAll();
    }

    @Override public void setLogin(String login) throws PimException
    {
	NullCheck.notNull(login, "login");
	super.setLogin(login);
	accounts.saveAll();
    }

    @Override public void setPasswd(String passwd) throws PimException
    {
	NullCheck.notNull(passwd, "passwd");
	super.setPasswd(passwd);
	accounts.saveAll();
    }

    @Override public void setTrustedHosts(String trustedHosts) throws PimException
    {
	NullCheck.notNull(trustedHosts, "trustedHosts");
	super.setTrustedHosts(trustedHosts);
	accounts.saveAll();
    }

    @Override public void setFlags(Set<Flags> flags) throws PimException
    {
	NullCheck.notNull(flags, "flags");
	super.setFlags(flags);
	accounts.saveAll();
    }

    @Override public void setSubstName(String substName) throws PimException
    {
	NullCheck.notNull(substName, "substName");
	super.setSubstName(substName);
	accounts.saveAll();
    }

    @Override public void setSubstAddress(String substAddress) throws PimException
    {
	NullCheck.notNull(substAddress, "substAddress");
	super.setSubstAddress(substAddress);
	accounts.saveAll();
    }
}
