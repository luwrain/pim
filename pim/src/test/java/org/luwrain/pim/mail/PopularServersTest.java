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

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class PopularServersTest
{
    @Test public void main() throws Exception
    {
	final var s = PopularServers.getPopularMailServers();
	assertNotNull(s);
	assertFalse(s.isEmpty());
	final var s1 = s.get(0);
	assertNotNull(s1);
	assertNotNull(s1.getSuffixes());
	assertEquals("@yandex.ru", s1.getSuffixes().get(0));
	assertNotNull(s1.getSmtp());
	assertEquals("smtp.yandex.ru", s1.getSmtp().getHost());
	assertNotNull(s1.getPop3());
	assertEquals("pop3.yandex.ru", s1.getPop3().getHost());
    }
}
