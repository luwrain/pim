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
import javax.mail.*;
import javax.mail.internet.*;

import static javax.mail.internet.MimeUtility.*;

final class MimePartsExtractor
{
    static private final Logger log = LogManager.getLogger();

    final List<MessageContentItem> items = new ArrayList<>();

        void on(MimePart p) throws IOException, MessagingException
    {
	on(p, false);
    }

    void on(MimePart p, boolean alternative) throws IOException, MessagingException
    {
	if (p == null || p.getContent() == null)
	    return;
			final String
			contentType = p.getContentType() != null?decodeText(p.getContentType()):"",
			fileName = p.getFileName() != null?decodeText(p.getFileName()):null,
		disposition = p.getDisposition();
			/*
			log.trace("MIME part contentType=" + contentType + ", disposition=" + disposition + ", fileName=" + fileName);
			log.trace("Class is " + p.getContent().getClass().getName());
			if (p.getContent() instanceof String s)
			    log.trace(s);
			*/
	if(p.getContent() instanceof MimeMultipart mm)
	{
	    	    for(int i = 0;i < mm.getCount();i++)
			on((MimeBodyPart)mm.getBodyPart(i), alternative || contentType.toLowerCase().startsWith("multipart/alternative"));
		return;
	    }
	final var item = new MessageContentItem();
	item.setContentType(contentType);
	item.setDisposition(disposition);
	item.setFileName(fileName);
	item.setAlternative(alternative);
	if (p.getContent() instanceof String s)
	    item.setText(s);
	items.add(item);
    }

    private void handle(MimeMultipart mm) throws IOException, MessagingException
    {
	    for(int i = 0;i < mm.getCount();i++)
	    {
		final var p = (MimeBodyPart)mm.getBodyPart(i);
		if (p == null)
		    continue;
		on(p);
	    }
    }

}
