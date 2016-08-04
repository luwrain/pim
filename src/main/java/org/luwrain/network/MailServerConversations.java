/*
   Copyright 2012-2016 Michael Pozhidaev <michael.pozhidaev@gmail.com>
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

package org.luwrain.network;

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

import org.luwrain.util.*;
import org.luwrain.pim.PimException;

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

    private final Properties props=new Properties();
    private Session session=Session.getDefaultInstance(new Properties(), null);
    private Store store;
    private Session smtpSession;
    private Transport smtpTransport;
    //    private Folder folder;

    /**
     * Prepares properties, session and store objects for operations through
     * POP3 protocol.
     *
     * @param host The host to connect to
     * @param port port Port number to use for connection
     * @param login The login to be used for authorizing
     * @param passwd The password to be used for authorizing
     * @param flags The flags to be used for filling the properties (SSL, TLS, etc)
     * @param extraProps The extra properties to be used for connection
     */
    public void initPop3(String host, int port,
			 String login, String passwd,
			 int flags, TreeMap<String,String> extraProps) throws PimException
    {
	NullCheck.notEmpty(host, "host");
	NullCheck.notNull(login, "login");
	NullCheck.notNull(passwd, "passwd");
	NullCheck.notNull(extraProps, "extraProps");
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
	try {
	    session = Session.getInstance(props,null);
	    store = session.getStore();
	    store.connect(host, login, passwd);
	}
	catch(MessagingException e)
	{
	    throw new PimException(e);
	}
    }

    public void initSmtp(HashMap<String,String> settings,String host,
		  String login, String passwd) throws PimException
    {
	NullCheck.notEmpty(host, "host");
	NullCheck.notNull(login, "login");
	NullCheck.notNull(passwd, "passwd");
	props.clear();
	for(Map.Entry<String,String> p:settings.entrySet())
	    props.put(p.getKey(), p.getValue());
	final String l = login;
	final String p = passwd;
	try {
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
	catch(MessagingException e)
	{
	    throw new PimException(e);
	}
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
    public void fetchMessages(String folderName, Listener listener,
			      boolean deleteMessagesOnServer, HtmlPreview htmlPreview) throws PimException, IOException, InterruptedException
    {
	NullCheck.notEmpty(folderName, "folderName");
	NullCheck.notNull(listener, "listener");
	try {
	    Folder folder = null;
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
		final MailUtils util = new MailUtils();
		for(int i = 0;i < messages.length;++i)
		{
		    if (Thread.currentThread().interrupted())
			throw new InterruptedException();
		    final MailMessage message=new MailMessage();
		    util.jmailmsg=messages[i];
		    util.fillBasicFields(message, htmlPreview);
		    message.messageId = util.getMessageId();
		    message.rawMail = util.toByteArray();
		    listener.newMessage(message, i, messages.length);
		    if (deleteMessagesOnServer)
			messages[i].setFlag(Flags.Flag.DELETED, true);
		}
	    }
	    finally {
		if (folder != null)
		    folder.close(false);
	    }
	}
	catch(MessagingException | UnsupportedEncodingException e)
	{
	    throw new PimException(e);
	}
    }

    public void sendMessages(MailMessage[] emails) throws Exception
    {
	NullCheck.notNullItems(emails, "emails");
	NullCheck.notNull(smtpTransport, "smtpTransport");
	final MailUtils util = new MailUtils();
	for(MailMessage message: emails)
	{
	    if (Thread.currentThread().interrupted())
		throw new InterruptedException();
	    util.load(message);
	    smtpTransport.sendMessage(util.jmailmsg,util.jmailmsg.getRecipients(RecipientType.TO));
	}
    }

    public void sendEncodedMessage(byte[] bytes) throws Exception
    {
	NullCheck.notNull(smtpTransport, "smtpTransport");
	final MailUtils util=new MailUtils();
	util.load(new ByteArrayInputStream(bytes));
	smtpTransport.sendMessage(util.jmailmsg, util.jmailmsg.getRecipients(RecipientType.TO));
    }

    static public String makeAddress(String name, String addr) throws UnsupportedEncodingException
    {
	if (name == null || name.trim().isEmpty())
	    return addr;
	return MimeUtility.encodeText(name) + " <" + addr + ">";
    }
}
