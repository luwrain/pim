/*
   Copyright 2012-2024 Michael Pozhidaev <msp@luwrain.org>
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

package org.luwrain.pim.mail.obsolete;

import java.util.*;
import java.io.*;
import org.apache.logging.log4j.*;

import javax.mail.*;
import javax.mail.internet.*;

import org.luwrain.core.*;


public final class AddressUtils
{
    static private final Logger log = LogManager.getLogger();

    static public String getPersonal(String addr)
    {
	if (addr.trim().isEmpty())
	    return "";
	try {
	    final var inetAddr = new javax.mail.internet.InternetAddress(addr, false);
	    final String personal = inetAddr.getPersonal();
	    return personal != null?personal.trim():"";
	}
	catch (javax.mail.internet.AddressException ex)
	{
	    log.warn("Unable to extract the personal part of the mail address '" + addr + "'", ex);
	    return "";
	}
    }

        static public String getAddress(String addr)
    {
	if (addr.trim().isEmpty())
	    return "";
	try {
	    final javax.mail.internet.InternetAddress inetAddr = new javax.mail.internet.InternetAddress(addr, false);
	    final String address = inetAddr.getAddress();
	    return address != null?address.trim():addr;
	}
	catch (javax.mail.internet.AddressException e)
	{
	    //	    	    Log.warning(LOG_COMPONENT, "unable to extract the address part of the mail address '" + addr + "'");
	    return addr;
	}
    }

    //Does not make any encoding
    static public String combinePersonalAndAddr(String personal, String addr)
    {
	NullCheck.notNull(personal, "personal");
	NullCheck.notNull(addr, "addr");
	if (personal.trim().isEmpty())
	    return addr.trim();
	return personal.trim() + " <" + addr.trim() + ">";
    }
}
