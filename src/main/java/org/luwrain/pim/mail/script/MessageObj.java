/*
   Copyright 2012-2022 Michael Pozhidaev <msp@luwrain.org>
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
import org.graalvm.polyglot.*;
import org.graalvm.polyglot.proxy.*;

import org.luwrain.core.*;
import org.luwrain.script.*;
import org.luwrain.pim.*;
import org.luwrain.pim.mail.*;

public final class MessageObj extends EmptyHookObject
{
    static final String LOG_COMPONENT = MailHookObject.LOG_COMPONENT;

    final MailMessage message;
    private String[] headers = null;
    private MailingListHookObject listHookObj = null;

    public MessageObj(MailMessage message)
    {
	NullCheck.notNull(message, "message");
	this.message = message;
    }

    @HostAccess.Export
    public final ProxyExecutable getSubject = (ProxyExecutable)this::getSubjectImpl;
    private Object getSubjectImpl(Value[] args)
    {
	return message.getSubject();
    };

    @HostAccess.Export
    public final ProxyExecutable getMessageText = (ProxyExecutable)this::getMessageTextImpl;
    private Object getMessageTextImpl(Value[] args)
    {
			return message.getText();
    }

    @HostAccess.Export
    public final ProxyExecutable getFromAddress = (ProxyExecutable)this::getFromAddressImpl;
    private Object getFromAddressImpl(Value[] args)
    {
			return new AddressObj(message.getFrom());
    }

    @HostAccess.Export
    public Object getCc(Value[] args)
    {
		    final List<Object> res = new ArrayList<>();
		    for(String s: message.getCc())
			if (s != null)
			    res.add(new AddressObj(s));
		    return ProxyArray.fromArray(res.toArray(new HookObject[res.size()]));
    }

    public Object getHeaders(Value[] args)
    {
			if (headers == null)
		    headers = extractHeaders(message.getRawMessage());
			return ProxyArray.fromArray((Object[])headers);
    }

    public Object getMailingList()
    {
    		if (this.listHookObj == null)
		{
		    if (headers == null)
			headers = extractHeaders(message.getRawMessage());
		    this.listHookObj = new MailingListHookObject(headers);
		}
		return listHookObj;
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

    public MailMessage getNativeMessageObj()
    {
	return message;
    }
}
