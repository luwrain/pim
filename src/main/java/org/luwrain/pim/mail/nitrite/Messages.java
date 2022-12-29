/*
   Copyright 2012-2022 Michael Pozhidaev <msp@luwrain.org>
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
	final Folder f = (Folder)folder;
	storing.execInQueue(()->{
		final Message m = new Message();
		m.messagesDir = this.messagesDir;
		m.initStoring(storing, repo);
		m.copyValues(message);
		m.genId();
		m.folderId = f.getId();
		m.saveRawMessage();
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
		    		    		    m.messagesDir = this.messagesDir;
				    m.initStoring(storing, repo);
		    m.loadRawMessage();
		    res.add(m);
		}
		return res.toArray(new Message[res.size()]);
	    });
    }

        @Override public MailMessage[] loadNoDeleted(MailFolder folder) throws PimException
    {
	NullCheck.notNull(folder, "folder");
	return load(folder);
								  }

    @Override public void delete(MailMessage message) throws PimException
    {
	NullCheck.notNull(message, "message");
	final Message m = (Message)message;
	    storing.execInQueue(()->{
		    return null;
		});
    }

    @Override public void moveToFolder(MailMessage message, MailFolder folder)
    {
	NullCheck.notNull(folder, "folder");
	NullCheck.notNull(message, "message");
	final Folder f = (Folder)folder;
	final Message m = (Message)message;
	m.folderId = f.getId();
	m.save();
    }

    @Override public byte[] toByteArray(MailMessage message, Map<String, String> extraHeaders)
    {
	NullCheck.notNull(message, "message");
		try {

	    return toByteArray(message, extraHeaders);
	}
	catch(Exception e)
	{
	    throw new PimException(e);
	}
    }

    @Override public     MailMessage fromByteArray(byte[] bytes)
    {
	throw new RuntimeException("not implemented");
    }
}
