/*
   Copyright 2012-2019 Michael Pozhidaev <msp@luwrain.org>
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

package org.luwrain.pim.contacts.sql;

import java.sql.*;

import org.luwrain.core.*;
import org.luwrain.pim.*;
import org.luwrain.pim.contacts.*;
import org.luwrain.pim.util.*;

public class Storing implements ContactsStoring
{
    private final Registry registry;
    private final Connection con;
    private final ExecQueues execQueues;
    private final boolean highPriority;

    public Storing(Registry registry, Connection con, ExecQueues execQueues, boolean highPriority)
    {
	NullCheck.notNull(registry, "registry");
	NullCheck.notNull(con, "con");
	NullCheck.notNull(execQueues, "execQueues");
	this.registry = registry;
	this.con = con;
	this.execQueues = execQueues;
	this.highPriority = highPriority;
    }

    @Override public org.luwrain.pim.contacts.Contacts getContacts()
    {
	return null;
    }

    @Override public ContactsFolders getFolders()
    {
	return null;
    }
}
