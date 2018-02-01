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

import java.io.*;
import java.util.*;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;

import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.Flags.Flag;
import javax.mail.Message.RecipientType;

import org.luwrain.core.*;
import org.luwrain.pim.*;
import org.luwrain.pim.mail.*;
import org.luwrain.util.*;

public final class BinaryMessage
{
    static public byte[] toByteArray(MailMessage message, Map<String, String> extraHeaders) throws MessagingException, UnsupportedEncodingException, IOException
    {
	NullCheck.notNull(message, "message");
	NullCheck.notNull(extraHeaders, "extraHeaders");
	final javax.mail.internet.MimeMessage mimeMessage = convertToMimeMessage(message, extraHeaders);
	return toByteArray(mimeMessage);
    }

    static private javax.mail.internet.MimeMessage convertToMimeMessage(MailMessage srcMsg, Map<String, String> headers) throws MessagingException, UnsupportedEncodingException
    {
	NullCheck.notNull(srcMsg, "srcMsg");
	NullCheck.notNull(headers, "headers");
	NullCheck.notNull(srcMsg.subject, "srcMsg.subject");
	NullCheck.notEmpty(srcMsg.from, "srcMsg.from");
	NullCheck.notEmptyArray(srcMsg.to, "srcMsg.to");
	NullCheck.notEmptyItems(srcMsg.to, "srcMsg.to");
	NullCheck.notEmptyItems(srcMsg.cc, "srcMsg.cc");
	NullCheck.notEmptyItems(srcMsg.bcc, "srcMsg.bcc");
	NullCheck.notEmptyItems(srcMsg.attachments, "srcMsg.attachments");
	final Session session = Session.getDefaultInstance(new Properties(), null);
	final javax.mail.internet.MimeMessage message = new MimeMessage(session);;
	for(Map.Entry<String,String> e: headers.entrySet())
	    message.addHeader(e.getKey(), e.getValue());
	message.setSubject(srcMsg.subject);
	message.setFrom(new InternetAddress(srcMsg.from));
	message.setRecipients(RecipientType.TO, encodeAddrs(srcMsg.to));
	if(srcMsg.cc.length>0)
	    message.setRecipients(RecipientType.CC, encodeAddrs(srcMsg.cc));
	if(srcMsg.bcc.length>0)
	    message.setRecipients(RecipientType.BCC, encodeAddrs(srcMsg.bcc));
	if(srcMsg.sentDate!=null)
	    message.setSentDate(srcMsg.sentDate);
	if(srcMsg.attachments.length > 0)
	{
	    final Multipart mp = new MimeMultipart();
	    MimeBodyPart part = new MimeBodyPart();
	    part.setText(srcMsg.baseContent);
	    mp.addBodyPart(part);
	    for(String fn:srcMsg.attachments)
	    {
		part = new MimeBodyPart();
		final File pfn = new File(fn);
		part.setFileName(MimeUtility.encodeText(pfn.getName()));
		final FileDataSource fds = new FileDataSource(fn);
		part.setDataHandler(new DataHandler(fds));
		mp.addBodyPart(part);
	    }
	    message.setContent(mp);
	} else
	{
	    if(srcMsg.mimeContentType==null)
	    { // simple text email body
		message.setText(srcMsg.baseContent);
	    } else
	    { // for example utf8 html - mimeContentType="text/html; charset=utf-8"
		message.setContent(srcMsg.baseContent,srcMsg.mimeContentType);
	    }
	}
	return message;
    }

    static public void convertFromMimeMessage(MimeMessage srcMsg, MailMessage dest, HtmlPreview htmlPreview) throws MessagingException, UnsupportedEncodingException, IOException
    {
	NullCheck.notNull(srcMsg, "srcMsg");
	NullCheck.notNull(dest, "dest");
	NullCheck.notNull(htmlPreview, "htmlPreview");
	dest.subject = srcMsg.getSubject();
	if (dest.subject == null)
	    dest.subject = "";
	if(srcMsg.getFrom() != null)
	    dest.from = MimeUtility.decodeText(srcMsg.getFrom()[0].toString()); else
	    dest.from = "";
	dest.to = decodeAddrs(srcMsg.getRecipients(RecipientType.TO));
	dest.cc = decodeAddrs(srcMsg.getRecipients(RecipientType.CC));
	dest.bcc = decodeAddrs(srcMsg.getRecipients(RecipientType.BCC));
	dest.sentDate = srcMsg.getSentDate();
	dest.receivedDate = srcMsg.getReceivedDate();
	if (dest.receivedDate == null)
	    dest.receivedDate = new java.util.Date();
	final MimePartCollector collector = new MimePartCollector(htmlPreview);
	dest.baseContent = collector.run(srcMsg.getContent(), srcMsg.getContentType(), "", "");
	dest.attachments = collector.attachments.toArray(new String[collector.attachments.size()]);
	dest.mimeContentType = srcMsg.getContentType();
    }

    static private byte[] toByteArray(javax.mail.Message message) throws MessagingException, IOException
    {
	NullCheck.notNull(message, "message");
	final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
	try {
	    message.writeTo(byteStream);
	    byteStream.flush();
	    return byteStream.toByteArray();
	}
	finally {
	    byteStream.close();
	}
    }

    static private String[] decodeAddrs(Address[] addrs) throws UnsupportedEncodingException
    {
	if (addrs == null)
	    return new String[0];
	final List<String> res=new LinkedList();
	for(int i = 0;i < addrs.length;++i)
	    if (addrs[i] != null)
		res.add(MimeUtility.decodeText(addrs[i].toString()));
	return res.toArray(new String[res.size()]);
    }

    static InternetAddress[] encodeAddrs(String[] addrs) throws AddressException
    {
	NullCheck.notNullItems(addrs, "addrs");
	final List<InternetAddress> res = new LinkedList();
	for(String s: addrs)
	    if (!s.trim().isEmpty())
		res.add(new InternetAddress(s));
	return res.toArray(new InternetAddress[res.size()]);
    }


    

    static class MimePartCollector
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
}