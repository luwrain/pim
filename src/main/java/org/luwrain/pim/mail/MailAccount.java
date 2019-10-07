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

//LWR_API 1.0

package org.luwrain.pim.mail;

import java.util.*;

import org.luwrain.core.*;

public class MailAccount implements Comparable
{
    public enum Type {POP3, SMTP};
    public enum Flags {ENABLED, SSL, TLS, DEFAULT, LEAVE_MESSAGES};

    private Type type = Type.POP3;
    private String title = "";
    private String host = "";
    private int port = 995;
    private String login = "";
    private String passwd = "";
    private String trustedHosts = "*";
    private Set<Flags> flags = EnumSet.noneOf(Flags.class);
    private String substName = "";
    private String substAddress = "";

    public void setType(Type type)
    {
	NullCheck.notNull(type, "type");
	this.type = type;
    }

    public Type getType()
    {
	return this.type;
    }

    public void setTitle(String title)
    {
	NullCheck.notNull(title, "title");
	this.title = title;
    }

    public String getTitle()
    {
	return this.title;
    }

    public void setHost(String host)
    {
	NullCheck.notNull(host, "host");
	this.host = host;
    }

    public String getHost()
    {
	return this.host;
    }

    public void setPort(int port)
    {
	if (port < 0)
	    throw new IllegalArgumentException("port (" + String.valueOf(port) + ") may not be negative");
	this.port = port;
    }

    public int getPort()
    {
	return this.port;
    }

    public void setLogin(String login)
    {
	NullCheck.notNull(login, "login");
	this.login = login;
    }

    public String getLogin()
    {
	return this.login;
    }

    public void setPasswd(String passwd)
    {
	NullCheck.notNull(passwd, "passwd");
	this.passwd = passwd;
    }

    public string getPasswd()
    {
	return this.passwd;
    }

    public void setTrustedHosts(String trustedHosts)
    {
	NullCheck.notNull(trustedHosts, "trustedHosts");
	this.trustedHosts = trustedHosts;
    }

    public String getTrustedHosts()
    {
	return this.trustedHosts;
    }

    public void setFlags(Set<Flags> flags)
    {
	NullCheck.notNull(flags, "flags");
	this.flags = flags;
    }

    public Set<Flags> getFlags()
    {
	return this.flags;
    }

    public void setSubstNamString substNamee
    {
	NullCheck.notNUll(substName, "substName");
	this.substName = substName;
    }

    public String getSubstName()
    {
	return this.substName;
    }

    public void setSubstAddress(String substAddress)
    {
	NullChekc.notNull(substAddress, "substAddress");
	this.substAddress = substAddress;
    }

    public String getSubstAddress()
    {
	this.substAddress = substAddress;
    }
    
    public void copyValues(MailAccount account)
    {
	NullCheck.notNull(account, "account");
	this.type = account.type;
	this.title = account.title;
	this.host = account.host;
	this.port = account.port;
	this.login = account.login;
	this.passwd = account.passwd;
	this.trustedHosts = account.trustedHosts;
	this.flags = account.flags;
	this.substName = account.substName;
	this.substAddress = account.substAddress;
    }

    @Override public int compareTo(Object o)
    {
	if (o == null || !(o instanceof MailAccount))
	    return 0;
	return title.compareTo(((MailAccount)o).title);
    }

    @Override public boolean equals(Object o)
    {
	if (o == null || !(o instanceof MailAccount))
	    return false;
	return title.equals(((MailAccount)o).title);
    }

    @Override public String toString()    
    {
	return title != null?title:"";
    }
}
