/*
   Copyright 2012-2015 Michael Pozhidaev <michael.pozhidaev@gmail.com>
   Copyright 2015 Roman Volovodov <gr.rPman@gmail.com>

   This file is part of the LUWRAIN.

   LUWRAIN is free software; you can redistribute it and/or
   modify it under the terms of the GNU General Public
   License as published by the Free Software Foundation; either
   version 3 of the License, or (at your option) any later version.

   LUWRAIN is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
   General Public License for more details.
*/

package org.luwrain.util;

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

import org.luwrain.core.NullCheck;
import org.luwrain.pim.mail.MailStoringSql.Condition;
import org.luwrain.pim.mail.*;

public class MailEssentialJavamail
{
    private Session session=Session.getDefaultInstance(new Properties(), null); // by default was used empty session for working .eml files
    public Message jmailmsg;

    private InternetAddress[] prepareInternetAddrs(String[] addrs) throws AddressException
    {
	NullCheck.notNull(addrs, "addrs");
	final InternetAddress[] res =new InternetAddress[addrs.length];
	for(int i = 0;i < addrs.length;++i)
	    res[i] = new InternetAddress(addrs[i]);
	return res;
    }

    public void prepareInternalStore(MailMessage msg) throws Exception
    {
	NullCheck.notNull(msg.subject, "msg.subject");
	if (msg.from == null || msg.from.trim().isEmpty())
	    throw new IllegalArgumentException("msg.from may not be empty");
	if (msg.to == null || msg.to.length < 1)
	    throw new IllegalArgumentException("msg.to may not be empty");
	if (msg.to[0] == null || msg.to[0].trim().isEmpty())
	    throw new IllegalArgumentException("msg.to[0] may not be empty");
	jmailmsg=new MimeMessage(session);
	jmailmsg.setSubject(msg.subject);
	jmailmsg.setFrom(new InternetAddress(msg.from));
	jmailmsg.setRecipients(RecipientType.TO, prepareInternetAddrs(msg.to));
	if(msg.cc!=null&&msg.cc.length>0)
	    jmailmsg.setRecipients(RecipientType.CC, prepareInternetAddrs(msg.cc));
	if(msg.bcc!=null&&msg.bcc.length>0)
	    jmailmsg.setRecipients(RecipientType.BCC, prepareInternetAddrs(msg.bcc));

	System.out.println("Ready to set date");
	if(msg.sentDate!=null)
	    jmailmsg.setSentDate(msg.sentDate);

	System.out.println("Ready to set body and attachments");
	// attachments and message body
	if(msg.attachments != null && msg.attachments.length > 0)
	{
	    Multipart mp = new MimeMultipart();
	    MimeBodyPart part = new MimeBodyPart();
	    part.setText(msg.baseContent);
	    // TODO: need to repair - in multipart message mimeContentType of baseContent was ignored
	    mp.addBodyPart(part);
	    for(String fn:msg.attachments)
	    {
		part = new MimeBodyPart();
		Path pfn=Paths.get(fn);
		part.setFileName(pfn.getFileName().toString());
		FileDataSource fds = new FileDataSource(fn);
		part.setDataHandler(new DataHandler(fds));
		mp.addBodyPart(part);
	    }
	    jmailmsg.setContent(mp);
	} else
	{
	    if(msg.mimeContentType==null)
	    { // simple text email body
		jmailmsg.setText(msg.baseContent);
	    } else
	    { // for example utf8 html - mimeContentType="text/html; charset=utf-8"
		jmailmsg.setContent(msg.baseContent,msg.mimeContentType);
	    }
	}
    }

    void readMessageBasicFields(MailMessage msg) throws Exception
    {
	msg.subject=jmailmsg.getSubject();
	if(jmailmsg.getFrom()!=null)
	    msg.from=jmailmsg.getFrom()[0].toString(); else
	    msg.from=null;
	if(jmailmsg.getRecipients(RecipientType.TO)!=null)
	{
	    final LinkedList<String> to=new LinkedList<String>();
	    for(Address addr:jmailmsg.getRecipients(RecipientType.TO))
		to.add(addr.toString());
	    msg.to=to.toArray(new String[to.size()]);
	} else
	    msg.to=null;
	if(jmailmsg.getRecipients(RecipientType.CC)!=null)
	{
	    final LinkedList<String> to=new LinkedList<String>();
	    for(Address addr:jmailmsg.getRecipients(RecipientType.CC)) 
to.add(addr.toString());
	    msg.cc=to.toArray(new String[to.size()]);
	} else 
	    msg.cc=null;
	if(jmailmsg.getRecipients(RecipientType.BCC)!=null)
	{
	    final LinkedList<String> to=new LinkedList<String>();
	    for(Address addr:jmailmsg.getRecipients(RecipientType.BCC)) 
to.add(addr.toString());
	    msg.bcc=to.toArray(new String[to.size()]);
	} else 
	    msg.bcc=null;
	msg.sentDate=jmailmsg.getSentDate();
	msg.receivedDate=jmailmsg.getReceivedDate();
	if (msg.receivedDate == null)
	    msg.receivedDate = new java.util.Date();

	final MimePartCollector collector = new MimePartCollector();
	collector.run(jmailmsg.getContent(), jmailmsg.getContentType(), "");
	msg.attachments = collector.attachments.toArray(new String[collector.attachments.size()]);
	msg.baseContent = collector.body.toString();
	msg.mimeContentType = jmailmsg.getContentType();
    }

    void readMessageId(MailMessage msg) throws Exception
    {
	if(jmailmsg instanceof IMAPMessage)
	{
	    final IMAPMessage imessage=((IMAPMessage)jmailmsg);
	    msg.messageId=imessage.getMessageID();
	} else 
	    if(jmailmsg instanceof POP3Message)
	    {
		final POP3Message pmessage=((POP3Message)jmailmsg);
		msg.messageId=pmessage.getMessageID();
	    } else
	throw new Exception("Unknown email Message class "+jmailmsg.getClass().getName());
    }

    public void saveRawContent(MailMessage msg) throws Exception
    {
	final File temp = File.createTempFile("email-"+String.valueOf(jmailmsg.hashCode()), ".tmp");
	FileOutputStream fs=new FileOutputStream(temp);
	jmailmsg.writeTo(fs);
	fs.flush();
	fs.close();
	msg.rawMail = Files.readAllBytes(temp.toPath());
	temp.delete();
    }

    public byte[] toByteArray() throws Exception 
    {
	final File temp = File.createTempFile("email-"+String.valueOf(jmailmsg.hashCode()), ".tmp");
	final FileOutputStream fs=new FileOutputStream(temp);
	jmailmsg.writeTo(fs);
	fs.flush();
	fs.close();
	final byte[] res = Files.readAllBytes(temp.toPath());
	temp.delete();
	return res;
    }

    // used to fill fields via .eml file stream
    /*
    public MailMessage loadMailFromFile(FileInputStream fs) throws Exception
    {
	MailMessage msg=new MailMessage();
	jmailmsg=new MimeMessage(session,fs);
	fs.close();
	readMessageBasicFields(msg);
	return msg;
    }
    */

    public void loadFromStream(InputStream fs) throws Exception
    {
	jmailmsg=new MimeMessage(session,fs);
	fs.close();
    }


    public void saveMailToFile(MailMessage msg,FileOutputStream fs) throws Exception
    {
	prepareInternalStore(msg);
	jmailmsg.writeTo(fs);
	fs.flush();
	fs.close();
    }
}
