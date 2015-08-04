/*
   Copyright 2012-2015 Michael Pozhidaev <msp@altlinux.org>

   This file is part of the Luwrain.

   Luwrain is free software; you can redistribute it and/or
   modify it under the terms of the GNU General Public
   License as published by the Free Software Foundation; either
   version 3 of the License, or (at your option) any later version.

   Luwrain is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
   General Public License for more details.
*/

package org.luwrain.pim.mail;

import java.sql.*;
import java.util.*;

import org.luwrain.core.Registry;

class MailStoringSql extends MailStoringRegistry
{
    private Connection con;

    public enum Condition {ALL,UNREAD};

    public MailStoringSql(Registry registry,Connection con)
    {
	super(registry);
	this.con = con;
	if (con == null)
	    throw new NullPointerException("con may not be null");
    }

    public static String SimpleArraySerialize(String[] list)
    { // FIXME: check list contains ';' char or change method to save simple lists of file names and email address
	StringBuilder b = new StringBuilder();
	for(String s: list)
	{
	    if (!b.toString().isEmpty())
		b.append(";");
	    b.append(s);
	}
	return b.toString();
	//    	return String.join(";", list);
    }

    public static String[] SimpleArrayDeSerialize(String str)
    {
    	return str.split(";");
    }

    @Override public void saveMessage(StoredMailFolder folder, MailMessage message) throws SQLException
    {
	if (folder == null)
	    throw new NullPointerException("folder may not be null");
	if (!(folder instanceof StoredMailFolderRegistry))
	    throw new NullPointerException("folder must be an instance of StoredMailFolderRegistry");
	final StoredMailFolderRegistry folderRegistry = (StoredMailFolderRegistry)folder;
    	PreparedStatement st = con.prepareStatement(
						    "INSERT INTO mail_message (mail_folder_id,state,subject,from_addr,message_id,sent_date,received_date,base_content,mime_content_type,raw_message) VALUES (?,?,?,?,?,?,?,?,?,?)",
						    Statement.RETURN_GENERATED_KEYS);
	System.out.println("1");
	st.setLong(1, folderRegistry.id);
	System.out.println("1");
	st.setInt(2, message.state);
	System.out.println("1");
	st.setString(3, message.subject);
	System.out.println("1");
	st.setString(4, message.from);
	System.out.println("1");
	st.setString(5, message.messageId);
	System.out.println("1");
	st.setDate(6, new java.sql.Date(message.sentDate.getTime()));
	System.out.println("1");
	st.setDate(7, new java.sql.Date(message.receivedDate.getTime()));
	System.out.println("1");
	st.setString(8, message.baseContent);
	System.out.println("1");
	st.setString(9, message.mimeContentType);
	System.out.println("1");
	st.setBytes(10, message.rawMail);
	System.out.println("1");
	final int updatedCount=st.executeUpdate();
	if(updatedCount==1)
	{ // get generated id
	    ResultSet generatedKeys = st.getGeneratedKeys();
	    long generatedKey = 0;
	    if (generatedKeys.next()) 
		generatedKey = generatedKeys.getLong(1);
	    System.out.println("generatedKey=" + generatedKey);
	}
    }

    @Override public StoredMailMessage[] loadMessages(StoredMailFolder folder) throws SQLException
    {
	if (folder == null)
	    throw new NullPointerException("folder may not be null");
	if (!(folder instanceof StoredMailFolderRegistry))
	    throw new IllegalArgumentException("folder must be an instance of StoredMailFolderRegistry");
	final StoredMailFolderRegistry folderRegistry = (StoredMailFolderRegistry)folder;
	PreparedStatement st = con.prepareStatement("SELECT id,message_id,state,subject,from_addr,sent_date,received_date,base_content,mime_content_type FROM mail_message WHERE mail_folder_id=?");
	st.setLong(1, folderRegistry.id);
    	ResultSet rs = st.executeQuery();
    	LinkedList<StoredMailMessage> res = new LinkedList<StoredMailMessage>();
    	while (rs.next())
    	{
	    final StoredMailMessageSql message=new StoredMailMessageSql(con);
	    message.id = rs.getLong(1);
	    message.messageId = rs.getString(2).trim();
	    message.state = rs.getInt(3);
	    message.subject = rs.getString(4);
	    message.from = rs.getString(5);
	    message.sentDate = new java.util.Date(rs.getDate(6).getTime());
	    message.receivedDate = new java.util.Date(rs.getDate(7).getTime());
	    message.baseContent = rs.getString(8);
	    message.mimeContentType = rs.getString(9).trim();
	    res.add(message);
	}
    	return res.toArray(new StoredMailMessage[res.size()]);
    }
}
