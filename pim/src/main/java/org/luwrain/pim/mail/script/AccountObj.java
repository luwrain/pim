// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>
// Copyright 2015 Roman Volovodov <gr.rPman@gmail.com>

package org.luwrain.pim.mail.script;

import java.util.*;

import org.luwrain.core.*;
import org.luwrain.script.core.*;
import org.luwrain.pim.*;
import org.luwrain.pim.mail.*;
import org.luwrain.pim.mail.persistence.model.*;

import static org.luwrain.script.ScriptUtils.*;

public class AccountObj extends MapScriptObject
{
    final Account account;

    public AccountObj(Account account)
    {
	super(createMap(account));
	NullCheck.notNull(account, "account");
	this.account = account;
    }

    static private Map<String, Object> createMap(Account account)
    {
	final Map<String, Object> map = new HashMap<>();
	map.put("type", account.getType().toString());
	map.put("title", account.getName());//FIXME:
	map.put("host", account.getHost());
	map.put("port", account.getPort());
	map.put("login", account.getLogin());
	map.put("passwd", account.getPasswd());
	map.put("trustedHosts", account.getTrustedHosts());
//	map.put("flags", createEnumSet(account.getFlags()));
	map.put("substName", account.getSubstName());
	map.put("substAddress", account.getSubstAddress());
	return map;
    }
}
