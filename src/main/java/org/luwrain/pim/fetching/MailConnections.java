/*
   Copyright 2012-2021 Michael Pozhidaev <msp@luwrain.org>
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

package org.luwrain.pim.fetching;

import java.net.URL;
import java.util.*;
import java.io.*;

import javax.mail.*;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.mail.search.ComparisonTerm;
import javax.mail.search.ReceivedDateTerm;
import javax.mail.internet.MimeUtility;

import org.luwrain.core.*;
import org.luwrain.util.*;
import org.luwrain.pim.PimException;

public final class MailConnections
{
    static private final String LOG_COMPONENT = Base.LOG_COMPONENT;
    static final int LIMIT_MESSAGES_LOAD = 500;

    public interface Listener
    {
	void numberOfNewMessages(int count, boolean haveMore);
	boolean saveMessage(byte[] message, int num, int total);
    }

    static public final class Params
    {
	public boolean doAuth = false;
	public String host = "";
	public int port = 25;
	public String login;
	public String passwd;
	public boolean ssl = false;
	public boolean tls = false;
	public Map<String, String> extProps = new HashMap();
    }

    private final Properties props;
    private final Session session;
    private final Store store;
    private final Transport smtpTransport;

    public MailConnections(Params params, boolean pop3) throws PimException
    {
	NullCheck.notNull(params, "params");
	NullCheck.notEmpty(params.host, "params.host");
	NullCheck.notNull(params.login, "params.login");
	NullCheck.notNull(params.passwd, "params.passwd");
	this.props = new Properties();
	if (pop3)
	{
	    props.put("mail.store.protocol", "pop3");
	    props.put( "mail.pop3.auth", new Boolean(params.doAuth).toString());
	    props.put( "mail.pop3.host", params.host);
	    props.put( "mail.pop3.user", params.login);
	    props.put( "mail.pop3.password", params.passwd);
	    props.put( "mail.pop3.port", String.valueOf(params.port));
	    props.put( "mail.pop3.ssl.enable", new Boolean(params.ssl).toString());
	    props.put("mail.pop3.starttls.enable", new Boolean(params.tls).toString());
	    for(Map.Entry<String,String> p: params.extProps.entrySet())
		props.put(p.getKey(), p.getValue());
	    try {
		this.session = Session.getInstance(props,null);
		this.store = session.getStore();
		Log.debug(LOG_COMPONENT, "store.connect(" + params.host + ", " + params.login + ")");
		store.connect(params.host, params.port, params.login, params.passwd);
		Log.debug(LOG_COMPONENT, "connected to " + params.host);
	    }
	    catch(Throwable e)
	    {
		Log.error(LOG_COMPONENT, "connecting to " + params.host + ":" + params.port + ": " + e.getClass().getName() + ":" + e.getMessage());
		throw new PimException(e);
	    }
	    this.smtpTransport = null;
	} else
	{
	    props.put("mail.smtp.auth", new Boolean(params.doAuth));
	    props.put("mail.smtp.host", params.host);
	    props.put("mail.smtp.port", new Integer(params.port).toString());
		      props.put("mail.smtp.ssl.enable", new Boolean(params.ssl).toString());
		      props.put("mail.smtp.starttls.enable", new Boolean(params.tls).toString());
	    try {
		this.session = Session.getInstance(props,
						   new Authenticator(){
						       protected PasswordAuthentication getPasswordAuthentication()
						       {
							   return new PasswordAuthentication(params.login, params.passwd);
						       }
						   });
		smtpTransport = session.getTransport("smtp");
		smtpTransport.connect();
	    }
	    catch(MessagingException e)
	    {
		throw new PimException(e);
	    }
	    this.store = null;
	}
    }


        /** Fetching of messages from the server.
     *
     * @param folderName Can be "INBOX" or any other IMAP folder name
     * @param listener The listener object to get information about fetching progress
     * @param deleteMessagesOnServer Must be true, if successfully fetched messages must be deleted on the server
     */
    public boolean fetchPop3(String folderName, Listener listener, boolean deleteMessagesOnServer) throws IOException, InterruptedException
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
		    return true;
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
		for(int i = 0;i < messages.length;++i)
		{
		    if (Thread.currentThread().interrupted())
			throw new InterruptedException();
		    if (!listener.saveMessage(saveToByteArray(messages[i]), i, messages.length))
			return false;
		    if (deleteMessagesOnServer)
			messages[i].setFlag(Flags.Flag.DELETED, true);
		}
		return true;
	    }
	    finally {
		if (folder != null)
		    folder.close(true);
	    }
	}
	catch(MessagingException e)
	{
	    throw new IOException("Unable to fetch messages from the server", e);
	}
    }

    public void send(byte[] bytes) throws FetchingException
    {
	NullCheck.notNull(smtpTransport, "smtpTransport");
	try {
	    final Message message = loadFromByteArray(bytes);
	    smtpTransport.sendMessage(message, message.getRecipients(RecipientType.TO));
	}
	catch(MessagingException | IOException e)
	{
	    throw new FetchingException(e);
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

    public byte[] saveToByteArray(Message message) throws MessagingException, IOException
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

    public Session getSession()
    {
	return session;
    }

    Message loadFromByteArray(byte[] bytes) throws MessagingException, IOException
    {
	NullCheck.notNull(bytes, "bytes");
	final InputStream is = new ByteArrayInputStream(bytes);
	try {
	    return new javax.mail.internet.MimeMessage(session, is);
	}
	finally {
	    is.close();
	}
    }
}
