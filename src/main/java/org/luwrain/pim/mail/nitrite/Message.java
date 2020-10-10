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
import java.util.zip.*;

import org.dizitart.no2.*;
import org.dizitart.no2.objects.*;
import org.dizitart.no2.objects.Cursor;
import static org.dizitart.no2.objects.filters.ObjectFilters.eq;
import static org.dizitart.no2.objects.filters.ObjectFilters.not;
import static org.dizitart.no2.objects.filters.ObjectFilters.and;

import org.luwrain.core.*;
import org.luwrain.pim.*;
import org.luwrain.pim.mail.*;
import org.luwrain.util.*;

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

    public void genId()
    {
	final byte[] bytes = getRawMessage();
	if (bytes.length == 0)
	    throw new IllegalStateException("the raw message can't be empty on ID generation");
	this.id = new Sha1().getSha1(bytes);
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

    public byte[] loadRawMessage() throws PimException
    {
	try {
	    final InputStream is = new GZIPInputStream(new FileInputStream(new File(messagesDir, getRawMessageFileName(id)) ));
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

    public void saveRawMessage() throws PimException
    {
	final byte[] bytes = getRawMessage();
	if (bytes.length == 0)
	    return;
	try {
	    saveRawMessage(bytes, messagesDir, id);
	}
	catch(IOException e)
	{
	    throw new PimException(e);
	}
    }

    static void saveRawMessage(byte[] bytes, File messagesDir, String id) throws IOException
    {
	mkdirs(messagesDir, id);
	final File file = new File(messagesDir, getRawMessageFileName(id));
	try (final FileOutputStream fs = new FileOutputStream(file)) {
	    try (final GZIPOutputStream os = new GZIPOutputStream(fs)) {
		try (final ByteArrayInputStream is = new ByteArrayInputStream(bytes)){
		    org.luwrain.util.StreamUtils.copyAllBytes(is, os);
		}
		os.flush();
	    }
	    fs.flush();
	}
    }

    static private void mkdirs(File messagesDir, String id)
    {
	File f = messagesDir;
	for(int i = 1;i < 5;i++)
	{
	    f = new File(f, id.substring(0, i));
	    f.mkdir();
	}
    }

    static String getRawMessageFileName(String id)
    {
	NullCheck.notEmpty(id, "id");
	if (id.length() < 4)
	    throw new IllegalArgumentException("id (" + id + ") can't have the lenth less than 4");
	final StringBuilder b = new StringBuilder();
	b.append(id.substring(0, 1)).append("/")
	.append(id.substring(0, 2)).append("/")
	.append(id.substring(0, 3)).append("/")
	.append(id.substring(0, 4)).append("/")
	.append(id).append(".eml.gz");
	return new String(b);
    }
}
