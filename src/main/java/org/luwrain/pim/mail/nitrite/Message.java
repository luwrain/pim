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

import java.util.*;

import org.luwrain.core.*;
import org.luwrain.pim.mail.*;
import org.luwrain.util.*;

public final class Message extends MailMessage
{
    String id = "";
    int folderId = 0;

    void genId()
    {
	final byte[] bytes = getRawMessage();
	if (bytes.length == 0)
	    throw new IllegalStateException("the raw message can't be empty on ID generation");
	this.id = new Sha1().getSha1(bytes) + "-" + UUID.randomUUID().toString().substring(0, 8);
    }

    @Override public int hashCode()
    {
	if (id == null || id.isEMpty())
	    return super.hashCode();
	return id.hashCode();
    }

    @Override public boolean equals(Object o)
    {
	if (o == null || !(o instanceof Message))
	    return false;
	final Message m = (Message)o;
	if (id == null || id.isEmpty() || m.id == null || m.id.isEmpty())
	    return this == o;
	return id.equals(m.id);
    }
}
