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
import java.util.function.*;

import org.dizitart.no2.*;
import org.dizitart.no2.objects.*;
import org.dizitart.no2.objects.Cursor;
import static org.dizitart.no2.objects.filters.ObjectFilters.eq;
import static org.dizitart.no2.objects.filters.ObjectFilters.not;
import static org.dizitart.no2.objects.filters.ObjectFilters.and;

import org.luwrain.core.*;
import org.luwrain.pim.*;
import org.luwrain.pim.mail.*;

import static org.luwrain.pim.mail.BinaryMessage.*;

final class Messages implements MailMessages
{
    private final Storing storing;
    private final File messagesDir;
    private final ObjectRepository<Message> repo;

    Messages(Storing storing,File messagesDir)
    {
	NullCheck.notNull(storing, "storing");
	NullCheck.notNull(messagesDir, "messagesDir");
	this.storing = storing;
	this.messagesDir = messagesDir;
	this.repo = storing.storage.get();
    }

    @Override public void save(MailFolder folder, MailMessage message)
    {
	NullCheck.notNull(folder, "folder");
	NullCheck.notNull(message, "message");
	if (message.getRawMessage() == null || message.getRawMessage().length == 0)
	    throw new IllegalArgumentException("Unable to save a message without raw message data");
	final Folder f = (Folder)folder;
	storing.execInQueue(()->{
		final Message m = new Message();
		//		m.setTransient(storing);
		m.copyValues(message);
		m.genId();
		m.folderId = f.getId();
		saveRawMessage(m);
		repo.insert(m);
		return null;
	    });
    }

    @Override public MailMessage[] load(MailFolder folder)
    {
	NullCheck.notNull(folder, "folder");
	final Folder f = (Folder)folder;
	return storing.execInQueue(()->{
		final List<Message> res = new ArrayList<>();
		final Cursor<Message> c = repo.find(eq("folderId", f.getId()));
		for(Message m: c)
		{
		    loadRawMessage(m);
		    res.add(m);
		}
		return res.toArray(new Message[res.size()]);
	    });
    }

    @Override public MailMessage[] load(MailFolder folder, Predicate<MailMessage> cond)
    {
	NullCheck.notNull(folder, "folder");
	NullCheck.notNull(cond, "cond");
	final Folder f = (Folder)folder;
	return storing.execInQueue(()->{
		final List<Message> res = new ArrayList<>();
		final Cursor<Message> c = repo.find(eq("folderId", f.getId()));
		for(Message m: c)
		{
		    if (!cond.test(m))
			continue;
		    loadRawMessage(m);
		    res.add(m);
		}
		return res.toArray(new Message[res.size()]);
	    });
    }

    @Override public void delete(MailMessage message)
    {
	NullCheck.notNull(message, "message");
	final Message m = (Message)message;
	if (m.id == null || m.id.isEmpty())
	    throw new IllegalArgumentException("Cannot delete a message without ID");
	storing.execInQueue(()->{
		repo.remove(eq("id", m.id));
		storing.getRawMessageFileName(m.id).delete();
		return null;
	    });
    }

    @Override public void update(MailMessage message)
    {
	NullCheck.notNull(message, "message");
	final Message m = (Message)message;
	if (m.id == null)
	    throw new IllegalStateException("The message ID can't be null");
	if (m.id.isEmpty())
	    throw new IllegalStateException("The message ID can't be empty");
	storing.execInQueue(()->{
		repo.update(eq("id", m.id), m);
		return null;
	    });
    }

    @Override public void moveToFolder(MailMessage message, MailFolder folder)
    {
	NullCheck.notNull(folder, "folder");
	NullCheck.notNull(message, "message");
	final Folder f = (Folder)folder;
	final Message m = (Message)message;
	if (f.id < 0)
	    throw new IllegalArgumentException("The folder ID can't be negative");
	m.folderId = f.id;
	update(m);
    }

    private void loadRawMessage(Message message)
    {
	try {
	    message.setRawMessage(storing.loadRawMessage(message.id));
	}
	catch(IOException e)
	{
	    throw new PimException(e);
	}
    }

    private void saveRawMessage(Message message)
    {
	final byte[] bytes = message.getRawMessage();
	if (bytes.length == 0)
	    return;
	try {
	    storing.saveRawMessage(bytes, message.id);
	}
	catch(IOException e)
	{
	    throw new PimException(e);
	}
    }
}
