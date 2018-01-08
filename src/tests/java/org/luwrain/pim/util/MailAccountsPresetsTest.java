/*
   Copyright 2012-2017 Michael Pozhidaev <michael.pozhidaev@gmail.com>

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

package org.luwrain.pim.util;

import java.io.*;
import java.util.*;

import org.junit.*;

import org.luwrain.core.*;

public class MailAccountsPresetsTest extends Assert
{
    static private String PROP_PATH = "/org/luwrain/pim/accounts.properties";

    @Test public void listTest() throws IOException
    {
	final InputStream is = getClass().getResourceAsStream(PROP_PATH);
	assertNotNull(is);
	try {
	    final Properties props = new Properties();
	    props.load(is);
	}
	finally {
	    is.close();
	}
    }
}
