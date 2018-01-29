/*
   Copyright 2012-2017 Michael Pozhidaev <michael.pozhidaev@gmail.com>
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

package org.luwrain.pim.mail;

import java.io.*;
import java.util.*;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;

import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.Flags.Flag;
import javax.mail.Message.RecipientType;

import org.luwrain.core.*;
import org.luwrain.pim.*;
import org.luwrain.pim.mail.*;

public final class BinaryMessage
{
    static public byte[] toByteArray(MailMessage message, Map<String, String> extraHeaders) throws MessagingException, UnsupportedEncodingException, IOException
    {
	NullCheck.notNull(message, "message");
	NullCheck.notNull(extraHeaders, "extraHeaders");
	final javax.mail.internet.MimeMessage mimeMessage = convert(message, extraHeaders);
	return toByteArray(mimeMessage);
    }

    static private javax.mail.internet.MimeMessage convert(MailMessage srcMsg, Map<String, String> headers) throws MessagingException, UnsupportedEncodingException
    {
	NullCheck.notNull(srcMsg, "srcMsg");
	NullCheck.notNull(headers, "headers");
	NullCheck.notNull(srcMsg.subject, "srcMsg.subject");
	NullCheck.notEmpty(srcMsg.from, "srcMsg.from");
	NullCheck.notEmptyArray(srcMsg.to, "srcMsg.to");
	NullCheck.notEmptyItems(srcMsg.to, "srcMsg.to");
	NullCheck.notEmptyItems(srcMsg.cc, "srcMsg.cc");
	NullCheck.notEmptyItems(srcMsg.bcc, "srcMsg.bcc");
	NullCheck.notEmptyItems(srcMsg.attachments, "srcMsg.attachments");
	final Session session = Session.getDefaultInstance(new Properties(), null);
	final javax.mail.internet.MimeMessage message = new MimeMessage(session);;
	for(Map.Entry<String,String> e: headers.entrySet())
	    message.addHeader(e.getKey(), e.getValue());
	message.setSubject(srcMsg.subject);
	message.setFrom(new InternetAddress(srcMsg.from));
	message.setRecipients(RecipientType.TO, AddressUtils.makeInternetAddrs(srcMsg.to));
	if(srcMsg.cc.length>0)
	    message.setRecipients(RecipientType.CC, AddressUtils.makeInternetAddrs(srcMsg.cc));
	if(srcMsg.bcc.length>0)
	    message.setRecipients(RecipientType.BCC, AddressUtils.makeInternetAddrs(srcMsg.bcc));
	if(srcMsg.sentDate!=null)
	    message.setSentDate(srcMsg.sentDate);
	if(srcMsg.attachments.length > 0)
	{
	    final Multipart mp = new MimeMultipart();
	    MimeBodyPart part = new MimeBodyPart();
	    part.setText(srcMsg.baseContent);
	    mp.addBodyPart(part);
	    for(String fn:srcMsg.attachments)
	    {
		part = new MimeBodyPart();
		final File pfn = new File(fn);
		part.setFileName(MimeUtility.encodeText(pfn.getName()));
		final FileDataSource fds = new FileDataSource(fn);
		part.setDataHandler(new DataHandler(fds));
		mp.addBodyPart(part);
	    }
	    message.setContent(mp);
	} else
	{
	    if(srcMsg.mimeContentType==null)
	    { // simple text email body
		message.setText(srcMsg.baseContent);
	    } else
	    { // for example utf8 html - mimeContentType="text/html; charset=utf-8"
		message.setContent(srcMsg.baseContent,srcMsg.mimeContentType);
	    }
	}
	return message;
    }

    static private byte[] toByteArray(javax.mail.Message message) throws MessagingException, IOException
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
}
