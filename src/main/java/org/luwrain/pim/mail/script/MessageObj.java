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
import org.luwrain.pim.*;
import org.luwrain.pim.mail.*;

import static org.luwrain.util.TextUtils.*;

public final class MessageObj
{
    final MailMessage message;
    private String[] headers = null;
    private MailingListObj listHookObj = null;

    public MessageObj(MailMessage message)
    {
	NullCheck.notNull(message, "message");
	this.message = message;
    }

    @HostAccess.Export
    public final ProxyExecutable getSubject = (ProxyExecutable)this::getSubjectImpl;
    private Object getSubjectImpl(Value[] args) { return message.getSubject(); };

        @HostAccess.Export
    public final ProxyExecutable getFrom = (ProxyExecutable)this::getFromImpl;
    private Object getFromImpl(Value[] args) { return new AddressObj(message.getFrom()); }

            @HostAccess.Export
    public final ProxyExecutable getTo = (ProxyExecutable)this::getToImpl;
    private Object getToImpl(Value[] args)
    {
	if (message.getTo() == null || message.getTo().length == 0)
	    return null;
	return new AddressObj(message.getTo()[0]);
    }


    @HostAccess.Export
    public final ProxyExecutable getText = (ProxyExecutable)this::getTextImpl;
    private Object getTextImpl(Value[] args) { return message.getText(); }

        @HostAccess.Export
    public final ProxyExecutable getTextAsArray = (ProxyExecutable)this::getTextAsArrayImpl;
    private Object getTextAsArrayImpl(Value[] args)
    {
	if (message.getText() == null)
	    return ProxyArray.fromArray(new Object[0]);
	return ProxyArray.fromArray(splitLinesAnySeparator(message.getText()));
    }


    @HostAccess.Export
    public Object getCc(Value[] args)
    {
		    final List<Object> res = new ArrayList<>();
		    for(String s: message.getCc())
			if (s != null)
			    res.add(new AddressObj(s));
		    return ProxyArray.fromArray(res.toArray(new Object[res.size()]));
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
		    this.listHookObj = new MailingListObj(headers);
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

    public MailMessage getMessage()
    {
	return message;
    }
}
