/*
   Copyright 2012-2020 Michael Pozhidaev <msp@luwrain.org>
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
import org.luwrain.pim.*;

public class MailMessage implements Comparable
{
    public enum State {NEW, READ, MARKED, DELETED};

    private String messageId = "";
    private String subject = "";
    private String from = "";
    private String[] to = new String[0];
    private String[] cc = new String[0];
    private String[] bcc = new String[0];
    private State state = State.NEW;
    private Date sentDate = new Date();
    private Date receivedDate = new Date();
    private String text = "";
    private String contentType = "";
    private String[] attachments = new String[0];
    private String extInfo = "";
        private transient byte[] rawMessage = new byte[0];

    public final void setMessageId(String messageId)
    {
	NullCheck.notNull(messageId, "messageId");
	this.messageId = messageId;
    }

    public final String getMessageId()
    {
	return this.messageId;
    }

    public final void setSubject(String subject)
    {
	NullCheck.notNull(subject, "subject");
	this.subject = subject;
    }

    public final String getSubject()
    {
	return this.subject;
    }

    public final void setFrom(String from)
    {
	NullCheck.notNull(from, "from");
	this.from = from;
    }

    public final String getFrom()
    {
	return this.from;
    }

    public final void setTo(String[] to)
    {
	NullCheck.notNullItems(to, "to");
	this.to = to.clone();
    }

    public final String[] getTo()
    {
	return this.to.clone();
    }

    public final void setCc(String[] cc)
    {
	NullCheck.notNullItems(cc, "cc");
	this.cc = cc.clone();
    }

    public final String[] getCc()
    {
	return cc.clone();
    }

    public final void setBcc(String[] bcc)
    {
	NullCheck.notNullItems(bcc, "bcc");
	this.bcc = bcc.clone();
    }

    public final String[] getBcc()
    {
	return this.bcc.clone();
    }

    public final void setState(State state)
    {
	NullCheck.notNull(state, "state");
	this.state = state;
    }

    public final State getState()
    {
	return this.state;
    }

    public final void setSentDate(Date date)
    {
	NullCheck.notNull(sentDate, "sentDate");
	this.sentDate = sentDate;
    }

    public final Date getSentDate()
    {
	return this.sentDate;
    }

    public final void setReceivedDate(Date date)
    {
	NullCheck.notNull(receivedDate, "receivedDate");
	this.receivedDate = receivedDate;
    }

    public final Date getReceivedDate()
    {
	return this.receivedDate;
    }

    public final void setText(String text)
    {
	NullCheck.notNull(text, "text");
	this.text = text;
    }

    public final String getText()
    {
	return this.text;
    }

    public final void setContentType(String contentType)
    {
	this.contentType = contentType;
    }

    public final String getContentType()
    {
	return this.contentType;
    }

    public final void setAttachments(String[] attachments)
    {
	NullCheck.notNullItems(attachments, "attachments");
	this.attachments = attachments.clone();
    }

    public final String[] getAttachments()
    {
	return this.attachments.clone();
    }

    public final void setExtInfo(String extInfo)
    {
	NullCheck.notNull(extInfo, "extInfo");
	this.extInfo = extInfo;
    }

    public final String getExtInfo()
    {
	return this.extInfo;
    }

        public final void setRawMessage(byte[] rawMessage)
    {
	NullCheck.notNull(rawMessage, "rawMessage");
	this.rawMessage = rawMessage.clone();
    }

    public final byte[] getRawMessage()
    {
	return this.rawMessage.clone();
    }

    public final void copyValues(MailMessage message)
    {
	NullCheck.notNull(message, "message");
	this.messageId = message.messageId;
	this.subject = message.subject;
	this.from = message.from;
	this.to = message.to.clone();
	this.cc = message.cc.clone();
	this.bcc = message.bcc.clone();
	this.state = message.state;
	this.sentDate = message.sentDate;
	this.receivedDate = message.receivedDate;
	this.text = message.text;
	this.contentType = message.contentType;
	this.attachments = message.attachments.clone();
	this.rawMessage = message.rawMessage.clone();
	this.extInfo = message.extInfo;
    }

    public void save() throws PimException
    {
    }

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
