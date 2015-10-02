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

import org.luwrain.pim.mail.MailStoringSql.Condition;
import org.luwrain.pim.mail.*;

public class MailEssentialJavamail implements MailEssential
{
    public Message jmailmsg;

    // make MimeMessage from class fields
    @Override public void PrepareInternalStore(MailMessage msg) throws Exception
    {
	jmailmsg=new MimeMessage(session);
	jmailmsg.setSubject(msg.subject);
	if(msg.from!=null)
	    jmailmsg.setFrom(new InternetAddress(msg.from));
	if(msg.to!=null&&msg.to.length>0)
	{
	    int i=0;
	    InternetAddress[] addr_to=new InternetAddress[msg.to.length];  
	    for(String addr:msg.to)
		addr_to[i++]=new InternetAddress(addr);
	    jmailmsg.setRecipients(RecipientType.TO, addr_to);
	}
	if(msg.cc!=null&&msg.cc.length>0)
	{
	    int i=0;
	    InternetAddress[] addr_cc=new InternetAddress[msg.cc.length];  
	    for(String addr:msg.cc)
		addr_cc[i++]=new InternetAddress(addr);
	    jmailmsg.setRecipients(RecipientType.CC, addr_cc);
	}
	if(msg.bcc!=null&&msg.bcc.length>0)
	{
	    int i=0;
	    InternetAddress[] addr_bcc=new InternetAddress[msg.bcc.length];  
	    for(String addr:msg.bcc)
		addr_bcc[i++]=new InternetAddress(addr);
	    jmailmsg.setRecipients(RecipientType.BCC, addr_bcc);
	}
	if(msg.sentDate!=null)
	    jmailmsg.setSentDate(msg.sentDate);
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

    // used to fill standart simple mime mail message fields (message can be Mime..., POP3... or IMAP... Message class)
    public void readJavamailMessageBaseFields(MailMessage msg) throws Exception
    {
	msg.subject=jmailmsg.getSubject();
	if(jmailmsg.getFrom()!=null)
	{
	    msg.from=jmailmsg.getFrom()[0].toString();
	    //	    InternetAddress inetAddr = (InternetAddress)jmailmsg.getFrom();
	    //msg.from = inetAddr.getAddress();
	} else 
	    msg.from=null;
	if(jmailmsg.getRecipients(RecipientType.TO)!=null)
	{
	    Vector<String> to=new Vector<String>();
	    for(Address addr:jmailmsg.getRecipients(RecipientType.TO))
		to.add(addr.toString());
	    msg.to=to.toArray(new String[to.size()]);
	} else
	    msg.to=null;
	if(jmailmsg.getRecipients(RecipientType.CC)!=null)
	{
	    Vector<String> to=new Vector<String>();
	    for(Address addr:jmailmsg.getRecipients(RecipientType.CC)) to.add(addr.toString());
	    msg.cc=to.toArray(new String[to.size()]);
	} else 
	    msg.cc=null;
	if(jmailmsg.getRecipients(RecipientType.BCC)!=null)
	{
	    Vector<String> to=new Vector<String>();
	    for(Address addr:jmailmsg.getRecipients(RecipientType.BCC)) to.add(addr.toString());
	    msg.bcc=to.toArray(new String[to.size()]);
	} else 
	    msg.bcc=null;
	//	msg.isReaded=!jmailmsg.getFlags().contains(Flag.SEEN);
	msg.sentDate=jmailmsg.getSentDate();
	msg.receivedDate=jmailmsg.getReceivedDate();
	if (msg.receivedDate == null)
	    msg.receivedDate = new java.util.Date();
	// message body
	if(jmailmsg.getContent().getClass()==MimeMultipart.class)
	{
	    Multipart content =(Multipart)jmailmsg.getContent();
	    MimeBodyPart file = (MimeBodyPart) content.getBodyPart(0); // first file of multipart is a message body 
	    msg.baseContent=file.getContent().toString();
	    // get attachments
	    for(int i=1;i<content.getCount();i++)
	    {
		file = (MimeBodyPart) content.getBodyPart(i);
		file.getContentID();
		file.getFileName();
	    }
	} else
	{
	    msg.baseContent=jmailmsg.getContent().toString();
	}
	msg.mimeContentType=jmailmsg.getContentType();
    }

    // used to load addition fields from Message POP3 or IMAP online
    public void readJavamailMessageOnline(MailMessage msg) throws Exception
    {
	if(jmailmsg instanceof IMAPMessage)
	{
	    IMAPMessage imessage=((IMAPMessage)jmailmsg);
	    msg.messageId=imessage.getMessageID();
	} else 
	    if(jmailmsg instanceof POP3Message)
	    {
		POP3Message pmessage=((POP3Message)jmailmsg);
		msg.messageId=pmessage.getMessageID();
	    } else
	throw new Exception("Unknown email Message class "+jmailmsg.getClass().getName());
    }

    public void readJavamailMessageContent(MailMessage msg) throws Exception
    {
	final File temp = File.createTempFile("email-"+String.valueOf(jmailmsg.hashCode()), ".tmp");
	System.out.println("saving " + temp.getAbsolutePath());
	FileOutputStream fs=new FileOutputStream(temp);
	//	SaveMailToFile(msg,fs);
	jmailmsg.writeTo(fs);
	fs.flush();
	fs.close();
	msg.rawMail = Files.readAllBytes(temp.toPath());
					 //FIXME:Delete temp;
    }

    Session session=Session.getDefaultInstance(new Properties(), null); // by default was used empty session for working .eml files

    // used to fill fields via .eml file stream
    @Override public MailMessage loadMailFromFile(FileInputStream fs) throws Exception
    {
	MailMessage msg=new MailMessage();
	jmailmsg=new MimeMessage(session,fs);
	fs.close();
	readJavamailMessageBaseFields(msg);
	return msg;
    }

    // used to save fields to .eml field stream
    @Override public void SaveMailToFile(MailMessage msg,FileOutputStream fs) throws Exception
    {
	PrepareInternalStore(msg);
	jmailmsg.writeTo(fs);
	fs.flush();
	fs.close();
    }
}
