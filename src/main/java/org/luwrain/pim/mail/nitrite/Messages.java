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

package org.luwrain.pim.mail.nitrite;

import java.io.*;
import java.sql.*;
import java.util.*;

import org.luwrain.core.*;
import org.luwrain.pim.*;
import org.luwrain.pim.mail.*;

final class Messages implements MailMessages
{
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
	//	final Folder folderReg = (Folder)folder;
	try {
	    queue.execInQueue(()->{
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
	return null;
								  }

        @Override public MailMessage[] loadNoDeleted(MailFolder folder) throws PimException
    {
	NullCheck.notNull(folder, "folder");
	return null;
								  }

    @Override public void delete(MailMessage message) throws PimException
    {
	NullCheck.notNull(message, "message");
	final Message messageSql = (Message)message;
	try {
	    queue.execInQueue(()->{
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
	//	final Folder folderRegistry = (Folder)folder;
	final Message messageSql = (Message)message;
	try {
	    queue.execInQueue(()->{
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
}
