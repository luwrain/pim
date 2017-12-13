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

package org.luwrain.pim.mail.mem;

import java.util.*;

import org.luwrain.core.*;
import org.luwrain.pim.*;
import org.luwrain.pim.mail.*;

class Message extends MailMessage implements StoredMailMessage
{
    int id;

    Message()
    {
    }

    @Override public MailMessage.State getState()
    {
	return state;
    }

    @Override public void setState(MailMessage.State state)
    {
	NullCheck.notNull(state, "state");
		this.state = state;
    }

    @Override public String getMessageId() 
{
    return messageId;
}

    @Override public void setMessageId(String messageId)
    {
	NullCheck.notNull(messageId, "messageId");
	this.messageId = messageId;
    }

    @Override public String getSubject() 
    {
	return subject != null?subject:"";
    }

    @Override public void setSubject(String subject)
    {
	NullCheck.notNull(subject, "subject");
	this.subject = subject;
    }

    @Override public String getFrom() 
{
    return from;
}

    @Override public void setFrom(String from)
	{
	    NullCheck.notNull(from, "from");
	    this.from = from;
    }

    @Override public String[] getTo() 
    {
	return to;
}

    @Override public void setTo(String[] to)
	{
	    NullCheck.notNullItems(to, "to");
	    this.to = to;
	}

	@Override public String[] getCc() 
    {
	return cc;
    }

	@Override public void setCc(String[] cc)
    {
	NullCheck.notNull(cc, "cc");
	this.cc = cc;
	}

    @Override public String[] getBcc() 
    {
	return bcc;
    }

    @Override public void setBcc(String[] bcc)
    {
	NullCheck.notNull(bcc, "bcc");
	this.bcc = bcc;
	}

    @Override public String[] getAttachments() 
    {
	return attachments;
    }

    @Override public void setAttachments(String[] value)
    {
	NullCheck.notNullItems(value, "value");
	this.attachments = value;
	}

    @Override public java.util.Date getSentDate() 
    {
	return sentDate;
    }

    @Override public void setSentDate(java.util.Date sentDate)
	{
	    NullCheck.notNull(sentDate, "sentDate");
		this.sentDate = sentDate;
	    }

    @Override public java.util.Date getReceivedDate()
    {
	return receivedDate;
    }

    @Override public void setReceivedDate(java.util.Date receivedDate)
	{
	    NullCheck.notNull(receivedDate, "receivedDate");
		this.receivedDate = receivedDate;
	    }

    @Override public String getText() 
{
return baseContent;
}

    @Override public void setText(String text)
    {
	NullCheck.notNull(text, "text");
	    this.baseContent = text;
	}

    @Override public String getMimeContentType() 
{
return mimeContentType;
}

    @Override public void setMimeContentType(String mimeContentType)
    {
	NullCheck.notNull(mimeContentType, "mimeContentType");
	    this.mimeContentType = mimeContentType;
	}

    @Override public byte[] getRawMessage()
    {
	    return rawMail;
	}

    @Override public void setRawMessage(byte[] rawMail)
    {
	NullCheck.notNull(rawMail, "rawMail");
	}

    @Override public String getExtInfo()
    {
	return extInfo != null?extInfo:"";
    }

    @Override public void setExtInfo(String value)
    {
	NullCheck.notNull(value, "value");
	this.extInfo = value;
    }
}
