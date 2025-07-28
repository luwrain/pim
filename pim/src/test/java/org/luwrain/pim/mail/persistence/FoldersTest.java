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

package org.luwrain.pim.mail.persistence;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import org.luwrain.pim.mail.persistence.model.*;
import static org.luwrain.pim.mail.persistence.MailPersistence.*;

public class FoldersTest
{
    @Test public void main() throws Exception
    {
	//	deleteAllFolders();
	final var dao = getFolderDAO();
	assertNotNull(dao);
	var f = new Folder();
	f.setName("LUWRAIN");
	dao.add(f);
	final var res = dao.getAll();
	assertNotNull(res);
	assertEquals(1, res.size());
	f = res.get(0);
	assertNotNull(f);
	assertEquals("LUWRAIN", f.getName());
    }

        @Test public void parents() throws Exception
    {
	//		deleteAllFolders();
	final var dao = getFolderDAO();
	assertNotNull(dao);
	final var f1 = new Folder();
	f1.setName("PARENT");
	dao.add(f1);
	dao.setRoot(f1);
	assertEquals(f1.getId(), f1.getParentFolderId());
	assertEquals(1, f1.getId());
	final var f2 = new Folder();
	f2.setName("CHILD");
	f2.setParentFolderId(f1.getId());
	dao.add(f2);
	final var res = dao.getChildFolders(f1);
	assertNotNull(res);
	assertEquals(1, res.size());
	final var f = res.get(0);
	assertNotNull(f);
	assertEquals("CHILD", f.getName());
	assertEquals(f1.getId(), f.getParentFolderId());
	final var root = dao.getRoot();
		assertNotNull(root);
		assertEquals("PARENT", root.getName());
		assertEquals(f1.getId(), root.getId());
		assertEquals(root.getId(), root.getParentFolderId());
    }

}
