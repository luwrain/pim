/*
   Copyright 2012-2019 Michael Pozhidaev <michael.pozhidaev@gmail.com>
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

//LWR_API 1.0

package org.luwrain.pim.mail;

import java.util.*;

import org.luwrain.core.*;

public class MailMessage implements Comparable
{
    public enum State {NEW, READ, MARKED, DELETED};

    public String messageId = "";
	public String subject = "";
    public String from = "";
    public String[] to = new String[0];
    public String[] cc = new String[0];
    public String[] bcc = new String[0];
    public State state = State.NEW;
    public Date sentDate = new Date();
    public Date receivedDate = new Date();
    public String baseContent = "";
    public String mimeContentType = "";
    public String[] attachments = new String[0];
    public byte[] rawMail = new byte[0];
    public String extInfo = "";

	@Override public int compareTo(Object o)
    {
	/*
FIXME:
    	if (o == null || !(o instanceof Message))
	    return 0;
    	StoredMailMessageSql article = (StoredMailMessageSql)o;
    	if (state != article.state)
    	{
	    if (stateToInt(state) > stateToInt(article.state))
 return -1;
	    if (stateToInt(state) < stateToInt(article.state)) 
return 1;
    		return 0;
    	}
    	if (receivedDate == null || article.receivedDate == null) return 0;
    	// if receivedDate are equal, compare messages via sentDate
    	if(receivedDate==article.receivedDate&&sentDate!=null&&article.sentDate!=null) return -1 * sentDate.compareTo(article.sentDate);
    	return -1 * receivedDate.compareTo(article.receivedDate);
	*/
	return 0;
    }

    static public State intToState(int stateCode)
    {
	switch(stateCode)
	{
	case 1:
	    return State.NEW;
	case 2:
	    return State.READ;
	case 3:
	    return State.MARKED;
	case 4:
	    return State.DELETED;
	default:
	    return State.NEW;
	}
    }

    static public int stateToInt(State state)
    {
	NullCheck.notNull(state, "state");
	switch(state)
	{
	case NEW:
	    return 1;
	case READ:
	    return 2;
	case MARKED:
	    return 3;
	case DELETED:
	    return 4;
	default:
	    return 1;
	}
    }
}
