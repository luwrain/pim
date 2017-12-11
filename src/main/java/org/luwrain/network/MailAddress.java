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

package org.luwrain.network;

import java.util.*;
import java.io.*;

import javax.mail.*;
import javax.mail.internet.*;

import org.luwrain.core.*;

public class MailAddress
{
    static public String makeEncodedAddress(String name, String addr) throws UnsupportedEncodingException
    {
	NullCheck.notNull(name, "name");
	NullCheck.notNull(addr, "addr");
	if (name.trim().isEmpty())
	    return addr;
	return MimeUtility.encodeText(name) + " <" + addr + ">";
    }

    static public String[] decodeAddrs(Address[] addrs) throws UnsupportedEncodingException
    {
	if (addrs == null)
	    return new String[0];
	final LinkedList<String> res=new LinkedList<String>();
	for(int i = 0;i < addrs.length;++i)
	    if (addrs[i] != null)
		res.add(MimeUtility.decodeText(addrs[i].toString()));
	return res.toArray(new String[res.size()]);
    }

    static public InternetAddress[] makeInternetAddrs(String[] addrs) throws AddressException
    {
	NullCheck.notNullItems(addrs, "addrs");
	final InternetAddress[] res =new InternetAddress[addrs.length];
	for(int i = 0;i < addrs.length;++i)
	    res[i] = new InternetAddress(addrs[i]);
	return res;
    }
}
