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

package org.luwrain.pim.mail;

import java.util.*;

public class MailAccount implements Comparable
{
    public enum Type {POP3, SMTP};
    public enum Flags {ENABLED, SSL, TLS, DEFAULT, LEAVE_MESSAGES};

    public Type type = Type.POP3;
    public String title = "";
    public String host = "";
    public int port = 995;
    public String login = "";
    public String passwd = "";
    public String trustedHosts = "*";
    public Set<Flags> flags = EnumSet.noneOf(Flags.class);
    public String substName = "";
    public String substAddress = "";

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
