/*
   Copyright 2012-2024 Michael Pozhidaev <msp@luwrain.org>
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

package org.luwrain.pim.mail2;

import java.io.*;
import java.util.*;

//import javax.activation.DataHandler;
//import javax.activation.FileDataSource;

import javax.mail.*;
import javax.mail.internet.*;
//import javax.mail.Flags.Flag;
//import javax.mail.Message.RecipientType;

//import org.luwrain.core.*;
import org.luwrain.pim.*;
import org.luwrain.pim.mail2.persistence.model.*;
//import org.luwrain.pim.mail.*;
//import org.luwrain.util.*;
//import org.luwrain.io.json.*;

import static org.luwrain.core.NullCheck.*;

public final class MessageDecoder implements MessageConsumer
{
    /*
    static public final String
	TRANSFER_ENCODING = "Content-Transfer-Encoding",
	DEFAULT_CHARSET = "UTF-8";
    */


@Override public void onMessage(Message message)
    {
		    final var session = Session.getDefaultInstance(new Properties(), null);
	final MimeMessage mm;
	final var m = message.metadata = new MessageMetadata();
	try {
		try (final var byteStream = new ByteArrayInputStream(message.rawMessage)) {
mm = new MimeMessage(session, byteStream);
	}
	    m.setSubject(mm.getSubject());
	if(mm.getFrom() != null)
	    m.setFromAddr(MimeUtility.decodeText(mm.getFrom()[0].toString()));
	/*
	if (srcMsg.getRecipients(RecipientType.TO) != null)
	    dest.setTo(decodeAddrs(srcMsg.getRecipients(RecipientType.TO)));
	if (srcMsg.getRecipients(RecipientType.CC) != null)
	    dest.setCc(decodeAddrs(srcMsg.getRecipients(RecipientType.CC)));
	if (srcMsg.getRecipients(RecipientType.BCC) != null)
	    dest.setBcc(decodeAddrs(srcMsg.getRecipients(RecipientType.BCC)));
	dest.setSentDate(srcMsg.getSentDate());
	dest.setReceivedDate(srcMsg.getReceivedDate());
	final MimePartCollector collector = new MimePartCollector();
	dest.setText(collector.run(srcMsg.getContent(), srcMsg.getContentType(), "", ""));
	dest.setAttachments(collector.attachments.toArray(new String[collector.attachments.size()]));
	dest.setContentType(srcMsg.getContentType());
	*/
	}
	catch(Exception e)
	{
	    throw new PimException(e);
	}
    }

    /*
    static public String decodeText(String text) throws IOException
    {
	notNull(text, "text");
	return MimeUtility.decodeText(text);
    }
	*/

    static private String[] decodeAddrs(Address[] addrs) throws IOException
    {
	if (addrs == null)
	    return new String[0];
	final List<String> res=new ArrayList<>();
	for(int i = 0;i < addrs.length;++i)
	    if (addrs[i] != null)
		res.add(MimeUtility.decodeText(addrs[i].toString()));
	return res.toArray(new String[res.size()]);
    }


    /*
    static class MimePartCollector
    {
	final List<String> attachments = new ArrayList<>();
	String run(Object o, String contentType,
		   String fileName, String disposition) throws PimException, IOException, MessagingException
	{
	    if(o instanceof MimeMultipart)
	    {
		final Multipart content =(Multipart)o;
		final boolean alternative = (contentType.toLowerCase().indexOf("alternative") >= 0);
		final StringBuilder textRes = new StringBuilder();
		final StringBuilder htmlRes = new StringBuilder();
		for(int i=0;i<content.getCount();i++)
		{
		    final MimeBodyPart part = (MimeBodyPart) content.getBodyPart(i);
		    if (part == null || part.getContent() == null)
			continue;
		    final String partContentType = part.getContentType() != null?part.getContentType():"";
		    final boolean html = (partContentType.toLowerCase().indexOf("html") >= 0);
		    if (html)
			htmlRes.append(run(part.getContent(), partContentType, 
					   part.getFileName() != null?part.getFileName():"", 
					   part.getDisposition() != null?part.getDisposition():"")); else
			textRes.append(run(part.getContent(), partContentType, 
					   part.getFileName() != null?part.getFileName():"" , 
					   part.getDisposition() != null?part.getDisposition():""));
		}
		final String textStr = textRes.toString();
		final String htmlStr = htmlRes.toString();
		if (!alternative)
		    return textStr + "\n" + htmlStr;
		if (!textStr.trim().isEmpty())
		    return textStr;
		return htmlStr;
	    }
	    if ((disposition != null && disposition.toLowerCase().indexOf("attachment") >= 0) ||
		contentType.toLowerCase().indexOf("text") < 0)
	    {
		if (fileName != null && !fileName.trim().isEmpty())
		{
		    attachments.add(MimeUtility.decodeText(fileName));
		    onAttachment(MimeUtility.decodeText(fileName), o);
		} else
		{
		    attachments.add(contentType);
		    onAttachment(contentType, o);
		}
		return "";
	    }
	    if (contentType != null && contentType.toLowerCase().indexOf("html") >= 0)
		return o.toString();
	    return o.toString();
	}
	protected void onAttachment(String fileName, Object obj) throws IOException
	{
	}
    }
    */
}
