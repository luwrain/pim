
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
		    attachments.add(fileName); else
		    attachments.add(contentType);
    }
}
