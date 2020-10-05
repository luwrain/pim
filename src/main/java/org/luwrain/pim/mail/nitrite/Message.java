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
import java.util.*;

import org.dizitart.no2.*;
import org.dizitart.no2.objects.*;
import org.dizitart.no2.objects.Cursor;
import static org.dizitart.no2.objects.filters.ObjectFilters.eq;
import static org.dizitart.no2.objects.filters.ObjectFilters.not;
import static org.dizitart.no2.objects.filters.ObjectFilters.and;

import org.luwrain.core.*;
import org.luwrain.pim.*;
import org.luwrain.pim.mail.*;

final class Message extends MailMessage
{
    private String id = "";
    private int folderId = 0;

    private transient File messagesDir;
    private transient ObjectRepository<Message> repo;

    public String getId()
    {
	return this.id;
    }

    public void setId(String id)
    {
	NullCheck.notEmpty(id, "id");
	this.id = id;
    }

    public int getFolderId()
    {
	return this.folderId;
    }

    public void setFolderId(int folderId)
    {
	this.folderId = folderId;
    }

    void setMessagesDir(File messagesDir)
    {
	NullCheck.notNull(messagesDir, "messagesDir");
	this.messagesDir = messagesDir;
    }

    void setRepo(ObjectRepository<Message> repo)
    {
	NullCheck.notNull(repo, "repo");
	this.repo = repo;
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

    static void saveRawMessage(byte[] bytes, File messagesDir, String id) throws IOException
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

    static String getRawMessageFileName(String id)
    {
	String s = id;
	/*
		String d = String.valueOf(id/ 1000);
	while (d.length() < 3)
	    d = "0" + d;
	*/
	return new File("message" + s + ".eml").getPath();
    }
}
