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

package org.luwrain.pim.mail2.persistence.model;

import java.util.*;

import lombok.*;
import jakarta.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table (name = "account")
public class Account
{
    public enum Type {POP3, SMTP};

        @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
        private Type type = Type.POP3;

    private String
	name, host, login, passwd,
	trustedHosts, substName,
	substAddress;

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
	return name != null?name:"";
    }
}
