
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
    public Message jmailmsg;

    public void loadFrom(MailMessage msg) throws PimException
    {
	NullCheck.notNull(msg, "msg");
	NullCheck.notNull(msg.subject, "msg.subject");
	NullCheck.notEmpty(msg.from, "msg.from");
	NullCheck.notEmptyArray(msg.to, "msg.to");
	NullCheck.notEmptyItems(msg.to, "msg.to");
	NullCheck.notEmptyItems(msg.cc, "msg.cc");
	NullCheck.notEmptyItems(msg.bcc, "msg.bcc");
	NullCheck.notEmptyItems(msg.attachments, "msg.attachments");
	try {
	    jmailmsg=new MimeMessage(session);
	    jmailmsg.setSubject(msg.subject);
	    jmailmsg.setFrom(new InternetAddress(msg.from));
	    jmailmsg.setRecipients(RecipientType.TO, MailAddress.makeInternetAddrs(msg.to));
	    if(msg.cc.length>0)
		jmailmsg.setRecipients(RecipientType.CC, MailAddress.makeInternetAddrs(msg.cc));
	    if(msg.bcc.length>0)
		jmailmsg.setRecipients(RecipientType.BCC, MailAddress.makeInternetAddrs(msg.bcc));
	    if(msg.sentDate!=null)
		jmailmsg.setSentDate(msg.sentDate);
	    if(msg.attachments.length > 0)
	    {
		final Multipart mp = new MimeMultipart();
		MimeBodyPart part = new MimeBodyPart();
		part.setText(msg.baseContent);
		// TODO: need to repair - in multipart message mimeContentType of baseContent was ignored
		mp.addBodyPart(part);
		for(String fn:msg.attachments)
		{
		    part = new MimeBodyPart();
		    Path pfn=Paths.get(fn);
		    part.setFileName(MimeUtility.encodeText(pfn.getFileName().toString()));
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
	catch(MessagingException | UnsupportedEncodingException e)
	{
	    throw new PimException(e);
	}
    }

    public void loadFrom(InputStream fs) throws PimException, IOException
    {
	NullCheck.notNull(fs, "fs");
	try {
	    jmailmsg=new MimeMessage(session,fs);
	    fs.close();
	}
	catch(MessagingException e)
	{
	    throw new PimException(e);
	}
	}

    public void loadFrom(byte[] bytes) throws PimException, IOException
    {
	NullCheck.notNull(bytes, "bytes");
	loadFrom(new ByteArrayInputStream(bytes));
    }

    void saveTo(MailMessage msg, HtmlPreview htmlPreview) throws MessagingException, UnsupportedEncodingException, IOException
    {
	NullCheck.notNull(msg, "msg");
	NullCheck.notNull(htmlPreview, "htmlPreview");
	msg.subject=jmailmsg.getSubject();
	if (msg.subject == null)
	    msg.subject = "";
	if(jmailmsg.getFrom()!=null)
	    msg.from = MimeUtility.decodeText(jmailmsg.getFrom()[0].toString()); else
	    msg.from = "";
	if(jmailmsg.getRecipients(RecipientType.TO)!=null)
	    msg.to = MailAddress.decodeAddrs(jmailmsg.getRecipients(RecipientType.TO)); else
	    msg.to = new String[0];
	if(jmailmsg.getRecipients(RecipientType.CC)!=null)
	    msg.cc = MailAddress.decodeAddrs(jmailmsg.getRecipients(RecipientType.CC)); else
	    msg.cc = new String[0];
	if(jmailmsg.getRecipients(RecipientType.BCC)!=null)
	    msg.bcc = MailAddress.decodeAddrs(jmailmsg.getRecipients(RecipientType.BCC)); else
	    msg.bcc = new String[0];
	msg.sentDate=jmailmsg.getSentDate();
	msg.receivedDate=jmailmsg.getReceivedDate();
	if (msg.receivedDate == null)
	    msg.receivedDate = new java.util.Date();
	final MimePartCollector collector = new MimePartCollector(htmlPreview);
	msg.baseContent = collector.run(jmailmsg.getContent(), jmailmsg.getContentType(), "", "");
	msg.attachments = collector.attachments.toArray(new String[collector.attachments.size()]);
	//	msg.baseContent = collector.body.toString();
	msg.mimeContentType = jmailmsg.getContentType();
    }

    public void saveTo(FileOutputStream fs) throws MessagingException, IOException
    {
	NullCheck.notNull(fs, "fs");
	jmailmsg.writeTo(fs);
	fs.flush();
	fs.close();
    }

    public byte[] saveToByteArray() throws IOException, PimException
    {
	try {
	    final File temp = File.createTempFile("email-"+String.valueOf(jmailmsg.hashCode()), ".tmp");
	    try {
		FileOutputStream fs=new FileOutputStream(temp);
		jmailmsg.writeTo(fs);
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

    String getMessageId() throws PimException, MessagingException
    {
	if(jmailmsg instanceof IMAPMessage)
	{
	    final IMAPMessage imessage=((IMAPMessage)jmailmsg);
return imessage.getMessageID();
	} else 
	    if(jmailmsg instanceof POP3Message)
	    {
		final POP3Message pmessage=((POP3Message)jmailmsg);
		return pmessage.getMessageID();
	    } else
	throw new PimException("Unknown email Message class "+jmailmsg.getClass().getName());
    }

    public String[] getReplyTo(boolean decode) throws PimException
    {
	try {
	    final Address[] addr = jmailmsg.getReplyTo();
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

    public String getWideReplyCcList(boolean decode) throws PimException
    {
	try {
	    final LinkedList<Address> addrs = new LinkedList<Address>();
	    if (jmailmsg.getRecipients(RecipientType.CC) != null)
		for(Address a: jmailmsg.getRecipients(RecipientType.CC)) 
		    if (a != null)
			addrs.add(a);
	    if (jmailmsg.getRecipients(RecipientType.TO) != null)
		for(Address a: jmailmsg.getRecipients(RecipientType.TO))
		    if (a != null)
			addrs.add(a);
	    final Address addrs2[] = addrs.toArray(new Address[addrs.size()]);
	    if (addrs2.length < 1)
		return "";
	    //	    final LinkedList<String> res = new LinkedList<String>();
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
	    saving.run(jmailmsg.getContent(), jmailmsg.getContentType(), "", "");
	    return saving.result() == MailAttachmentSaving.SUCCESS;
	}
	catch(IOException | MessagingException e)
	{
	    e.printStackTrace();
	    throw new PimException(e);
	}
    }
}
