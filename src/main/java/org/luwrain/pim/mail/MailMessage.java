/*
   Copyright 2012-2016 Michael Pozhidaev <michael.pozhidaev@gmail.com>
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

package org.luwrain.pim.mail;

import java.util.*;

public class MailMessage implements Comparable
{
    public String messageId = "";
	public String subject = "";
    public String from = "";
    public String[] to = new String[0];
    public String[] cc = new String[0];
    public String[] bcc = new String[0];
    public int state = 0;
    public Date sentDate = new Date();
    public Date receivedDate = new Date();
    public String baseContent = "";
    public String mimeContentType = "";
    public String[] attachments = new String[0];
    public byte[] rawMail = new byte[0];
    public String extInfo = "";

	@Override public int compareTo(Object o)
    {
    	if (o == null || !(o instanceof StoredMailMessageSql)) return 0;
    	StoredMailMessageSql article = (StoredMailMessageSql)o;
    	if (state != article.state)
    	{
    		if (state > article.state) return -1;
    		if (state < article.state) return 1;
    		return 0;
    	}
    	if (receivedDate == null || article.receivedDate == null) return 0;
    	// if receivedDate are equal, compare messages via sentDate
    	if(receivedDate==article.receivedDate&&sentDate!=null&&article.sentDate!=null) return -1 * sentDate.compareTo(article.sentDate);
    	return -1 * receivedDate.compareTo(article.receivedDate);
    }

}
