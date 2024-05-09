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

import org.apache.logging.log4j.*;
//import javax.activation.DataHandler;
//import javax.activation.FileDataSource;

import javax.mail.*;
import javax.mail.internet.*;
//import javax.mail.Flags.Flag;
import javax.mail.Message.RecipientType;

//import org.luwrain.core.*;
import org.luwrain.pim.*;
import org.luwrain.pim.mail2.persistence.model.*;
//import org.luwrain.pim.mail.*;
//import org.luwrain.util.*;
//import org.luwrain.io.json.*;

import static org.luwrain.core.NullCheck.*;

public final class MessageDecoder implements MessageConsumer
{
    static private final Logger log = LogManager.getLogger();

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
	if (mm.getRecipients(RecipientType.TO) != null)
	    m.setToAddr(decodeAddrs(mm.getRecipients(RecipientType.TO)));
	m.setSentTimestamp(mm.getSentDate().getTime());
	log.trace("Decoding message body");
	final var e = new MimePartsExtractor();
	e.on(mm);
	m.setContent(MessageContentItem.toJson(e.items));
	/*
	if (srcMsg.getRecipients(RecipientType.CC) != null)
	    dest.setCc(decodeAddrs(srcMsg.getRecipients(RecipientType.CC)));
	if (srcMsg.getRecipients(RecipientType.BCC) != null)
	    dest.setBcc(decodeAddrs(srcMsg.getRecipients(RecipientType.BCC)));
	dest.setReceivedDate(srcMsg.getReceivedDate());
	final MimePartCollector collector = new MimePartCollector();
	dest.setText(collector.run(srcMsg.getContent(), srcMsg.getContentType(), "", ""));
	dest.setAttachments(collector.attachments.toArray(new String[collector.attachments.size()]));
	dest.setContentType(srcMsg.getContentType());
	*/
	}
	catch(Exception ex)
	{
	    log.catching(ex);
	    throw new PimException(ex);
	}
    }

    static private List<String> decodeAddrs(Address[] addrs) throws IOException
    {
	if (addrs == null)
	    return Arrays.asList();
	final List<String> res=new ArrayList<>();
	for(int i = 0;i < addrs.length;++i)
	    if (addrs[i] != null)
		res.add(MimeUtility.decodeText(addrs[i].toString()));
	//	return res.toArray(new String[res.size()]);
	return res;
    }
}
