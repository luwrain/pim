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
import java.util.*;
import java.sql.*;

import org.luwrain.core.*;
import org.luwrain.pim.*;
import org.luwrain.pim.mail.*;

final class Message extends MailMessage
{
    final long id;
    private final Connection con;
    private final ExecQueue queue;

    private final File messagesDir;
    private boolean committed = false;

    Message(ExecQueue queue, Connection con, long id, File messagesDir)
    {
	NullCheck.notNull(queue, "queue");
	NullCheck.notNull(con, "con");
	NullCheck.notNull(messagesDir, "messagesDir");
	if (id < 0)
	    throw new IllegalArgumentException("id (" + String.valueOf(id) + " may not be negative");
	this.queue = queue;
    	this.con = con;
	this.id = id;
	this.messagesDir = messagesDir;
    }

    @Override public void setState(MailMessage.State state) throws PimException
    {
	NullCheck.notNull(state, "state");
	if (committed)
	    try {
		queue.execInQueue(()->{
			final PreparedStatement st = con.prepareStatement(
									  "UPDATE mail_message SET state=? WHERE id=?;"
									  );
			st.setInt(1, MailMessage.stateToInt(state));
			st.setLong(2, id);
			st.executeUpdate();
			return null;
		    });
	    }
	    catch(Exception e)
	    {
		throw new PimException(e);
	    }
	super.setState(state);
    }

    @Override public void setSentDate(java.util.Date sentDate) throws PimException
    {
	if (committed)
	    try {
		final PreparedStatement st = con.prepareStatement("UPDATE email_message SET sent_date = ? WHERE id = ?;");
		st.setDate(1, new java.sql.Date(sentDate.getTime()));
		st.setLong(2, id);
		st.executeUpdate();
	    }
	    catch(SQLException e)
	    {
		throw new PimException(e);
	    }
	super.setSentDate(sentDate);
    }

    @Override public void setReceivedDate(java.util.Date receivedDate) throws PimException
    {
	if (committed)
	    try {
		final PreparedStatement st = con.prepareStatement("UPDATE email_message SET received_date = ? WHERE id = ?;");
		st.setDate(1, new java.sql.Date(receivedDate.getTime()));
		st.setLong(2, id);
		st.executeUpdate();
	    }
	    catch(SQLException e)
	    {
		throw new PimException(e);
	    }
	super.setReceivedDate(receivedDate);
    }

    @Override public void setText(String text) throws PimException
    {
	NullCheck.notNull(text, "text");
	if (committed)
	    try {
		final PreparedStatement st = con.prepareStatement("UPDATE email_message SET body = ? WHERE id = ?;");
		st.setString(1, text);
		st.setLong(2, id);
		st.executeUpdate();
	    }
	    catch(SQLException e)
	    {
		throw new PimException(e);
	    }
	super.setText(text);
    }

    @Override public void setContentType(String mimeContentType) throws PimException
    {
	if (committed)
	    try {
		final PreparedStatement st = con.prepareStatement("UPDATE email_message SET body = ? WHERE id = ?;");
		st.setString(1, mimeContentType);
		st.setLong(2, id);
		st.executeUpdate();
	    }
	    catch(SQLException e)
	    {
		throw new PimException(e);
	    }
	super.setContentType(mimeContentType);
    }

    @Override public byte[] getRawMessage() throws PimException
    {
	try {
	    final InputStream is = new FileInputStream(new File(messagesDir, getRawMessageFileName(id)) );
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

    @Override public void setRawMessage(byte[] rawMessage) throws PimException
    {
	NullCheck.notNull(rawMessage, "rawMessage");
	try {
	    saveRawMessage(rawMessage, messagesDir, id);
	}
	catch(IOException e)
	{
	    throw new PimException(e);
	}
    }

    void commit()
    {
	this.committed = true;
    }

    static void saveRawMessage(byte[] bytes, File messagesDir, long id) throws IOException
    {
	final File file = new File(messagesDir, getRawMessageFileName(id));
	final File parent = file.getParentFile();
	if (!parent.exists())
	    parent.mkdir(); else
	    if (!parent.isDirectory())
		throw new IOException(parent.getAbsolutePath() + " exists is not a directory");
	final OutputStream os = new FileOutputStream(file);
	try {
	    final ByteArrayInputStream is = new ByteArrayInputStream(bytes);
	    org.luwrain.util.StreamUtils.copyAllBytes(is, os);
	    os.flush();
	}
	finally {
	    os.close();
	}
    }

    static String getRawMessageFileName(long id)
    {
	String s = String.valueOf(id);
	while(s.length() < 4)
	    s = "0" + s;
	String d = String.valueOf(id/ 1000);
	while (d.length() < 3)
	    d = "0" + d;
	return new File(new File(d), "message" + s + ".eml").getPath();
    }
}
