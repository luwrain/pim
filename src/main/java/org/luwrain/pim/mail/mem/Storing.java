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

import java.util.*;

import org.luwrain.core.*;
import org.luwrain.pim.*;
import org.luwrain.pim.mail.*;

class Storing implements MailStoring
{
    private final Accounts accounts;
    private final Folders folders;
    private final Rules rules;
    private final Messages messages;

    Storing()
    {
	this.accounts = new Accounts();
	this.folders = new Folders();
	this.rules = new Rules();
	this.messages = new Messages();
    }

    @Override public MailRules getRules()
    {
	return rules;
    }

    @Override public MailMessages getMessages()
    {
	return messages;
    }

    @Override public MailFolders getFolders()
    {
	return folders;
    }

    @Override public MailAccounts getAccounts()
    {
	return accounts;
    }
}
