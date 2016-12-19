
package org.luwrain.pim.mail;

import java.util.*;
import java.sql.*;
import org.luwrain.core.*;
import org.luwrain.pim.*;

class StoredMailMessageSql extends MailMessage implements StoredMailMessage
{
    final Connection con;
    long id;

    StoredMailMessageSql(Connection con)
    {
	NullCheck.notNull(con, "con");
    	this.con = con;
    }

    @Override public int getState()
    {
	return state;
    }

    @Override public void setState(int state) throws PimException
    {
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
	catch(SQLException e)
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
	    this.rawMail = rawMail;
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
