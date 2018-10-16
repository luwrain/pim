/*
   Copyright 2012-2018 Michael Pozhidaev <michael.pozhidaev@gmail.com>

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
import org.luwrain.network.*;
import org.luwrain.util.*;
import org.luwrain.pim.PimException;

public final class MailServerConversations
{
    static private final String TRUE = "true";
    static private final String FALSE = "false";
    
    static public final int SSL = 1;
    static public final int TLS = 2;

    static final int LIMIT_MESSAGES_LOAD = 5000;

    public interface Listener
    {
	void numberOfNewMessages(int count, boolean haveMore);
	boolean newMessage(byte[] message, int num, int total);
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
    //    private final Session session=Session.getDefaultInstance(new Properties(), null);
public final Session session;
    private Store store = null;
    //    private Session smtpSession = null;
    private Transport smtpTransport = null;

    public MailServerConversations(Params params) throws PimException
    {
	NullCheck.notNull(params, "params");
	NullCheck.notEmpty(params.host, "params.host");
	this.props = new Properties();
        props.put("mail.smtp.auth", params.doAuth?TRUE:FALSE);
        props.put("mail.smtp.host", params.host);
        props.put("mail.smtp.port", "" + String.valueOf(params.port));
	props.put("mail.smtp.ssl.enable", params.ssl?TRUE:FALSE);
	props.put("mail.smtp.starttls.enable", params.tls?TRUE:FALSE);
	//	final String l = login;
	//	final String p = passwd;
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
	}


        /** Fetching of messages from the server.
     *
     * @param folderName Can be "INBOX" or any other IMAP folder name
     * @param listener The listener object to get information about fetching progress
     * @param deleteMessagesOnServer Must be true, if successfully fetched messages must be deleted on the server
     */
    public void fetchPop3(String folderName, Listener listener, boolean deleteMessagesOnServer) throws IOException, InterruptedException
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
		for(int i = 0;i < messages.length;++i)
		{
		    if (Thread.currentThread().interrupted())
			throw new InterruptedException();
		    listener.newMessage(saveToByteArray(messages[i]), i, messages.length);
		    if (deleteMessagesOnServer)
			messages[i].setFlag(Flags.Flag.DELETED, true);
		}
	    }
	    finally {
		if (folder != null)
		    folder.close(true);
	    }
	}
	catch(MessagingException | UnsupportedEncodingException e)
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
	    //	    session = Session.getInstance(props,null);
	    store = session.getStore();
	    store.connect(host, login, passwd);
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
