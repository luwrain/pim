/*
   Copyright 2012-2019 Michael Pozhidaev <msp@luwrain.org>
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
import java.sql.*;
import java.util.*;

import org.luwrain.core.*;
import org.luwrain.pim.*;
import org.luwrain.pim.mail.*;

final class Messages implements MailMessages
{
    static private final int FIELD_TYPE_TO = 1;
    static private final int FIELD_TYPE_CC = 2;
    static private final int FIELD_TYPE_BCC = 3;
    static private final int FIELD_TYPE_ATTACHMENT = 4;

    private final ExecQueue queue;
    private final Connection con;
    private final File messagesDir;

    Messages(ExecQueue queue,Connection con, File messagesDir)
    {
	NullCheck.notNull(queue, "queue");
	NullCheck.notNull(con, "con");
	NullCheck.notNull(messagesDir, "messagesDir");
	this.queue = queue;
	this.con = con;
	this.messagesDir = messagesDir;
    }

    @Override public void save(MailFolder folder, MailMessage message) throws PimException
    {
	NullCheck.notNull(folder, "folder");
	NullCheck.notNull(message, "message");
	final Folder folderReg = (Folder)folder;
	try {
	    queue.execInQueue(()->{
		    PreparedStatement st = con.prepareStatement(
								"INSERT INTO mail_message (mail_folder_id,state,subject,from_addr,message_id,sent_date,received_date,base_content,mime_content_type,ext_info) VALUES (?,?,?,?,?,?,?,?,?,?)",
								Statement.RETURN_GENERATED_KEYS);
		    st.setLong(1, folderReg.id);
		    st.setInt(2, MailMessage.stateToInt(message.getState()));
		    st.setString(3, message.getSubject());
		    st.setString(4, message.getFrom());
		    st.setString(5, message.getMessageId());
		    st.setDate(6, new java.sql.Date(message.getSentDate().getTime()));
		    st.setDate(7, new java.sql.Date(message.getReceivedDate().getTime()));
		    st.setString(8, message.getText());
		    st.setString(9, message.getContentType());
		    st.setString(10, message.getExtInfo());
		    final int updatedCount=st.executeUpdate();
		    if(updatedCount != 1)
			throw new PimException("Unable to execute the initial INSERT query");
		    final ResultSet generatedKeys = st.getGeneratedKeys();
		    if (!generatedKeys.next())
			throw new PimException("The used back-end does not support the retrieving of the automatically generated keys");
		    final long generatedKey = generatedKeys.getLong(1);
		    Message.saveRawMessage(message.getRawMessage(), messagesDir, generatedKey);
		    //to
		    for(String v: message.getTo())
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
		    for(String v: message.getCc())
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
		    for(String v: message.getBcc())
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
		    for(String v: message.getAttachments())
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

    @Override public MailMessage[] load(MailFolder folder) throws PimException
    {
	NullCheck.notNull(folder, "folder");
	return loadWithFilter(folder, "");
								  }

        @Override public MailMessage[] loadNoDeleted(MailFolder folder) throws PimException
    {
	NullCheck.notNull(folder, "folder");
	return loadWithFilter(folder, "mail_message.state <> " + MailMessage.stateToInt(MailMessage.State.DELETED));
								  }

    private MailMessage[] loadWithFilter(MailFolder folder, String filter) throws PimException
    {
	NullCheck.notNull(folder, "folder");
	NullCheck.notNull(filter, "filter");
	final Folder folderReg = (Folder)folder;
	final String filterExp = !filter.isEmpty()?" AND " + filter:"";
	try {
	    return (MailMessage[])queue.execInQueue(()->{
		    final Map<Long, StringValue> stringValues = new HashMap();
		    final PreparedStatement st = con.prepareStatement(
								"SELECT id,message_id,state,subject,from_addr,sent_date,received_date,base_content,mime_content_type,ext_info FROM mail_message WHERE mail_message.mail_folder_id=?" + filterExp
								);
		    st.setLong(1, folderReg.id);
		    ResultSet rs = st.executeQuery();
		    final List<MailMessage> res = new LinkedList();
		    while (rs.next())
		    {
			final Message message=new Message(queue, con, rs.getLong(1), messagesDir);
			message.setMessageId(rs.getString(2).trim());
			message.setState(MailMessage.intToState(rs.getInt(3)));
			message.setSubject(rs.getString(4));
			message.setFrom(rs.getString(5));
			message.setSentDate(new java.util.Date(rs.getTimestamp(6).getTime()));
			message.setReceivedDate(new java.util.Date(rs.getTimestamp(7).getTime()));
			message.setText(rs.getString(8));
			message.setContentType(rs.getString(9).trim());
			message.setExtInfo(rs.getString(10).trim());
			stringValues.put(new Long(message.id), new StringValue(message.id));
			res.add(message);
		    }
		    final PreparedStatement vst = con.prepareStatement(
								       "SELECT mail_message_field.mail_message_id,mail_message_field.field_type,mail_message_field.value FROM mail_message_field,mail_message WHERE mail_message_field.mail_message_id = mail_message.id" + filterExp
								       );
		    		    st.setLong(1, folderReg.id);
		    rs = vst.executeQuery();
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
		    for(MailMessage message: res)
		    {
			final Message m = (Message)message;
			if (!stringValues.containsKey(new Long(m.id)))//Should never happen;
			    continue;
			final StringValue s = stringValues.get(new Long(m.id));
			m.setTo(s.to.toArray(new String[s.to.size()]));
			m.setCc(s.cc.toArray(new String[s.cc.size()]));
			m.setBcc(s.bcc.toArray(new String[s.bcc.size()]));
			m.setAttachments(s.attachments.toArray(new String[s.attachments.size()]));
			m.commit();
		    }
		    return res.toArray(new MailMessage[res.size()]);
		});
	}
	catch(Exception e)
	{
	    throw new PimException(e);
	}
    }

    @Override public void delete(MailMessage message) throws PimException
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
		    //FIXME:delete the message file
		    return null;
		});
	}
	catch(Exception e)
	{
	    throw new PimException(e);
	}
    }

    @Override public void moveToFolder(MailMessage message, MailFolder folder) throws PimException
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

    @Override public byte[] toByteArray(MailMessage message, Map<String, String> extraHeaders) throws PimException
    {
	NullCheck.notNull(message, "message");
	try {
	    return org.luwrain.pim.mail.BinaryMessage.toByteArray(message, extraHeaders);
	}
	catch(Exception e)
	{
	    throw new PimException(e);
	}
    }

    @Override public     MailMessage fromByteArray(byte[] bytes) throws PimException
    {
	throw new RuntimeException("not implemented");
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
