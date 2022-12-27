/*
   Copyright 2012-2022 Michael Pozhidaev <msp@luwrain.org>
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

package org.luwrain.pim.mail.protocols;

import java.util.*;
import java.io.*;

import org.luwrain.core.*;

import org.luwrain.pim.*;
import org.luwrain.pim.fetching.*;
import org.luwrain.pim.mail.*;
import org.luwrain.pim.mail.script.*;

import static org.luwrain.script.Hooks.*;

public final class Pop3 extends Base implements MailConversations.Listener
{
    static private final String
	LOG_COMPONENT = "pop3",
	HOOK_SAVE = "luwrain.pim.mail.save.new";

    private final MailStoring storing;
    private final MailHookObject mailHookObject;

    public Pop3(Control control, Strings strings) throws FetchingException, PimException, InterruptedException
    {
	super(control, strings);
	this.storing = org.luwrain.pim.Connections.getMailStoring(luwrain, false);
	if (storing == null)
	    throw new PimException("No connection");
	this.mailHookObject = new MailHookObject(storing);
    }

    public void fetch() throws InterruptedException
    {
	final MailAccount[] accounts;
	accounts = storing.getAccounts().load();
	Log.debug(LOG_COMPONENT, "loaded " + accounts.length + " account(s) for fetching mail");
	int used = 0;
	for(MailAccount a: accounts)
	{
	    checkInterrupted();
	    final MailAccount.Type type = a.getType();
	    if (type == MailAccount.Type.POP3)
	    {
		try {
		    processAccount(a);
		}
		catch(Throwable e)
		{
		    Log.error(LOG_COMPONENT, "unable to fetch mail from the account '" + a.getTitle() + "': " + e.getClass().getName() + ": " + e.getMessage());
		}
		checkInterrupted();
		++used;
	    }
	}
	if (used <= 0)
	    message(strings.noMailAccountsForFetching());
    }

    private void processAccount(MailAccount account) throws IOException, PimException, InterruptedException
    {
	final String title = account.getTitle();
	Log.debug(LOG_COMPONENT, "fetching POP3 mail from the account '" + account.getTitle() + "', flags " + account.getFlags());
	if (!account.getFlags().contains(MailAccount.Flags.ENABLED))
	{
	    Log.debug(LOG_COMPONENT, "the account '" + account.getTitle() + "' is disabled");
	    message(strings.skippingFetchingFromDisabledAccount(title));
	    return;
	}
	control.message(strings.fetchingMailFromAccount(title));
	Log.debug(LOG_COMPONENT, "connecting to the POP3 server:" + account.getHost() + ":" + account.getPort());
	control.message(strings.connectingTo(account.getHost() + ":" + account.getPort()));
	final MailConversations conversation = new MailConversations(createMailServerParams(account), true);
	Log.debug(LOG_COMPONENT, "connection established");
	message(strings.connectionEstablished(account.getHost() + ":" + account.getPort()));
	conversation.fetchPop3("inbox", this, !account.getFlags().contains(MailAccount.Flags.LEAVE_MESSAGES));
	Log.debug(LOG_COMPONENT, "fetching from the account finished");
    }

    @Override public void numberOfNewMessages(int count, boolean haveMore)
    {
	Log.debug(LOG_COMPONENT, String.valueOf(count) + " messages");
	if (count <= 0)
	    return;
	//			control.message(strings.numberOfNewMessages("" + count, title));
	if (haveMore)
	    control.message(strings.noAllMessagesToBeFetched());
    }

    @Override public boolean saveMessage(byte[] bytes, int num, int total)
    {
	final MailMessage message;
	try {
	    message = BinaryMessage.fromByteArray(bytes);
	}
	catch(PimException | IOException e)
	{
	    Log.error(LOG_COMPONENT, "unable to create a message object: " + e.getClass().getName() + ": " + e.getMessage());
	    return false;
	}
	final MessageObj hookObj = new MessageObj(message);
	try {
	    Log.debug(LOG_COMPONENT, "saving the message " + num + "/" + total + " from " + message.getFrom());
	    return chainOfResponsibility(luwrain, HOOK_SAVE, new Object[]{mailHookObject, hookObj});
	}
	catch(Throwable e)
	{
	    Log.error(LOG_COMPONENT, "unable to save the message: " + e.getClass().getName() + ": " + e.getMessage());
	    return false;
	}
    }
}
