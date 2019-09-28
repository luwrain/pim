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

package org.luwrain.pim.mail.sql;

import java.io.*;
import java.util.*;
import java.sql.*;

import org.luwrain.core.*;
import org.luwrain.pim.*;
import org.luwrain.pim.mail.*;

final class Message extends MailMessage implements StoredMailMessage
{
    final Connection con;
    final long id;
    private final File messagesDir;

    Message(Connection con, long id, File messagesDir)
    {
	NullCheck.notNull(con, "con");
	NullCheck.notNull(messagesDir, "messagesDir");
	if (id < 0)
	    throw new IllegalArgumentException("id (" + String.valueOf(id) + " may not be negative");
    	this.con = con;
	this.id = id;
	this.messagesDir = messagesDir;
    }

    @Override public MailMessage.State getState()
    {
	return state;
    }

    @Override public void setState(MailMessage.State state) throws PimException
    {
	NullCheck.notNull(state, "state");
	    try {
		final PreparedStatement st = con.prepareStatement("UPDATE mail_message SET state=? WHERE id=?;");
		st.setInt(1, MailMessage.stateToInt(state));
		st.setLong(2, id);
		st.executeUpdate();
		this.state = state;
	    }
	    catch(SQLException e)
	    {
		throw new PimException(e);
	    }
    }

    @Override public String getMessageId() 
{
    return messageId;
}

    @Override public void setMessageId(String messageId) throws PimException
    {
    }

    @Override public String getSubject() 
    {
	return subject != null?subject:"";
    }

    @Override public void setSubject(String subject) throws PimException
    {
    }

    @Override public String getFrom() 
{
    return from != null?from:"";
}

    @Override public void setFrom(String from) throws PimException
	{
    }

    @Override public String[] getTo() 
    {
	return to;
}

    @Override public void setTo(String[] to) throws PimException
	{
	}

	@Override public String[] getCc() 
    {
	return cc;
    }

	@Override public void setCc(String[] cc) throws PimException
    {
	}

    @Override public String[] getBcc() 
    {
	return bcc;
    }

    @Override public void setBcc(String[] bcc) throws PimException
    {
	}

    @Override public String[] getAttachments() 
    {
	return attachments;
    }

    @Override public void setAttachments(String[] value) throws PimException
    {
	//FIXME:
	}

    @Override public java.util.Date getSentDate() 
    {
	return sentDate;
    }

    @Override public void setSentDate(java.util.Date sentDate) throws PimException
	{
	    try {
		PreparedStatement st = con.prepareStatement("UPDATE email_message SET sent_date = ? WHERE id = ?;");
		st.setDate(1, new java.sql.Date(sentDate.getTime()));
		st.setLong(2, id);
		st.executeUpdate();
		this.sentDate = sentDate;
	    }
	    catch(SQLException e)
	    {
		throw new PimException(e);
	    }
	    }

    @Override public java.util.Date getReceivedDate()
    {
	return receivedDate;
    }

    @Override public void setReceivedDate(java.util.Date receivedDate) throws PimException
	{
	    try {
		PreparedStatement st = con.prepareStatement("UPDATE email_message SET received_date = ? WHERE id = ?;");
		st.setDate(1, new java.sql.Date(receivedDate.getTime()));
		st.setLong(2, id);
		st.executeUpdate();
		this.receivedDate = receivedDate;
	    }
	    catch(SQLException e)
	    {
		throw new PimException(e);
	    }
	    }

    @Override public String getText() 
{
return baseContent;
}

    @Override public void setText(String text) throws PimException
    {
	NullCheck.notNull(text, "text");
	try {
	    final PreparedStatement st = con.prepareStatement("UPDATE email_message SET body = ? WHERE id = ?;");
	    st.setString(1, text);
	    st.setLong(2, id);
	    st.executeUpdate();
	    this.baseContent = text;
	}
	catch(SQLException e)
	{
	    throw new PimException(e);
	}
	}

    @Override public String getMimeContentType() 
{
return mimeContentType;
}

    @Override public void setMimeContentType(String mimeContentType) throws PimException
    {
	try {
	    PreparedStatement st = con.prepareStatement("UPDATE email_message SET body = ? WHERE id = ?;");
	    st.setString(1, mimeContentType);
	    st.setLong(2, id);
	    st.executeUpdate();
	    this.mimeContentType = mimeContentType;
	}
	catch(SQLException e)
	{
	    throw new PimException(e);
	}
	}

    @Override public byte[] getRawMessage() throws PimException
    {
	try {
	    final InputStream is = new FileInputStream(new File("") );
	    try {
		final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		org.luwrain.util.StreamUtils.copyAllBytes(is, bytes);
		return bytes.toByteArray();
	    }
	    finally {
		is.close();
	    }
	}
	catch(IOException e)
	{
	    throw new PimException(e);
	}
    }

    @Override public void setRawMessage(byte[] rawMail) throws PimException
    {
	try {
	    PreparedStatement st = con.prepareStatement("UPDATE email_message SET raw = ? WHERE id = ?;");
	    st.setBytes(1, rawMail);
	    st.setLong(2, id);
	    st.executeUpdate();
	    super.setRawMessage(rawMail);
	}
	catch(SQLException e)
	{
	    throw new PimException(e);
	}
	}

    @Override public String getExtInfo() throws PimException
    {
	return extInfo != null?extInfo:"";
    }

    @Override public void setExtInfo(String value) throws PimException
    {
	//FIXME:
    }
}
