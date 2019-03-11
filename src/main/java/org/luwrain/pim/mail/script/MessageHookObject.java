/*
   Copyright 2012-2019 Michael Pozhidaev <michael.pozhidaev@gmail.com>
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

package org.luwrain.pim.mail.script;

import java.util.*;

import org.luwrain.core.*;
import org.luwrain.script.*;
import org.luwrain.pim.*;
import org.luwrain.pim.mail.*;

public final class MessageHookObject extends EmptyHookObject
{
    static final String LOG_COMPONENT = MailHookObject.LOG_COMPONENT;

    final MailMessage message;
    private String[] headers = null;
    private ListHookObject listHookObj = null;

    public MessageHookObject(MailMessage message)
    {
	NullCheck.notNull(message, "message");
	this.message = message;
    }

    @Override public Object getMember(String name)
    {
	NullCheck.notNull(name, "name");
	switch(name)
	{
	case "subject":
	    return message.subject != null?message.subject:"";
	    	case "from":
		    return message.from != null?new AddressHookObject(message.from):"";
	case "cc":
	    {
		final List<HookObject> res = new LinkedList();
		if (message.cc != null)
		    for(String s: message.cc)
			if (s != null)
			    res.add(new AddressHookObject(s));
		return ScriptUtils.createReadOnlyArray(res.toArray(new HookObject[res.size()]));
	    }
	case "headers":
	    if (headers == null)
		headers = extractHeaders(message.rawMail);
	    return ScriptUtils.createReadOnlyArray(headers);
	case "list":
	    if (this.listHookObj == null)
	    {
	    if (headers == null)
		headers = extractHeaders(message.rawMail);
	    this.listHookObj = new ListHookObject(headers);
	    }
	    return listHookObj;
	    
	default:
	    return super.getMember(name);
	}
    }

    static private String[] extractHeaders(byte[] bytes)
    {
	NullCheck.notNull(bytes, "bytes");
	final String str;
	try {
	    str = new String(bytes, "US-ASCII");
	}
	catch(java.io.UnsupportedEncodingException e)
	{
	    Log.debug(LOG_COMPONENT, "unable to decode a message to US-ASCII:" + e.getClass().getName() + ":" + e.getMessage());
	    return new String[0];
	}
	final String[] lines = str.split("\n", -1);
	final List<String> res = new LinkedList();
	for(String s: lines)
	{
	    if (s.trim().isEmpty())
		break;
	    res.add(s.replaceAll("\r", ""));
	}
	return res.toArray(new String[res.size()]);
    }
}
