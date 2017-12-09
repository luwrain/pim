/*
   Copyright 2012-2017 Michael Pozhidaev <michael.pozhidaev@gmail.com>
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

import org.luwrain.core.*;
import org.luwrain.pim.*;
import org.luwrain.pim.mail.*;

class Folders implements MailFolders
{
    	    @Override public int getId(StoredMailFolder folder)
    {
	NullCheck.notNull(folder, "folder");
	return 0;
    }

    @Override public StoredMailFolder loadById(int id)
    {
	return null;
    }

    @Override public void save(StoredMailFolder parentFolder, MailFolder newFolder)
    {
	NullCheck.notNull(parentFolder, "parentFolder");
	NullCheck.notNull(parentFolder, "parentFolder");
    }

    @Override public StoredMailFolder getRoot()
    {
	return null;
    }

    @Override public StoredMailFolder[] load(StoredMailFolder folder)
    {
	return null;
    }

    @Override public String getUniRef(StoredMailFolder folder)
    {
	return null;
    }

    @Override public StoredMailFolder loadByUniRef(String uniRef)
    {
	return null;
    }
}
