/*
   Copyright 2012-2024 Michael Pozhidaev <msp@luwrain.org>

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

import java.io.*;
import java.util.*;

import javax.mail.*;
import javax.mail.internet.*;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import org.luwrain.core.*;

public class MailServerConversationsTest
{
    /*
    static private final File PROPERTY_FILE = new File(new File(System.getProperty("user.home")), "testing.properties");
    
    @Test public void sending() throws Exception
    {
	if (!PROPERTY_FILE.exists())
	    return;
	final Properties prop = new Properties();
	prop.load(new FileInputStream(PROPERTY_FILE));
	final String countStr = prop.getProperty("mail.smtp.count");
	if (countStr == null || countStr.trim().isEmpty())
	    return;
	final int count = Integer.parseInt(countStr);
	for(int i = 0;i < count;++i)
	{
	    final String prefix = "mail.smtp." + (i + 1) + ".";
	    final MailServerConversations.Params params = new MailServerConversations.Params();
	    params.doAuth = true;
	    params.host = prop.getProperty(prefix + "host");
	    assertNotNull(params.host);
	    	    params.port = Integer.parseInt(prop.getProperty(prefix + "port"));
	    params.login = prop.getProperty(prefix + "login");
	    //	    assertNotNull(params.login);
	    params.passwd = prop.getProperty(prefix + "passwd");
	    //	    assertNotNull(params.passwd);
	    params.ssl = prop.getProperty(prefix + "ssl").trim().toLowerCase().equals("yes");
	    	    params.tls = prop.getProperty(prefix + "tls").trim().toLowerCase().equals("yes");
		    System.out.println("kaka");
		    final MailServerConversations conv = new MailServerConversations(params);
		    final Message message = createMessage(conv.session, prop, prefix);
		    conv.send(conv.saveToByteArray(message));
	    
	}
    }

    private MimeMessage createMessage(Session session, Properties prop, String prefix) throws MessagingException
    {
	final MimeMessage message = new MimeMessage(session);
	//	    for(Map.Entry<String,String> e: headers.entrySet())
	//		storedMsg.addHeader(e.getKey(), e.getValue());
	message.setSubject(prop.getProperty(prefix + "subject"));
	message.setFrom(new InternetAddress(prop.getProperty(prefix + "login")));
	message.setRecipients(javax.mail.Message.RecipientType.TO, new Address[]{new InternetAddress(prop.getProperty(prefix + "to"))});
	message.setSentDate(new Date());
	message.setText(prop.getProperty(prefix + "text"));
	return message;
    }
    */
}
