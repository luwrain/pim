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

import java.net.URL;
import java.util.*;
import java.io.*;

import javax.mail.*;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.mail.search.ComparisonTerm;
import javax.mail.search.ReceivedDateTerm;
import javax.mail.internet.MimeUtility;

import org.luwrain.core.NullCheck;
import org.luwrain.pim.mail.*;

public class MailServerConversations
{
    static public final int SSL = 1;
    static public final int TLS = 2;

    /** max number messages count to load from big email folders when first loaded (limit for testing)*/
    static final int LIMIT_MESSAGES_LOAD = 5000;

    public interface Listener
    {
	void numberOfNewMessages(int count, boolean haveMore);
	boolean newMessage(MailMessage message, int num, int total);
    }

    final Properties props=new Properties();
    Session session=Session.getDefaultInstance(new Properties(), null); // by default was used empty session for working .eml files
    Store store;
    Session smtpSession;
    Transport smtpTransport;
    Folder folder;

    public void initPop3(String host,
			 int port,
			 String login,
			 String passwd,
			 int flags,
			 TreeMap<String,String> extraProps) throws Exception
    {
	final boolean ssl = (flags & SSL) != 0;
	final boolean tls = (flags & TLS) > 0;
	props.clear();
	props.put("mail.store.protocol", "pop3");
	props.put( "mail.pop3.auth", "true");
	props.put( "mail.pop3.host", host);
	props.put( "mail.pop3.user", login);
	props.put( "mail.pop3.password", passwd);
	props.put( "mail.pop3.port", "" + port);
	props.put( "mail.pop3.ssl.enable", ssl?"true":"false");
	props.put("mail.pop3.starttls.enable", tls?"true":"false");
	for(Map.Entry<String,String> p: extraProps.entrySet())
	    props.put(p.getKey(), p.getValue());
	session = Session.getInstance(props,null);
	//	session.setDebug(true);
	store = session.getStore();
	store.connect(host, login, passwd);
    }

    public void initSmtp(HashMap<String,String> settings,String host,
		  String login,
		  String password) throws Exception
    {
	props.clear();
	for(Map.Entry<String,String> p:settings.entrySet())
	    props.put(p.getKey(), p.getValue());
	final String l = login;
	final String p = password;
	smtpSession = Session.getInstance(props,
					  new Authenticator(){
					      protected PasswordAuthentication getPasswordAuthentication()
					      {
						  return new PasswordAuthentication(l, p);
					      }
					  });
	smtpTransport=smtpSession.getTransport("smtp");
	smtpTransport.connect();
    }

    public String[] getFolderNames() throws Exception
    {
	Folder[] folders=store.getDefaultFolder().list();
	String[] result=new String[folders.length];
	for(int i=0;i<folders.length;i++) 
	    result[i] = folders[i].getFullName();
	return result;
    }

    /** Fetches messages from the server.
     *
     * @param folderName Can be "INBOX" or any other IMAP folder name
     * @param listener The listener object to get information about fetching progress and the messages themselves
     */
    public void fetchMessages(String folderName, Listener listener, boolean deleteMessagesOnServer, HtmlPreview htmlPreview) throws Exception
    {
	NullCheck.notNull(folderName, "folderName");
	if (folderName.trim().isEmpty())
	    throw new IllegalArgumentException("folderName may not be empty");
	if(folder!=null) 
	    folder.close(true); // true - remove messages market to remove
	try {
	    folder = store.getFolder(folderName);
	    folder.open(Folder.READ_WRITE);
	    final int msgCount = folder.getMessageCount();
	    if (msgCount == 0)
	    {
		listener.numberOfNewMessages(0, false);
		return;
	    }
	    Message[] messages;
	    if (msgCount > LIMIT_MESSAGES_LOAD)
	    {
		listener.numberOfNewMessages(LIMIT_MESSAGES_LOAD, true);
		messages = folder.getMessages(1, LIMIT_MESSAGES_LOAD);
	    } else
	    {
		listener.numberOfNewMessages(msgCount, false);
		messages = folder.getMessages(1, msgCount);
	    }
	    if (Thread.currentThread().interrupted())
		throw new InterruptedException();
	    final MailEssentialJavamail es=new MailEssentialJavamail(); // FIXME: replace empty EmailEssentialJavamail to instance from luwrain.getSharedObject("luwrain.pim.email");  
	    for(int i = 0;i < messages.length;++i)
	    {
		if (Thread.currentThread().interrupted())
		    throw new InterruptedException();
		final MailMessage message=new MailMessage();
		es.jmailmsg=messages[i];
		es.fillBasicFields(message, htmlPreview);
message.messageId = es.getMessageId();
		message.rawMail = es.toByteArray();
		listener.newMessage(message, i, messages.length);
		if (deleteMessagesOnServer)
		    messages[i].setFlag(Flags.Flag.DELETED, true);
	    }
	    folder.close(deleteMessagesOnServer);
	    folder = null;
	}
	finally
	{
	    if (folder != null)
		folder.close(false);
	    folder = null;
	}
    }

    public void sendMessages(MailMessage[] emails) throws Exception
    {
	if(smtpTransport==null) 
	    throw new Exception("SMTP connection must be initialised");
	MailEssentialJavamail es=new MailEssentialJavamail();
	for(MailMessage message: emails)
	{
	    if (Thread.currentThread().interrupted())
		throw new InterruptedException();
	    es.load(message);
	    smtpTransport.sendMessage(es.jmailmsg,es.jmailmsg.getRecipients(RecipientType.TO));
	}
    }

    public void sendEncodedMessage(byte[] bytes) throws Exception
    {
	if(smtpTransport==null) 
	    throw new Exception("SMTP connection must be initialised");
	final MailEssentialJavamail es=new MailEssentialJavamail();
	es.load(new ByteArrayInputStream(bytes));
	smtpTransport.sendMessage(es.jmailmsg,es.jmailmsg.getRecipients(RecipientType.TO));
    }

    static public String makeAddress(String name, String addr) throws UnsupportedEncodingException
    {
	if (name == null || name.trim().isEmpty())
	    return addr;
	return MimeUtility.encodeText(name) + " <" + addr + ">";
    }
}
