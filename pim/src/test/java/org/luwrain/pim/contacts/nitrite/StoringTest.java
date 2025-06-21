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

package org.luwrain.pim.contacts.nitrite;

import java.io.*;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import org.luwrain.core.*;
import org.luwrain.pim.contacts.*;

public final class StoringTest
{
    /*
@Test public void main() throws Exception
    {
	final File tmpFile = File.createTempFile(".lwr-junit-pim-contacts.", ".nitrite");
	tmpFile.delete();
	final Storing s = new Storing(tmpFile);
	ContactsFolder f = new ContactsFolder();
	f.setTitle("Proba");
	s.getFolders().save(null, f);
	ContactsFolder[] ff = s.getFolders().load(null);
	assertNotNull(ff);
	assertEquals(1, ff.length);
	assertEquals("Proba", ff[0].getTitle());
    }
    */
}
