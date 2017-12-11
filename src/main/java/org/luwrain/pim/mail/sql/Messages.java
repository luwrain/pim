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

package org.luwrain.pim.mail.sql;

import java.sql.*;
import java.util.*;

import org.luwrain.core.*;
import org.luwrain.pim.*;
import org.luwrain.pim.mail.*;

class Messages implements MailMessages
{
    static private final int FIELD_TYPE_TO = 1;
    static private final int FIELD_TYPE_CC = 2;
    static private final int FIELD_TYPE_BCC = 3;
    static private final int FIELD_TYPE_ATTACHMENT = 4;

    private final ExecQueue queue;
    private final Connection con;

    Messages(ExecQueue queue,Connection con)
    {
	NullCheck.notNull(queue, "queue");
	NullCheck.notNull(con, "con");
	this.queue = queue;
	this.con = con;
    }

    @Override public void save(StoredMailFolder folder, MailMessage message) throws PimException
    {
	NullCheck.notNull(folder, "folder");
	NullCheck.notNull(message, "message");
	final Folder folderRegistry = (Folder)folder;
	try {
	    queue.execInQueue(()->{
		    PreparedStatement st = con.prepareStatement(
								"INSERT INTO mail_message (mail_folder_id,state,subject,from_addr,message_id,sent_date,received_date,base_content,mime_content_type,raw_message,ext_info) VALUES (?,?,?,?,?,?,?,?,?,?,?)",
								Statement.RETURN_GENERATED_KEYS);
		    st.setLong(1, folderRegistry.id);
		    st.setInt(2, MailMessage.stateToInt(message.state));
		    st.setString(3, message.subject);
		    st.setString(4, message.from);
		    st.setString(5, message.messageId);
		    st.setDate(6, new java.sql.Date(message.sentDate.getTime()));
		    st.setDate(7, new java.sql.Date(message.receivedDate.getTime()));
		    st.setString(8, message.baseContent);
		    st.setString(9, message.mimeContentType);
		    st.setBytes(10, message.rawMail);
		    st.setString(11, message.extInfo);
		    final int updatedCount=st.executeUpdate();
		    if(updatedCount != 1)
			throw new PimException("Unable to execute initial INSERT query");
		    final ResultSet generatedKeys = st.getGeneratedKeys();
		    if (!generatedKeys.next()) 
			return null;
		    final long generatedKey = generatedKeys.getLong(1);
		    //to
		    if (message.to != null)
			for(String v: message.to)
			{
			    st = con.prepareStatement(
						      "INSERT INTO mail_message_field (mail_message_id,field_type,value) VALUES (?,?,?)"
						      );
			    st.setLong(1, generatedKey);
			    st.setInt(2, FIELD_TYPE_TO);
			    st.setString(3, v);
			    st.executeUpdate();
			}
		    //cc
		    if (message.cc != null)
			for(String v: message.cc)
			{
			    st = con.prepareStatement(
						      "INSERT INTO mail_message_field (mail_message_id,field_type,value) VALUES (?,?,?)"
						      );
			    st.setLong(1, generatedKey);
			    st.setInt(2, FIELD_TYPE_CC);
			    st.setString(3, v);
			    st.executeUpdate();
			}
		    //bcc
		    if (message.bcc != null)
			for(String v: message.bcc)
			{
			    st = con.prepareStatement(
						      "INSERT INTO mail_message_field (mail_message_id,field_type,value) VALUES (?,?,?)"
						      );
			    st.setLong(1, generatedKey);
			    st.setInt(2, FIELD_TYPE_BCC);
			    st.setString(3, v);
			    st.executeUpdate();
			}
		    //attachment
		    if (message.attachments != null)
			for(String v: message.attachments)
			{
			    st = con.prepareStatement(
						      "INSERT INTO mail_message_field (mail_message_id,field_type,value) VALUES (?,?,?)"
						      );
			    st.setLong(1, generatedKey);
			    st.setInt(2, FIELD_TYPE_ATTACHMENT);
			    st.setString(3, v);
			    st.executeUpdate();
			}
		    return null;
		});
	}
	catch(Exception e)
	{
	    throw new PimException(e);
	}
    }

    @Override public StoredMailMessage[] load(StoredMailFolder folder) throws PimException
    {
	NullCheck.notNull(folder, "folder");
	final Folder folderRegistry = (Folder)folder;
	try {
	    return (StoredMailMessage[])queue.execInQueue(()->{
		    final Map<Long, StringValue> stringValues = new HashMap();
		    PreparedStatement st = con.prepareStatement(
								"SELECT id,message_id,state,subject,from_addr,sent_date,received_date,base_content,mime_content_type,ext_info FROM mail_message WHERE mail_folder_id=?"
								);
		    st.setLong(1, folderRegistry.id);
		    ResultSet rs = st.executeQuery();
		    final List<StoredMailMessage> res = new LinkedList();
		    while (rs.next())
		    {
			final Message message=new Message(con);
			message.id = rs.getLong(1);
			message.messageId = rs.getString(2).trim();
			message.state = MailMessage.intToState(rs.getInt(3));
			message.subject = rs.getString(4);
			message.from = rs.getString(5);
			message.sentDate = new java.util.Date(rs.getTimestamp(6).getTime());
			message.receivedDate = new java.util.Date(rs.getTimestamp(7).getTime());
			message.baseContent = rs.getString(8);
			message.mimeContentType = rs.getString(9).trim();
			message.rawMail = null;
			message.extInfo = rs.getString(10).trim();
			stringValues.put(new Long(message.id), new StringValue(message.id));
			res.add(message);
		    }
		    final Statement vst = con.createStatement();
		    rs = vst.executeQuery(
					  "SELECT mail_message_id,field_type,value FROM mail_message_field"
					  );
		    while (rs.next())
		    {
			final long id = rs.getLong(1);
			final int fieldType = rs.getInt(2);
			final String value = rs.getString(3);
			if (!stringValues.containsKey(new Long(id)))
			    continue;
			final StringValue s = stringValues.get(new Long(id));
			switch(fieldType)
			{
			case FIELD_TYPE_TO:
			    s.to.add(value);
			    break;
			case FIELD_TYPE_CC:
			    s.cc.add(value);
			    break;
			case FIELD_TYPE_BCC:
			    s.bcc.add(value);
			    break;
			case FIELD_TYPE_ATTACHMENT:
			    s.attachments.add(value);
			    break;
			}
		    }
		    for(StoredMailMessage message: res)
		    {
			final Message m = (Message)message;
			if (!stringValues.containsKey(new Long(m.id)))//Should never happen;
			    continue;
			final StringValue s = stringValues.get(new Long(m.id));
			m.to = s.to.toArray(new String[s.to.size()]);
			m.cc = s.cc.toArray(new String[s.cc.size()]);
			m.bcc = s.bcc.toArray(new String[s.bcc.size()]);
			m.attachments = s.attachments.toArray(new String[s.attachments.size()]);
		    }
		    return res.toArray(new StoredMailMessage[res.size()]);
		});
	}
	catch(Exception e)
	{
	    throw new PimException(e);
	}
    }

    @Override public void delete(StoredMailMessage message) throws PimException
    {
	NullCheck.notNull(message, "message");
	final Message messageSql = (Message)message;
	try {
	    queue.execInQueue(()->{
		    PreparedStatement st = con.prepareStatement(
								"DELETE FROM mail_message_field WHERE mail_message_id=?"
								);
		    st.setLong(1, messageSql.id);
		    st.executeUpdate();
		    st = con.prepareStatement(
					      "DELETE FROM mail_message WHERE id=?"
					      );
		    st.setLong(1, messageSql.id);
		    st.executeUpdate();
		    return null;
		});
	}
	catch(Exception e)
	{
	    throw new PimException(e);
	}
    }

    @Override public void moveToFolder(StoredMailMessage message, StoredMailFolder folder) throws PimException
    {
	NullCheck.notNull(folder, "folder");
	NullCheck.notNull(message, "message");
	final Folder folderRegistry = (Folder)folder;
	final Message messageSql = (Message)message;
	try {
	    queue.execInQueue(()->{
		    final PreparedStatement st = con.prepareStatement(
								      "UPDATE mail_message SET mail_folder_id=? WHERE id=?"
								      );
		    st.setLong(1, folderRegistry.id);
		    st.setLong(2, messageSql.id);
		    st.executeUpdate();
		    return null;
		});
	}
	catch(Exception e)
	{
	    throw new PimException(e);
	}
    }

    static private class StringValue
    {
	final long messageId;
	final List<String> to = new LinkedList();
	final List<String> cc = new LinkedList();
	final List<String> bcc = new LinkedList();
	final List<String> attachments = new LinkedList();

	StringValue(long messageId)
	{
	    this.messageId = messageId;
	}
    }
}
