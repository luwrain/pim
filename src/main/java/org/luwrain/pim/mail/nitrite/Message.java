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

public final class Message extends MailMessage
{
    String id = "";
    int folderId = 0;

transient Storing storing = null;
transient ObjectRepository<Message> repo = null;

    void genId()
    {
	final byte[] bytes = getRawMessage();
	if (bytes.length == 0)
	    throw new IllegalStateException("the raw message can't be empty on ID generation");
	this.id = new Sha1().getSha1(bytes);
    }

    void initStoring(Storing storing, ObjectRepository<Message> repo)
    {
	NullCheck.notNull(storing, "storing");
		NullCheck.notNull(repo, "repo");
			this.storing = storing;
	this.repo = repo;
    }

    @Override public void save() throws PimException
    {
	if (storing == null)
	    throw new IllegalStateException("storing can't be null");
	if (repo == null)
	    throw new IllegalStateException("repo can't be null");
	if (id == null)
	    throw new IllegalStateException("id can't be null");
	if (id.isEmpty())
	    throw new IllegalStateException("id can't be empty");
	try {
	    storing.execInQueue(()->{
		    repo.update(eq("id", id), this);
		    return null;
		});
	}
	catch(Exception e)
	{
	    throw new PimException(e);
	}
    }

    void loadRawMessage()
    {
	try {
	    try (final InputStream is = new GZIPInputStream(new FileInputStream(storing.getRawMessageFileName(id) ))){
		final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		org.luwrain.util.StreamUtils.copyAllBytes(is, bytes);
		setRawMessage(bytes.toByteArray());
	    }
	}
	catch(IOException e)
	{
	    throw new PimException(e);
	}
    }

    void saveRawMessage()
    {
	final byte[] bytes = getRawMessage();
	if (bytes.length == 0)
	    return;
	try {
	    storing.saveRawMessage(bytes, id);
	}
	catch(IOException e)
	{
	    throw new PimException(e);
	}
    }

}
