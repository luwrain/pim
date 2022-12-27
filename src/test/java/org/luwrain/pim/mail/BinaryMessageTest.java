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

package org.luwrain.pim.mail;

import java.util.*;
import java.io.*;

import org.junit.*;

//import org.luwrain.core.*;

import static org.luwrain.util.StreamUtils.*;
import static org.luwrain.pim.mail.BinaryMessage.*;

public class BinaryMessageTest extends Assert
{
    private byte[] rawMessage = null;

    @Test public void parsing() throws IOException
    {
	final MailMessage m = fromByteArray(rawMessage);
	assertNotNull(m);
	assertNotNull(m.getFrom());
	assertEquals("Редактор новостей Радио РАНСиС <news@ransis.org>", m.getFrom());
	assertNotNull(m.getSubject());
	assertEquals("[RANSIS:8682] Инфо. \"Спецхран\" (анонс)", m.getSubject());
	assertNotNull(m.getTo());
	assertEquals(1, m.getTo().length);
	assertNotNull(m.getTo()[0]);
	assertEquals("ransis@googlegroups.com", m.getTo()[0]);

	assertNotNull(m.getContentType());
	assertEquals("text/plain; charset=\"UTF-8\"", m.getContentType());
	final String text = m.getText();
	assertNotNull(text);
	assertEquals(1580, text.length());
	final String[] lines = text.split("\n", -1);
	assertEquals(42, lines.length);
	assertEquals("   Уважаемые подписчики,\\r", lines[0].replaceAll("\r", "\\\\r"));

	assertNotNull(m.getMessageId());
	assertEquals("", m.getMessageId());//FIXME:

	final Date d = m.getSentDate();
	assertNotNull(m);
	assertEquals(122, d.getYear());
	assertEquals(9, d.getMonth());
		assertEquals(29, d.getDate());
    }

    @Before public void readRawMessage() throws IOException
    {
	final ByteArrayOutputStream os = new ByteArrayOutputStream();
	try (final InputStream is = getClass().getResourceAsStream("test.eml")) {
	    copyAllBytes(is, os);
	}
	this.rawMessage = os.toByteArray();
    }
    }
