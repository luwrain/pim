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

//LWR_API 1.0

package org.luwrain.pim.mail.script;

import java.util.*;

import org.luwrain.core.*;
import org.luwrain.script.*;
import org.luwrain.pim.*;
import org.luwrain.pim.mail.*;

public class AccountHookObject extends EmptyHookObject
{
    static final String LOG_COMPONENT = MailHookObject.LOG_COMPONENT;

    final MailAccount account;

    public AccountHookObject(MailAccount account)
    {
	NullCheck.notNull(account, "account");
	this.account = account;
    }

    @Override public Object getMember(String name)
    {
	NullCheck.notNull(name, "name");
	try {
	switch(name)
	{
case "type":
    return account.getType().toString().toLowerCase();
case "title":
return account.getTitle();
case "host":
return account.getHost();
case "port":
return account.getPort();
case "login":
return account.getLogin();
case "passwd":
return account.getPasswd();
case "trustedHosts":
return account.getTrustedHosts();
case "flags":
return ScriptUtils.createEnumSet(account.getFlags());
case "substName":
return account.getSubstName();
case "substAddress":
return account.getSubstAddress();
default:
return super.getMember(name);
	}
}
catch(PimException e)
{
Log.warning(LOG_COMPONENT, "unable to get the member \'" + name + "\' of the mail account:" + e.getClass().getName() + ":" + e.getMessage());
return null;
}
    }

/*
    setMember()
    {
	    public Type type = Type.POP3;
    public String title = "";
    public String host = "";
    public int port = 995;
    public String login = "";
    public String passwd = "";
    public String trustedHosts = "*";
    public Set<Flags> flags = EnumSet.noneOf(Flags.class);
    public String substName = "";
    public String substAddress = "";

    }
*/
}
