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
    private byte[] rawMessage = new byte[0];
    private String extInfo = "";

    public void setMessageId(String messageId) throws PimException
    {
	NullCheck.notNull(messageId, "messageId");
	this.messageId = messageId;
    }

    public String getMessageId() throws PimException
    {
	return this.messageId;
    }

    public void setSubject(String subject) throws PimException
    {
	NullCheck.notNull(subject, "subject");
	this.subject = subject;
    }

    public String getSubject() throws PimException
    {
	return this.subject;
    }

    public void setFrom(String from) throws PimException
    {
	NullCheck.notNull(from, "from");
	this.from = from;
    }

    public String getFrom() throws PimException
    {
	return this.from;
    }

    public void setTo(String[] to) throws PimException
    {
	NullCheck.notNullItems(to, "to");
	this.to = to.clone();
    }

    public String[] getTo() throws PimException
    {
	return this.to.clone();
    }

    public void setCc(String[] cc) throws PimException
    {
	NullCheck.notNullItems(cc, "cc");
	this.cc = cc.clone();
    }

    public String[] getCc() throws PimException
    {
	return cc.clone();
    }

    public void setBcc(String[] bcc) throws PimException
    {
	NullCheck.notNullItems(bcc, "bcc");
	this.bcc = bcc.clone();
    }

    public String[] getBcc() throws PimException
    {
	return this.bcc.clone();
    }

    public void setState(State state) throws PimException
    {
	NullCheck.notNull(state, "state");
	this.state = state;
    }

    public State getState() throws PimException
    {
	return this.state;
    }

    public void setSentDate(Date date) throws PimException
    {
	NullCheck.notNull(sentDate, "sentDate");
	this.sentDate = sentDate;
    }

    public Date getSentDate() throws PimException
    {
	return this.sentDate;
    }

    public void setReceivedDate(Date date) throws PimException
    {
	NullCheck.notNull(receivedDate, "receivedDate");
	this.receivedDate = receivedDate;
    }

    public Date getReceivedDate() throws PimException
    {
	return this.receivedDate;
    }

    public void setText(String text) throws PimException
    {
	NullCheck.notNull(text, "text");
	this.text = text;
    }

    public String getText() throws PimException
    {
	return this.text;
    }

    public void setContentType(String contentType) throws PimException
    {
	this.contentType = contentType;
    }

    public String getContentType() throws PimException
    {
	return this.contentType;
    }

    public void setAttachments(String[] attachments) throws PimException
    {
	NullCheck.notNullItems(attachments, "attachments");
this.attachments = attachments.clone();
    }

    public String[] getAttachments() throws PimException
    {
	return this.attachments.clone();
    }

        public void setRawMessage(byte[] rawMessage) throws PimException
    {
NullCheck.notNull(rawMessage, "rawMessage");
this.rawMessage = rawMessage.clone();
    }

    public byte[] getRawMessage() throws PimException
    {
	return this.rawMessage.clone();
    }

    public void setExtInfo(String extInfo) throws PimException
    {
NullCheck.notNull(extInfo, "extInfo");
this.extInfo = extInfo;
    }

    public String getExtInfo() throws PimException
    {
	return this.extInfo;
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
