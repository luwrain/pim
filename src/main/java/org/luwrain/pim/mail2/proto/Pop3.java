/*
   Copyright 2012-2024 Michael Pozhidaev <msp@luwrain.org>
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

package org.luwrain.pim.mail2.proto;

import java.util.*;
import java.util.function.*;
import java.io.*;

import org.luwrain.core.*;
import org.luwrain.pim.*;
import org.luwrain.pim.fetching.*;
//import org.luwrain.pim.mail.*;
import org.luwrain.pim.mail2.*;
import org.luwrain.pim.mail2.persistence.model.*;

import static org.luwrain.core.NullCheck.*;

public final class Pop3 implements MessageProvider<Pop3.ExtData>
{
    static private final String
	LOG_COMPONENT = "pop3";

    static public final class ExtData
    {
	public final int msgNum, totalMsgCount;
	public ExtData(int msgNum , int totalMsgCount)
	{
	    this.msgNum = msgNum;
	    this.totalMsgCount = totalMsgCount;
	}
    }

    protected final org.luwrain.pim.mail2.persistence.model.Account account;

    public Pop3(Account account)
    {
	notNull(account, "account");
	this.account = account;
    }

    @Override public void getMessages(BiConsumer<Message, ExtData> c)
    {
	final String name = account.getName();
	Log.debug(LOG_COMPONENT, "fetching POP3 mail from: " + account.getName());
	if (!account.isEnabled())
	{
	    Log.debug(LOG_COMPONENT, "the account '" + account.getName() + "' is disabled");
	    return;
	}
	Log.debug(LOG_COMPONENT, "connecting to the POP3 server:" + account.getHost() + ":" + account.getPort());
	final var con = new MailConnections(createMailServerParams(), true);
	Log.debug(LOG_COMPONENT, "connection established");
	try {
	    con.fetchPop3("inbox", new MailConnections.Listener(){
		    @Override public void numberOfNewMessages(int count, boolean haveMore) {}
		    @Override public boolean saveMessage(byte[] bytes, int num, int total)
		    {
			final Message m = new Message();
			m.setRawMessage(bytes);
			c.accept(m, new ExtData(num, total));
			return true;
		    }},
		!account.isLeaveMessages());
	}
	catch(InterruptedException e)
	{
	    Thread.currentThread().interrupt();
	    return;
	}
	catch(IOException e)
	{
	    throw new PimException(e);
	}
	Log.debug(LOG_COMPONENT, "fetching from the account finished");
    }

    MailConnections.Params createMailServerParams()
    {
	final var params = new MailConnections.Params();
	params.doAuth = !account.getLogin().isEmpty();
	params.host = account.getHost();
	params.port = account.getPort();
	params.ssl = account.isSsl();
	params.tls = account.isTls();
	params.login = account.getLogin();
	params.passwd = account.getPasswd();
	if (account.getTrustedHosts() != null && !account.getTrustedHosts().isEmpty())
	params.extProps.put( "mail.pop3.ssl.trust", account.getTrustedHosts());
	return params;
    }
}
