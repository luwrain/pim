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

package org.luwrain.pim.mail;

import java.util.*;
import java.sql.*;
import org.luwrain.core.NullCheck;
import org.luwrain.util.Strings;

class StoredMailMessageSql extends MailMessage implements StoredMailMessage, Comparable
{
    Connection con;
    long id;

    StoredMailMessageSql(Connection con)
    {
    	this.con = con;
	NullCheck.notNull(con, "con");
    }

    @Override public int getState()
    {
	return state;
    }

    @Override public void setState(int state) throws SQLException
    {
    }

    @Override public String getMessageId() 
{
    return messageId;
}

    @Override public void setMessageId(String messageId) throws SQLException
    {
    }

    @Override public String getSubject() 
    {
	return subject != null?subject:"";
    }

    @Override public void setSubject(String subject) throws SQLException
    {
    }

    @Override public String getFrom() 
{
    return from != null?from:"";
}

    @Override public void setFrom(String from) throws SQLException
	{
    }

    @Override public String[] getTo() 
    {
	return Strings.notNullArray(to);
}

    @Override public void setTo(String[] to) throws SQLException
	{
	}

	@Override public String[] getCc() 
    {
	return Strings.notNullArray(cc);
    }

	@Override public void setCc(String[] cc) throws SQLException
    {
	}

    @Override public String[] getBcc() 
    {
	return Strings.notNullArray(bcc);
    }

    @Override public void setBcc(String[] bcc) throws SQLException
    {
	}

    @Override public String[] getAttachments() 
    {
	return Strings.notNullArray(attachments);
    }

    @Override public void setAttachments(String[] value) throws SQLException
    {
	//FIXME:
	}


    @Override public java.util.Date getSentDate() 
    {
	return sentDate;
    }

    @Override public void setSentDate(java.util.Date sentDate) throws SQLException
	{
		PreparedStatement st = con.prepareStatement("UPDATE email_message SET sent_date = ? WHERE id = ?;");
		st.setDate(1, new java.sql.Date(sentDate.getTime()));
		st.setLong(2, id);
		st.executeUpdate();
		this.sentDate = sentDate;
    }

    @Override public java.util.Date getReceivedDate()
    {
	return receivedDate;
    }

    @Override public void setReceivedDate(java.util.Date receivedDate) throws SQLException
	{
		PreparedStatement st = con.prepareStatement("UPDATE email_message SET received_date = ? WHERE id = ?;");
		st.setDate(1, new java.sql.Date(receivedDate.getTime()));
		st.setLong(2, id);
		st.executeUpdate();
		this.receivedDate = receivedDate;
    }

    @Override public String getBaseContent() {return baseContent;}
    @Override public void setBaseContent(String baseContent) throws SQLException
    {
    	PreparedStatement st = con.prepareStatement("UPDATE email_message SET body = ? WHERE id = ?;");
    	st.setString(1, baseContent);
    	st.setLong(2, id);
    	st.executeUpdate();
    	this.baseContent = baseContent;
	}
    @Override public String getMimeContentType() {return mimeContentType;}
    @Override public void setMimeContentType(String mimeContentType) throws SQLException
    {
    	PreparedStatement st = con.prepareStatement("UPDATE email_message SET body = ? WHERE id = ?;");
    	st.setString(1, mimeContentType);
    	st.setLong(2, id);
    	st.executeUpdate();
    	this.mimeContentType = mimeContentType;
	}
    @Override public byte[] getRawMail() throws SQLException
    {
    	if(rawMail==null)
    	{
        	final PreparedStatement st = con.prepareStatement("SELECT raw_message FROM mail_message WHERE id = ?;");
        	st.setLong(1, this.id);
        	final ResultSet rs = st.executeQuery();
        	if(rs.next()) 
		    this.rawMail=rs.getBytes(1);
    	}
    	return rawMail;
    }

    @Override public void setRawMail(byte[] rawMail) throws SQLException
    {
    	PreparedStatement st = con.prepareStatement("UPDATE email_message SET raw = ? WHERE id = ?;");
    	st.setBytes(1, rawMail);
    	st.setLong(2, id);
    	st.executeUpdate();
    	this.rawMail = rawMail;
    }

    @Override public String getExtInfo() throws Exception
    {
	return extInfo != null?extInfo:"";
    }

    @Override public void setExtInfo(String value) throws Exception
    {
	//FIXME:
    }

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
