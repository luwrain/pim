/*
   Copyright 2012-2024 Michael Pozhidaev <msp@luwrain.org>

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

package org.luwrain.pim.mail;

import java.io.*;
import java.util.*;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import org.dizitart.no2.*;
import org.dizitart.no2.objects.*;
import org.dizitart.no2.objects.Cursor;
import static org.dizitart.no2.objects.filters.ObjectFilters.eq;
import static org.dizitart.no2.objects.filters.ObjectFilters.not;
import static org.dizitart.no2.objects.filters.ObjectFilters.and;

import org.luwrain.core.*;
import org.luwrain.pim.contacts.*;

public final class StoringTest
{
    @Disabled @Test public void main() throws Exception
    {
	final File tmpFile = File.createTempFile(".lwr-junit-pim-mail.", ".nitrite");
	final String messageId = "message123";
	tmpFile.delete();
	final Nitrite db = Nitrite.builder()
        .compressed()
        .filePath(tmpFile)
        .openOrCreate("luwrain", "passwd");
	final ObjectRepository<MailMessage> repo = db.getRepository(MailMessage.class);
	MailMessage m = new MailMessage();
	m.setMessageId(messageId);
	m.setCc(new String[]{"0", "1", "2"});
	m.setRawMessage(new byte[]{0, 1, 2});
	repo.insert(m);
	final Cursor<MailMessage> c = repo.find();
	final List<MailMessage> res = c.toList();
	assertNotNull(res);
	assertEquals(1, res.size());
	m = res.get(0);
	assertNotNull(m);
	assertEquals(messageId, m.getMessageId());
	String[] a = m.getCc();
	assertNotNull(a);
	assertEquals(3, a.length);
	for(int i = 0;i < a.length;i++)
	{
	    assertNotNull(a[i]);
	    assertEquals(String.valueOf(i), a[i]);
	}
	assertNotNull(m.getRawMessage());
	assertEquals(0, m.getRawMessage().length);
    }
}
