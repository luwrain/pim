/*
   Copyright 2012-2021 Michael Pozhidaev <msp@luwrain.org>
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

//LWR_API 1.0

package org.luwrain.pim.mail.script;

import java.util.*;

import org.luwrain.core.*;
import org.luwrain.script.core.*;
import org.luwrain.pim.*;
import org.luwrain.pim.mail.*;

import static org.luwrain.script2.ScriptUtils.*;

public class AccountObj extends MapScriptObject
{
    final MailAccount account;

    public AccountObj(MailAccount account)
    {
	super(createMap(account));
	NullCheck.notNull(account, "account");
	this.account = account;
    }

    static private Map<String, Object> createMap(MailAccount account)
    {
	final Map<String, Object> map = new HashMap<>();
	map.put("type", account.getType().toString());
	map.put("title", account.getTitle());
	map.put("host", account.getHost());
	map.put("port", account.getPort());
	map.put("login", account.getLogin());
	map.put("passwd", account.getPasswd());
	map.put("trustedHosts", account.getTrustedHosts());
	map.put("flags", createEnumSet(account.getFlags()));
	map.put("substName", account.getSubstName());
	map.put("substAddress", account.getSubstAddress());
	return map;
    }
}
