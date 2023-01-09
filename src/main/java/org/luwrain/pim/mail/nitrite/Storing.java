/*
   Copyright 2012-2023 Michael Pozhidaev <msp@luwrain.org>
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
import java.util.concurrent.*;
import java.util.zip.*;

import org.dizitart.no2.*;

import org.luwrain.core.*;
import org.luwrain.pim.*;
import org.luwrain.pim.storage.*;
import org.luwrain.pim.mail.*;

public final class Storing implements MailStoring
{
    static final String
	LOG_COMPONENT = "pim";

    static private final String
	MESSAGE_FILE_EXTENSION = ".eml.gz";

    final Luwrain luwrain;
    final Registry registry;
    final NitriteStorage<Message> storage;
        final Object syncObj;
    private final ExecQueues execQueues;
    private final boolean highPriority;
    private final File messagesDir;

    private final Accounts accounts;
    private final Folders folders;
    private final Messages messages;

    public Storing(
		   Luwrain luwrain,
		   NitriteStorage<Message> storage,
		   ExecQueues execQueues,
		   Object syncObj,
		   boolean highPriority,
		   File messagesDir)
    {
	NullCheck.notNull(luwrain, "luwrain");
	NullCheck.notNull(storage, "storage");
	NullCheck.notNull(execQueues, "execQueues");
	NullCheck.notNull(messagesDir, "messagesDir");
	this.luwrain = luwrain;
	this.registry = luwrain.getRegistry();
	this.storage = storage;
	this.execQueues = execQueues;
	this.syncObj = syncObj;
	this.highPriority = highPriority;
	this.messagesDir = messagesDir;
		this.messages = new Messages(this, messagesDir);
		this.folders = new Folders(this, messages);
	this.accounts = new Accounts(this);
	    }

    @Override public MailRules getRules() { return null; }
    @Override public MailFolders getFolders() { return folders; }
    @Override public MailAccounts getAccounts() { return accounts; }
    @Override public MailMessages getMessages() { return messages; }

        @Override public     String combinePersonalAndAddr(String personal, String addr)
    {
	NullCheck.notNull(personal, "personal");
	NullCheck.notNull(addr, "addr");
	return AddressUtils.combinePersonalAndAddr(personal, addr);
    }

    <T> T execInQueue(Callable<T> callable)
    {
	NullCheck.notNull(callable, "callable");
	try {
	    return execQueues.exec(new FutureTask<T>(callable), highPriority);
	}
	catch(Throwable e)
	{
	    throw new PimException(e);
	}
    }

    void saveRawMessage(byte[] bytes, String id) throws IOException
    {
	final File messageFile = getRawMessageFileName(id);
	java.nio.file.Files.createDirectories(messageFile.getParentFile().toPath());
	try (final FileOutputStream fs = new FileOutputStream(messageFile)) {
	    try (final GZIPOutputStream os = new GZIPOutputStream(fs)) {
		try (final ByteArrayInputStream is = new ByteArrayInputStream(bytes)){
		    org.luwrain.util.StreamUtils.copyAllBytes(is, os);
		}
		os.flush();
	    }
	    fs.flush();
	}
    }

        byte[] loadRawMessage(String id) throws IOException
    {
	    try (final InputStream is = new GZIPInputStream(new FileInputStream(getRawMessageFileName(id) ))){
		final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		org.luwrain.util.StreamUtils.copyAllBytes(is, bytes);
return bytes.toByteArray();
	    }
    }



File getRawMessageFileName(String id)
    {
	if (id.length() < 4)
	    throw new IllegalArgumentException("id (" + id + ") can't be shorter than 4");
	File f = new File(messagesDir, id.substring(0, 1));
	f = new File(f, id.substring(0, 2));
	f = new File(f, id.substring(0, 3));
	f = new File(f, id.substring(0, 4));
return new File(f, id + MESSAGE_FILE_EXTENSION);
    }
}
