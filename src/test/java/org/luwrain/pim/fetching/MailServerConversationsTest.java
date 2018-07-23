
package org.luwrain.pim.fetching;

import java.io.*;
import java.util.*;

import javax.mail.*;
import javax.mail.internet.*;

import org.junit.*;

import org.luwrain.core.*;

public class MailServerConversationsTest extends Assert
{
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
}
