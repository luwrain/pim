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

package org.luwrain.pim.mail.sql;

import java.sql.*;
import java.util.*;

import org.luwrain.core.*;
import org.luwrain.pim.*;
import org.luwrain.pim.mail.*;

public final class Storing implements MailStoring
{
    private final Registry registry;
    private final Connection con;

    public Storing(Registry registry,Connection con)
    {
	NullCheck.notNull(registry, "registry");
	NullCheck.notNull(con, "con");
	this.registry = registry;
	this.con = con;
	    }

    @Override public MailRules getRules()
    {
	return null;//FIXME:
    }

    @Override public MailFolders getFolders()
    {
	return null;
    }

    @Override public MailAccounts getAccounts()
    {
	return null;
    }

    @Override public MailMessages getMessages()
    {
	return null;
    }
}
