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

package org.luwrain.pim.mail.sql;

import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.concurrent.*;

import org.luwrain.core.*;
import org.luwrain.pim.*;
import org.luwrain.pim.mail.*;
import org.luwrain.pim.util.*;

public final class Storing implements MailStoring, ExecQueue
{
    static final String LOG_COMPONENT = "pim";

    private final Registry registry;
    private final Connection con;
    private final ExecQueues execQueues;
    private final boolean highPriority;

    private final Accounts accounts;
    private final Rules rules;
    private final Folders folders;
    private final Messages messages;

    public Storing(Registry registry,Connection con, ExecQueues execQueues, boolean highPriority, File messagesDir)
    {
	NullCheck.notNull(registry, "registry");
	NullCheck.notNull(con, "con");
	NullCheck.notNull(execQueues, "execQueues");
	NullCheck.notNull(messagesDir, "messagesDir");
	this.registry = registry;
	this.con = con;
	this.execQueues = execQueues;
	this.highPriority = highPriority;
	this.rules = new Rules(registry);
		this.messages = new Messages(this, con, messagesDir);
		this.folders = new Folders(registry, messages);
	this.accounts = new Accounts(registry);
	    }

    @Override public MailRules getRules()
    {
	return null;//rules;
    }

    @Override public MailFolders getFolders()
    {
	return folders;
    }

    @Override public MailAccounts getAccounts()
    {
	return accounts;
    }

    @Override public MailMessages getMessages()
    {
	return messages;
    }

    @Override public Object execInQueue(Callable callable) throws Exception
    {
	NullCheck.notNull(callable, "callable");
	return execQueues.exec(new FutureTask(callable), highPriority);
    }

        @Override public     String combinePersonalAndAddr(String personal, String addr)
    {
	NullCheck.notNull(personal, "personal");
	NullCheck.notNull(addr, "addr");
	return AddressUtils.combinePersonalAndAddr(personal, addr);
    }
}
