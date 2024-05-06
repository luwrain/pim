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

package org.luwrain.pim.mail.script;

import java.util.*;
import java.io.*;
import javax.mail.*;
import javax.mail.internet.*;

import org.graalvm.polyglot.*;
import org.graalvm.polyglot.proxy.*;

import org.luwrain.core.*;
import org.luwrain.pim.*;
import org.luwrain.pim.mail2.*;

import static org.luwrain.core.NullCheck.*;
import static org.luwrain.util.TextUtils.*;
import static org.luwrain.script.ScriptUtils.*;
import static org.luwrain.pim.mail.script.MailObj.*;

public final class MessageObj
{
    public final org.luwrain.pim.mail2.Message message;
    private MimeMessage mimeMessage = null;
    private List<String> headers = null;
    private MailingListObj listHookObj = null;

    public MessageObj(org.luwrain.pim.mail2.Message message)
    {
	notNull(message, "message");
	notNull(message.getMetadata(), "message.metadata");
	this.message = message;
    }

    @HostAccess.Export
    public final ProxyExecutable getSubject = (ProxyExecutable)this::getSubjectImpl;
    private Object getSubjectImpl(Value[] args) { return message.getMetadata().getSubject(); }

        @HostAccess.Export
    public final ProxyExecutable getTitle = (ProxyExecutable)this::getTitleImpl;
    private Object getTitleImpl(Value[] args) { return message.getMetadata().getTitle(); }

            @HostAccess.Export
    public final ProxyExecutable setTitle = (ProxyExecutable)this::setTitleImpl;
    private Object setTitleImpl(Value[] args)
    {
	if (notNullAndLen(args, 1) || !args[0].isString())
	    throw new IllegalArgumentException("Message.setTitle() takes exactly one string argument");
	message.getMetadata().setTitle(args[0].asString());
	return this;
    }

        @HostAccess.Export
    public final ProxyExecutable getFrom = (ProxyExecutable)this::getFromImpl;
    private Object getFromImpl(Value[] args) { return new AddressObj(message.getMetadata().getFromAddr()); }

            @HostAccess.Export
    public final ProxyExecutable getTo = (ProxyExecutable)this::getToImpl;
    private Object getToImpl(Value[] args)
    {
	if (message.getMetadata().getToAddr() == null || message.getMetadata().getToAddr().isEmpty())
	    return null;
	return new AddressObj(message.getMetadata().getToAddr().get(0));
    }


    @HostAccess.Export
    public final ProxyExecutable getText = (ProxyExecutable)this::getTextImpl;
    private Object getTextImpl(Value[] args) { return message.getMetadata().getText(); }

        @HostAccess.Export
    public final ProxyExecutable getTextAsArray = (ProxyExecutable)this::getTextAsArrayImpl;
    private Object getTextAsArrayImpl(Value[] args)
    {
	if (message.getMetadata().getText() == null)
	    return ProxyArray.fromArray(new Object[0]);
	return ProxyArray.fromArray(splitLinesAnySeparator(message.getMetadata().getText()));
    }


    @HostAccess.Export
    public Object getCc(Value[] args)
    {
		    final List<Object> res = new ArrayList<>();
		    for(String s: message.getMetadata().getCcAddr())
			if (s != null)
			    res.add(new AddressObj(s));
		    return ProxyArray.fromArray(res.toArray(new Object[res.size()]));
    }

        @HostAccess.Export
	    public ProxyExecutable getHeaders = this::getHeadersImpl;
    private Object getHeadersImpl(Value[] args)
    {
	initMimeMessage();
	return ProxyArray.fromArray((Object[])headers.toArray(new String[headers.size()]));
    }

            @HostAccess.Export
	    public ProxyExecutable getHeader = this::getHeaderImpl;
    private Object getHeaderImpl(Value[] args)
    {
	if (!notNullAndLen(args, 1) || !args[0].isString() || args[0].asString().trim().isEmpty())
	    throw new IllegalArgumentException("Message.getHeader() takes exactly one non-empty string argument");
	initMimeMessage();
	final var prefix = args[0].asString().trim() + ":";
	final var res = new ArrayList<Object>();
	for (var s: headers)
	    if (s.startsWith(prefix))
		res.add(s.substring(prefix.length()).trim());
	return ProxyArray.fromList(res);
    }


    /*
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
    */

    /*
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
	final List<String> res = new ArrayList<>();
	for(String s: lines)
	{
	    if (s.trim().isEmpty())
		break;
	    res.add(s.replaceAll("\r", ""));
	}
	return res.toArray(new String[res.size()]);
    }
    */

    private void initMimeMessage()
    {
	if (mimeMessage != null && headers != null)
	    return;
		if (message.getRawMessage() == null && message.getRawMessage().length == 0)
		    throw new IllegalStateException("No raw message, unable to create MIME message");
	    try {
	    try (final ByteArrayInputStream byteStream = new ByteArrayInputStream(message.getRawMessage())) {
this.mimeMessage = new MimeMessage(session, byteStream);
	}
	    	    	    headers = new ArrayList<>();
	    for (String h: Collections.list(mimeMessage.getAllHeaderLines()))
		headers.add(h);
	    }
	    catch(IOException | MessagingException ex)
	    {
		log.catching(ex);
		this.mimeMessage = null;
		this.headers = null;
		throw new PimException(ex);
	    }
    }
}
