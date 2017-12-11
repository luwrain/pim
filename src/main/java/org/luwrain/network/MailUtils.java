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

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.sql.*;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.*;
import javax.mail.Flags.Flag;
import javax.mail.Message.RecipientType;
import javax.mail.internet.*;
import com.sun.mail.imap.IMAPMessage;
import com.sun.mail.pop3.POP3Message;

import org.luwrain.core.*;
import org.luwrain.pim.*;
import org.luwrain.pim.mail.*;
import org.luwrain.util.*;

public class MailUtils
{
    private final Session session=Session.getDefaultInstance(new Properties(), null); // by default was used empty session for working .eml files
    private final Message storedMsg;

    public MailUtils()
    {
	storedMsg = new MimeMessage(session);
    }

    MailUtils(Message message)
    {
	NullCheck.notNull(message, "message");
	storedMsg = message;
    }

    public MailUtils(InputStream fs) throws PimException, IOException
    {
	NullCheck.notNull(fs, "fs");
	try {
	    storedMsg = new MimeMessage(session, fs);
	}
	catch(MessagingException e)
	{
	    throw new PimException(e);
	}
    }

    public MailUtils(byte[] bytes) throws PimException, IOException
    {
	NullCheck.notNull(bytes, "bytes");
	final InputStream s = new ByteArrayInputStream(bytes);
	try {
	    try {
		storedMsg = new MimeMessage(session, s);
	    }
	    finally {
		s.close();
	    }
	}
	catch(MessagingException e)
	{
	    throw new PimException(e);
	}
    }

    Message getStoredMessage()
    {
	return storedMsg;
    }

    public void loadFrom(MailMessage msg, Map<String, String> headers) throws PimException
    {
	NullCheck.notNull(msg, "msg");
	NullCheck.notNull(headers, "headers");
	NullCheck.notNull(msg.subject, "msg.subject");
	NullCheck.notEmpty(msg.from, "msg.from");
	NullCheck.notEmptyArray(msg.to, "msg.to");
	NullCheck.notEmptyItems(msg.to, "msg.to");
	NullCheck.notEmptyItems(msg.cc, "msg.cc");
	NullCheck.notEmptyItems(msg.bcc, "msg.bcc");
	NullCheck.notEmptyItems(msg.attachments, "msg.attachments");
	try {
	    for(Map.Entry<String,String> e: headers.entrySet())
		storedMsg.addHeader(e.getKey(), e.getValue());
	    storedMsg.setSubject(msg.subject);
	    storedMsg.setFrom(new InternetAddress(msg.from));
	    storedMsg.setRecipients(RecipientType.TO, MailAddress.makeInternetAddrs(msg.to));
	    if(msg.cc.length>0)
		storedMsg.setRecipients(RecipientType.CC, MailAddress.makeInternetAddrs(msg.cc));
	    if(msg.bcc.length>0)
		storedMsg.setRecipients(RecipientType.BCC, MailAddress.makeInternetAddrs(msg.bcc));
	    if(msg.sentDate!=null)
		storedMsg.setSentDate(msg.sentDate);
	    if(msg.attachments.length > 0)
	    {
		final Multipart mp = new MimeMultipart();
		MimeBodyPart part = new MimeBodyPart();
		part.setText(msg.baseContent);
		mp.addBodyPart(part);
		for(String fn:msg.attachments)
		{
		    part = new MimeBodyPart();
		    final Path pfn = Paths.get(fn);
		    part.setFileName(MimeUtility.encodeText(pfn.getFileName().toString()));
		    final FileDataSource fds = new FileDataSource(fn);
		    part.setDataHandler(new DataHandler(fds));
		    mp.addBodyPart(part);
		}
		storedMsg.setContent(mp);
	    } else
	    {
		if(msg.mimeContentType==null)
		{ // simple text email body
		    storedMsg.setText(msg.baseContent);
		} else
		{ // for example utf8 html - mimeContentType="text/html; charset=utf-8"
		    storedMsg.setContent(msg.baseContent,msg.mimeContentType);
		}
	    }
	}
	catch(MessagingException | UnsupportedEncodingException e)
	{
	    throw new PimException(e);
	}
    }

    public void saveTo(MailMessage msg, HtmlPreview htmlPreview) throws MessagingException, UnsupportedEncodingException, IOException
    {
	NullCheck.notNull(msg, "msg");
	NullCheck.notNull(htmlPreview, "htmlPreview");
	msg.subject = storedMsg.getSubject();
	if (msg.subject == null)
	    msg.subject = "";
	if(storedMsg.getFrom()!=null)
	    msg.from = MimeUtility.decodeText(storedMsg.getFrom()[0].toString()); else
	    msg.from = "";
	msg.to = MailAddress.decodeAddrs(storedMsg.getRecipients(RecipientType.TO));
	msg.cc = MailAddress.decodeAddrs(storedMsg.getRecipients(RecipientType.CC));
	msg.bcc = MailAddress.decodeAddrs(storedMsg.getRecipients(RecipientType.BCC));
	msg.sentDate=storedMsg.getSentDate();
	msg.receivedDate=storedMsg.getReceivedDate();
	if (msg.receivedDate == null)
	    msg.receivedDate = new java.util.Date();
	final MimePartCollector collector = new MimePartCollector(htmlPreview);
	msg.baseContent = collector.run(storedMsg.getContent(), storedMsg.getContentType(), "", "");
	msg.attachments = collector.attachments.toArray(new String[collector.attachments.size()]);
	msg.mimeContentType = storedMsg.getContentType();
    }

    public void saveTo(FileOutputStream fs) throws MessagingException, IOException
    {
	NullCheck.notNull(fs, "fs");
	try {
	    storedMsg.writeTo(fs);
	}
	finally {
	    fs.flush();
	    fs.close();
	}
    }

    public byte[] saveToByteArray() throws IOException, PimException
    {
	try {
	    final File temp = File.createTempFile("email-"+String.valueOf(storedMsg.hashCode()), ".tmp");
	    try {
		FileOutputStream fs=new FileOutputStream(temp);
		storedMsg.writeTo(fs);
		fs.flush();
		fs.close();
		return Files.readAllBytes(temp.toPath());
	    }
	    finally {
		temp.delete();
	    }
	}
	catch(MessagingException e)
	{
	    throw new PimException(e);
	}
    }

    public String getMessageId() throws PimException, MessagingException
    {
	if(storedMsg instanceof IMAPMessage)
	{
	    final IMAPMessage imessage=((IMAPMessage)storedMsg);
	    return imessage.getMessageID();
	} else 
	    if(storedMsg instanceof POP3Message)
	    {
		final POP3Message pmessage=((POP3Message)storedMsg);
		return pmessage.getMessageID();
	    } else
		throw new PimException("Unknown email Message class "+storedMsg.getClass().getName());
    }

    public String[] getReplyTo(boolean decode) throws PimException
    {
	try {
	    final Address[] addr = storedMsg.getReplyTo();
	    if (addr == null || addr.length < 1)
		return new String[0];
	    final LinkedList<String> res = new LinkedList<String>();
	    for(Address a: addr)
		if (decode)
		    res.add(MimeUtility.decodeText(a.toString())); else
		    res.add(a.toString());
	    return res.toArray(new String[res.size()]);
	}
	catch(MessagingException | UnsupportedEncodingException e)
	{
	    throw new PimException(e);
	}
    }

    public String getWideReplyCc(String[] exclude, boolean decode) throws PimException
    {
	try {
	    final LinkedList<Address> addrs = new LinkedList<Address>();
	    if (storedMsg.getRecipients(RecipientType.CC) != null)
		for(Address a: storedMsg.getRecipients(RecipientType.CC)) 
		    if (a != null)
			addrs.add(a);
	    if (storedMsg.getRecipients(RecipientType.TO) != null)
		for(Address a: storedMsg.getRecipients(RecipientType.TO))
		    if (a != null)
			addrs.add(a);
	    final LinkedList<Address> filtered = new LinkedList<Address>();
	    for(Address a: addrs)
	    {
		if (!(a instanceof InternetAddress))
		{
		    filtered.add(a);
		    continue;
		}
		final InternetAddress addr = (InternetAddress)a;
		if (addr.getAddress() == null)
		{
		    filtered.add(addr);
		    continue;
		}
		int k;
		for(k = 0;k < exclude.length;++k)
		    if (addr.getAddress().trim().toLowerCase().equals(exclude[k].toLowerCase().trim()))
			break;
		if (k >= exclude.length)
		    filtered.add(addr);
	    }
	    final Address addrs2[] = filtered.toArray(new Address[filtered.size()]);
	    if (addrs2.length < 1)
		return "";
	    final StringBuilder res = new StringBuilder();
	    res.append(decode?MimeUtility.decodeText(addrs2[0].toString()):addrs2[0].toString());
	    for(int i = 1;i < addrs2.length;++i)
		res.append("," + (decode?MimeUtility.decodeText(addrs2[i].toString()):addrs2[i].toString()));
	    return res.toString();
	}
	catch(MessagingException | UnsupportedEncodingException e)
	{
	    throw new PimException(e);
	}
    }

    public boolean saveAttachment(String fileName, File destFile) throws PimException
    {
	NullCheck.notEmpty(fileName, "fileName");
	NullCheck.notNull(destFile, "destFile");
	final MailAttachmentSaving saving = new MailAttachmentSaving(fileName, destFile);
	try {
	    saving.run(storedMsg.getContent(), storedMsg.getContentType(), "", "");
	    return saving.result() == MailAttachmentSaving.SUCCESS;
	}
	catch(IOException | MessagingException e)
	{
	    e.printStackTrace();
	    throw new PimException(e);
	}
    }

    static public String extractNameFromAddr(String addr)
    {
	NullCheck.notNull(addr, "addr");
	if (addr.trim().isEmpty())
	    return addr;
	try {
	    final javax.mail.internet.InternetAddress inetAddr = new javax.mail.internet.InternetAddress(addr, false);
	    final String personal = inetAddr.getPersonal();
	    if (personal == null || personal.trim().isEmpty())
		return addr;
	    return personal;
	}
	catch (javax.mail.internet.AddressException e)
	{
	    e.printStackTrace();
	    return addr;
	}
    }
}
