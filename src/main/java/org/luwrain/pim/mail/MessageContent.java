/*
   Copyright 2012-2020 Michael Pozhidaev <msp@luwrain.org>
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
import com.google.gson.*;
import com.google.gson.annotations.*;

import org.luwrain.core.*;

final class MessageContent
{
    @SerializedName("to")
    private String to = null;

    @SerializedName("cc")
    private String cc = null;

    @SerializedName("subject")
    public String subject = null;

    @SerializedName("body")
    private List<String> body = null;

    @SerializedName("headers")
    private List<String> headers = null;

    @SerializedName("attachments")
    private List<Attachment> attachments = null;

    public String getTo()
    {
	return to != null?to:"";
    }

    public void setTo(String to)
    {
	NullCheck.notNull(to, "to");
	this.to = to;
    }

    public String getCc()
    {
	return cc != null?cc:"";
    }

    public void setCc(String cc)
    {
	NullCheck.notNull(cc, "cc");
	this.cc = cc;
    }

    public String getSubject()
    {
	return subject != null?subject:"";
    }

    public void setSubject(String subject)
    {
	NullCheck.notNull(subject, "subject");
	this.subject = subject;
    }

    public String[] getBody()
    {
	if (body == null)
	    return new String[0];
	return body.toArray(new String[body.size()]);
    }

    public void setBody(String[] body)
    {
	NullCheck.notNullItems(body, "body");
	this.body = new LinkedList();
	for(String b: body)
	    this.body.add(b);
    }

    public String[] getHeaders()
    {
	if (headers == null)
	    return new String[0];
	return headers.toArray(new String[headers.size()]);
    }

    public void setHeaders(String[] headers)
    {
	NullCheck.notNullItems(headers, "headers");
	this.headers = new LinkedList();
	for(String h: headers)
	    this.headers.add(h);
    }

    public Attachment[] getAttachments()
    {
	if (attachments == null)
	    return new Attachment[0];
	return attachments.toArray(new Attachment[attachments.size()]);
    }

    public void setAttachments(Attachment[] attachments)
    {
	NullCheck.notNullItems(attachments, "attachments");
	this.attachments = new LinkedList();
	for(Attachment a: attachments)
	    this.attachments.add(a);
    }

    @Override public String toString()
    {
	return new Gson().toJson(this);
    }

    static public MessageContent fromString(String str)
    {
	NullCheck.notNull(str, "str");
	try {
	    return new Gson().fromJson(str, MessageContent.class);
	}
	catch(Exception e)
	{
	    throw new IllegalArgumentException(e);
	}
    }

static public final class Attachment
{
    @SerializedName("path")
private String path = null;
    @SerializedName("fileName")
private String fileName = null;
    public String getPath()
    {
	return path;
    }
    public void setPath(String path)
    {
	NullCheck.notNull(path, "path");
	this.path = path;
    }
    public String ggetFileName()
    {
	return fileName;
    }
    public void setFileName(String fileName)
    {
	NullCheck.notNull(fileName, "fileName");
	this.fileName = fileName;
    }
}
}
