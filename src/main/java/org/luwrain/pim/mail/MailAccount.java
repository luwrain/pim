/*
   Copyright 2012-2021 Michael Pozhidaev <msp@luwrain.org>
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

import com.google.gson.annotations.*;
import lombok.*;

import org.luwrain.core.*;
import org.luwrain.pim.*;

@Data
@NoArgsConstructor
public class MailAccount implements Comparable
{
    public enum Type {POP3, SMTP};
    public enum Flags {ENABLED, SSL, TLS, DEFAULT, LEAVE_MESSAGES};

    @SerializedName("type")
    private Type type = Type.POP3;

    @SerializedName("title")
    private String title = "";

    @SerializedName("host")
    private String host = "";

    @SerializedName("port")
    private int port = 995;

    @SerializedName("login")
    private String login = "";

    @SerializedName("passwd")
    private String passwd = "";

    @SerializedName("trusted-hosts")
    private String trustedHosts = "*";

    @SerializedName("flags")
    private Set<Flags> flags = EnumSet.of(Flags.ENABLED, Flags.DEFAULT);

    @SerializedName("subst-name")
    private String substName = "";

    @SerializedName("subst-address")
    private String substAddress = "";

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
