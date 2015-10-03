/*
   Copyright 2012-2015 Michael Pozhidaev <michael.pozhidaev@gmail.com>
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

package org.luwrain.util;

import java.util.*;
import java.io.*;
import javax.mail.*;
import javax.mail.internet.*;


class MimePartCollector
{
    final LinkedList<String> attachments = new LinkedList<String>();
    final StringBuilder body = new StringBuilder();

    void run(Object o,
	     String contentType, String fileName) throws IOException, MessagingException
    {
	if(o instanceof MimeMultipart)
	{
	    final Multipart content =(Multipart)o;
	    for(int i=0;i<content.getCount();i++)
	    {
		final MimeBodyPart part = (MimeBodyPart) content.getBodyPart(i);
		run(part.getContent(), part.getContentType(), part.getFileName());
	    }
	    return;
	}

	    if (contentType != null && contentType.startsWith("text/"))
	    {
		body.append(o.toString());
		return;
	    }
	    if (fileName != null && !fileName.trim().isEmpty())
		attachments.add(MimeUtility.decodeText(fileName)); else
		    attachments.add(contentType);
    }
}
