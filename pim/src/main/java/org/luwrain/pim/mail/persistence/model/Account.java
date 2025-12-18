// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>
// Copyright 2015 Roman Volovodov <gr.rPman@gmail.com>

package org.luwrain.pim.mail.persistence.model;

import java.util.*;
import java.io.*;
import lombok.*;

@Data
public class Account implements Serializable
{
    public enum Type {POP3, SMTP};

    private int id;
        private Type type = Type.POP3;

    private String
	name, host, login, passwd,
	trustedHosts, substName, substAddress;

    private int port = 995;
    private boolean enabled, tls, ssl, defaultAccount, leaveMessages;

    @Override public boolean equals(Object o)
    {
	if (o != null && o instanceof Account a)
	    return id == a.id;
	return false;
    }

    @Override public int hashCode()
    {
	return id;
    }

    @Override public String toString()    
    {
	final var b = new StringBuilder();
	if (type != null)
	    b.append("type=").append(type.toString()).append(", ");
	if (name != null)
	    b.append("name=").append(name).append(", ");
	if (login != null)
	    b.append("login=").append(login).append(", ");
	if (passwd != null)
	    b.append("password=").append(passwd.length()).append(" chars, ");
	if (host != null)
	    b.append("host=").append(host).append(", ");
	b.append("port=").append(port).append(", ");
		if (substAddress != null)
	    b.append("substAddress=").append(substAddress).append(", ");
	if (substName != null)
	    b.append("substName=").append(substName).append(", ");
	b.append("tls=").append(tls).append(", ");
	b.append("ssl=").append(ssl).append(", ");
	b.append("enabled=").append(enabled).append(", ");
	b.append("defaultAccount=").append(defaultAccount).append(", ");
	b.append("leaveMessages=").append(leaveMessages).append(", ");
	b.append("id=").append(id);
	return new String(b);
    }
}
