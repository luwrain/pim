/*
   Copyright 2012-2019 Michael Pozhidaev <michael.pozhidaev@gmail.com>
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

package org.luwrain.pim.mail.mem;

import java.util.*;

import org.luwrain.core.*;
import org.luwrain.pim.*;
import org.luwrain.pim.mail.*;

class Messages implements MailMessages
{
    @Override public void save(StoredMailFolder folder, MailMessage message)
    {
	NullCheck.notNull(folder, "folder");
	NullCheck.notNull(message, "message");
    }

    @Override public StoredMailMessage[] load(StoredMailFolder folder)
    {
	NullCheck.notNull(folder, "folder");
	return null;
    }

    @Override public void moveToFolder(StoredMailMessage message, StoredMailFolder folder)
    {
	NullCheck.notNull(folder, "folder");
	NullCheck.notNull(message, "message");
    }

    @Override public void delete(StoredMailMessage message)
    {
	NullCheck.notNull(message, "message");
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
