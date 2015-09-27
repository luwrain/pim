/*
   Copyright 2012-2015 Michael Pozhidaev <michael.pozhidaev@gmail.com>
   Copyright 2015 Roman Volovodov <gr.rPman@gmail.com>

   This file is part of the LUWRAIN.

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

public class MailAccount implements Comparable
{
    static public final int POP3 = 0;
    static public final int SMTP = 1;

    static public final int FLAG_SSL = 1;
    static public final int FLAG_TLS = 2;
    static public final int FLAG_DEFAULT = 4;

    public int type = POP3;
    public String title = "";
    public String host = "";
    public int port = 1;
    public String login = "";
    public String passwd = "";
    public int flags = 0;
    public String substName = "";
    public String substAddress = "";

    @Override public int compareTo(Object o)
    {
	if (o == null || !(o instanceof MailAccount))
	    return 0;
	final MailAccount account = (MailAccount)o;
	return title.compareTo(account.title);
    }

    @Override public boolean equals(Object o)
    {
	if (o == null || !(o instanceof MailAccount))
	    return false;
	final MailAccount account = (MailAccount)o;
	return title.equals(account.title);
    }

    @Override public String toString()    
    {
	return title != null?title:"";
    }
}
