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
    static public byte[] toByteArray(MailMessage message, Map<String, String> extraHeaders) throws PimException, IOException
    {
	NullCheck.notNull(message, "message");
	NullCheck.notNull(extraHeaders, "extraHeaders");
	try {
	    return mimeToByteArray(convertToMimeMessage(message, extraHeaders));
	}
	catch(MessagingException e)
	{
	    throw new PimException(e);
	}
    }

    static public MailMessage fromByteArray(byte[] bytes) throws PimException, IOException
    {
	NullCheck.notNull(bytes, "bytes");
	final MailMessage message = new MailMessage();
	try {
	    convertFromMimeMessage(mimeFromByteArray(bytes), message);
	}
	catch(MessagingException e)
	{
	    throw new PimException(e);
	}
	message.setRawMessage(bytes);
	return message;
    }

    static private javax.mail.internet.MimeMessage convertToMimeMessage(MailMessage srcMsg, Map<String, String> headers) throws PimException, IOException, MessagingException
    {
	NullCheck.notNull(srcMsg, "srcMsg");
	NullCheck.notNull(headers, "headers");
	//	NullCheck.notEmpty(srcMsg.getFrom(), "srcMsg.getFrom()");
	//	NullCheck.notEmptyArray(srcMsg.to, "srcMsg.to");
	//	NullCheck.notEmptyItems(srcMsg.to, "srcMsg.to");
	//	NullCheck.notEmptyItems(srcMsg.cc, "srcMsg.cc");
	//	NullCheck.notEmptyItems(srcMsg.bcc, "srcMsg.bcc");
	//	NullCheck.notEmptyItems(srcMsg.attachments, "srcMsg.attachments");
	final Session session = Session.getDefaultInstance(new Properties(), null);
	final javax.mail.internet.MimeMessage message = new MimeMessage(session);;
	for(Map.Entry<String,String> e: headers.entrySet())
	    message.addHeader(e.getKey(), e.getValue());
	message.setSubject(srcMsg.getSubject());
	message.setFrom(encodeAddr(srcMsg.getFrom()));
	message.setRecipients(RecipientType.TO, encodeAddrs(srcMsg.getTo()));
	if(srcMsg.getCc().length>0)
	    message.setRecipients(RecipientType.CC, encodeAddrs(srcMsg.getCc()));
	if(srcMsg.getBcc().length>0)
	    message.setRecipients(RecipientType.BCC, encodeAddrs(srcMsg.getBcc()));
	message.setSentDate(srcMsg.getSentDate());
	if(srcMsg.getAttachments().length > 0)
	{
	    final Multipart mp = new MimeMultipart();
	    MimeBodyPart part = new MimeBodyPart();
	    part.setText(srcMsg.getText());
	    mp.addBodyPart(part);
	    for(String fn:srcMsg.getAttachments())
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
	    if(srcMsg.getContentType().isEmpty())
	    { // simple text email body
		message.setText(srcMsg.getText());
	    } else
	    { // for example utf8 html - mimeContentType="text/html; charset=utf-8"
		message.setContent(srcMsg.getText(), srcMsg.getContentType());
	    }
	}
	return message;
    }

    static private void convertFromMimeMessage(MimeMessage srcMsg, MailMessage dest) throws PimException, MessagingException, UnsupportedEncodingException, IOException
    {
	NullCheck.notNull(srcMsg, "srcMsg");
	NullCheck.notNull(dest, "dest");
	if (srcMsg.getSubject() != null)
	dest.setSubject(srcMsg.getSubject());
	if(srcMsg.getFrom() != null)
	    dest.setFrom(MimeUtility.decodeText(srcMsg.getFrom()[0].toString()));
	if (srcMsg.getRecipients(RecipientType.TO) != null)
	dest.setTo(decodeAddrs(srcMsg.getRecipients(RecipientType.TO)));
	if (srcMsg.getRecipients(RecipientType.CC) != null)
	    dest.setCc(decodeAddrs(srcMsg.getRecipients(RecipientType.CC)));
	if (srcMsg.getRecipients(RecipientType.BCC) != null)
	    dest.setBcc(decodeAddrs(srcMsg.getRecipients(RecipientType.BCC)));
	dest.setSentDate(srcMsg.getSentDate());
	dest.setReceivedDate(srcMsg.getReceivedDate());
	final MimePartCollector collector = new MimePartCollector();
	dest.setText(collector.run(srcMsg.getContent(), srcMsg.getContentType(), "", ""));
	dest.setAttachments(collector.attachments.toArray(new String[collector.attachments.size()]));
	dest.setContentType(srcMsg.getContentType());
    }

    static private byte[] mimeToByteArray(javax.mail.internet.MimeMessage message) throws MessagingException, IOException
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

    static private javax.mail.internet.MimeMessage mimeFromByteArray( byte[] bytes) throws MessagingException, IOException
    {
	NullCheck.notNull(bytes, "bytes");
	final ByteArrayInputStream byteStream = new ByteArrayInputStream(bytes);
	final Session session = Session.getDefaultInstance(new Properties(), null);
	try {
	    return new MimeMessage(session, byteStream);
	}
	finally {
	    byteStream.close();
	}
    }

    static public String decodeText(String text) throws IOException
    {
	NullCheck.notNull(text, "text");
	return MimeUtility.decodeText(text);
    }

    static private String[] decodeAddrs(Address[] addrs) throws IOException
    {
	if (addrs == null)
	    return new String[0];
	final List<String> res=new LinkedList();
	for(int i = 0;i < addrs.length;++i)
	    if (addrs[i] != null)
		res.add(decodeText(addrs[i].toString()));
	return res.toArray(new String[res.size()]);
    }

    static InternetAddress encodeAddr(String addr) throws IOException, AddressException
    {
	NullCheck.notNull(addr, "addr");
	final String personal = AddressUtils.getPersonal(addr);
	final String mail = AddressUtils.getAddress(addr);
	if (personal.trim().isEmpty())
	    return new InternetAddress(addr);
	return new InternetAddress(AddressUtils.combinePersonalAndAddr(MimeUtility.encodeText(personal), mail));
    }

    static InternetAddress[] encodeAddrs(String[] addrs) throws AddressException
    {
	NullCheck.notNullItems(addrs, "addrs");
	final List<InternetAddress> res = new LinkedList();
	for(String s: addrs)
	    if (!s.trim().isEmpty())
		res.add(new InternetAddress(s));//FIXME:encoding???
	return res.toArray(new InternetAddress[res.size()]);
    }

    static class MimePartCollector
    {
	final List<String> attachments = new LinkedList();
	String run(Object o, String contentType,
		   String fileName, String disposition) throws PimException, IOException, MessagingException
	{
	    if(o instanceof MimeMultipart)
	    {
		final Multipart content =(Multipart)o;
		final boolean alternative = (contentType.toLowerCase().indexOf("alternative") >= 0);
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
		return o.toString();
	    return o.toString();
	}
	protected void onAttachment(String fileName, Object obj) throws IOException
	{
	}
    }
}
