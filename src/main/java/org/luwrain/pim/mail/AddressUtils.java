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
import java.io.*;

import javax.mail.*;
import javax.mail.internet.*;

import org.luwrain.core.*;

class AddressUtils
{
    static String getPersonal(String addr)
    {
	NullCheck.notNull(addr, "addr");
	if (addr.trim().isEmpty())
	    return "";
	try {
	    final javax.mail.internet.InternetAddress inetAddr = new javax.mail.internet.InternetAddress(addr, false);
	    final String personal = inetAddr.getPersonal();
	    return personal != null?personal.trim():"";
	}
	catch (javax.mail.internet.AddressException e)
	{
	    return "";
	}
    }

        static String getAddress(String addr)
    {
	NullCheck.notNull(addr, "addr");
	if (addr.trim().isEmpty())
	    return "";
	try {
	    final javax.mail.internet.InternetAddress inetAddr = new javax.mail.internet.InternetAddress(addr, false);
	    final String address = inetAddr.getAddress();
	    return address != null?address.trim():addr;
	}
	catch (javax.mail.internet.AddressException e)
	{
	    return addr;
	}
    }

    static public String combinePersonalAndAddress(String personal, String addr)
    {
	NullCheck.notNull(personal, "personal");
	NullCheck.notNull(addr, "addr");
	if (personal.trim().isEmpty())
	    return addr.trim();
	return personal.trim() + " <" + addr.trim() + ">";
    }

    static InternetAddress[] makeInternetAddrs(String[] addrs) throws AddressException
    {
	NullCheck.notNullItems(addrs, "addrs");
	final InternetAddress[] res =new InternetAddress[addrs.length];
	for(int i = 0;i < addrs.length;++i)
	    res[i] = new InternetAddress(addrs[i]);
	return res;
    }
}
