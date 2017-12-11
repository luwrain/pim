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

import org.luwrain.util.*;

class MimePartCollector
{
    final LinkedList<String> attachments = new LinkedList<String>();
    private HtmlPreview htmlPreview = null;
    //    final StringBuilder body = new StringBuilder();

    MimePartCollector()
    {
	htmlPreview = null;
    }

    MimePartCollector(HtmlPreview htmlPreview)
    {
	this.htmlPreview = htmlPreview;
    }

    String run(Object o, String contentType,
	     String fileName, String disposition) throws IOException, MessagingException
    {
	if(o instanceof MimeMultipart)
	{
	    final Multipart content =(Multipart)o;
	    //	    System.out.println("multipart " + contentType);
	    final boolean alternative = (contentType.toLowerCase().indexOf("alternative") >= 0);
	    //	    System.out.println("alternative " + alternative);
	    final StringBuilder textRes = new StringBuilder();
	    final StringBuilder htmlRes = new StringBuilder();
	    for(int i=0;i<content.getCount();i++)
	    {
		final MimeBodyPart part = (MimeBodyPart) content.getBodyPart(i);
		if (part == null || part.getContent() == null)
		    continue;
		final String partContentType = part.getContentType() != null?part.getContentType():"";
		final boolean html = (partContentType.toLowerCase().indexOf("html") >= 0);
		if (html)
		    htmlRes.append(run(part.getContent(), partContentType, 
				       part.getFileName() != null?part.getFileName():"", 
				       part.getDisposition() != null?part.getDisposition():"")); else
		    textRes.append(run(part.getContent(), partContentType, 
				       part.getFileName() != null?part.getFileName():"" , 
				       part.getDisposition() != null?part.getDisposition():""));
	    }
	    final String textStr = textRes.toString();
	    final String htmlStr = htmlRes.toString();
	    if (!alternative)
		return textStr + "\n" + htmlStr;
	    if (!textStr.trim().isEmpty())
		return textStr;
	    return htmlStr;
	}

	if ((disposition != null && disposition.toLowerCase().indexOf("attachment") >= 0) ||
	    contentType.toLowerCase().indexOf("text") < 0)
	{
	    if (fileName != null && !fileName.trim().isEmpty())
	    {
		attachments.add(MimeUtility.decodeText(fileName));
		onAttachment(MimeUtility.decodeText(fileName), o);
	    } else
	    {
		attachments.add(contentType);
		onAttachment(contentType, o);
	    }
	    return "";
	}

	if (contentType != null && contentType.toLowerCase().indexOf("html") >= 0)
	    return htmlPreview.generateHtmlTextPreview(o.toString());
	return o.toString();
    }

    protected void onAttachment(String fileName, Object obj) throws IOException
    {
	//	System.out.println(fileName + ":" + obj.getClass().getName());
    }
}
