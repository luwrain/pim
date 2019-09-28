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

package org.luwrain.pim.fetching;

import java.util.*;
import java.io.*;

import org.luwrain.core.*;
import org.luwrain.pim.*;
import org.luwrain.pim.mail.*;
import org.luwrain.pim.mail.script.*;

public final class Pop3 extends Base implements MailConversations.Listener
{
    static private final String HOOK_NAME_SAVE = "luwrain.pim.message.new.save";

    private final MailStoring storing;
    private final MailHookObject mailHookObject;

    public Pop3(Control control, Strings strings) throws FetchingException, PimException, InterruptedException
    {
	super(control, strings);
	this.storing = org.luwrain.pim.Connections.getMailStoring(luwrain, false);
	if (storing == null)
	    throw new FetchingException("Отсутствует соединение");
	this.mailHookObject = new MailHookObject(storing);
    }

    public void fetch() throws PimException, InterruptedException
    {
	final StoredMailAccount[] accounts;
	try {
	    accounts = storing.getAccounts().load();
	}
	catch(PimException e)
	{
	    throw new FetchingException(strings.errorLoadingMailAccounts(e.getClass().getName() + ":" + e.getMessage()));
	}
	Log.debug(LOG_COMPONENT, "loaded " + accounts.length + " accounts for fetching mail");
	int used = 0;
	for(StoredMailAccount a: accounts)
	{
	    checkInterrupted();
	    final MailAccount.Type type;
	    try {
		type = a.getType();
	    }
	    catch(PimException e)
	    {
		control.luwrain().crash(e);
		continue;
	    }
	    if (type == MailAccount.Type.POP3)
	    {
		try {
		    processAccount(a);
		}
		catch(PimException | IOException e)
		{
		    //FIXME:
		}
		checkInterrupted();
		++used;
	    }
	}
	if (used <= 0)
	    message(strings.noMailAccountsForFetching());
    }

    private void processAccount(StoredMailAccount account) throws IOException, PimException, InterruptedException
    {
	NullCheck.notNull(account, "account");
	final String title = account.getTitle();
	Log.debug(LOG_COMPONENT, "fetching POP3 mail from account \"" + account.getTitle() + "\", flags " + account.getFlags());
	if (!account.getFlags().contains(MailAccount.Flags.ENABLED))
	{
	    message(strings.skippingFetchingFromDisabledAccount(title));
	    return;
	}
	control.message(strings.fetchingMailFromAccount(title));
	Log.debug(LOG_COMPONENT, "connecting to POP3 server:" + account.getHost() + ":" + account.getPort());
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
		NullCheck.notNull(bytes, "bytes");
	final MailMessage message;
	try {
	    message = BinaryMessage.fromByteArray(bytes);
	    Log.debug(LOG_COMPONENT, "saving the message with ID " + message.getMessageId());
	}
	catch(PimException | IOException e)
	{
	    Log.error(LOG_COMPONENT, "unable to create a message object:" + e.getMessage());
	    return false;
	}
	final MessageHookObject hookObj = new MessageHookObject(message);
	try {
	    return luwrain.xRunHooks(HOOK_NAME_SAVE, new Object[]{mailHookObject, hookObj}, Luwrain.HookStrategy.CHAIN_OF_RESPONSIBILITY);
	}
	catch(RuntimeException e)
	{
	    Log.error(LOG_COMPONENT, "unable to save a message:" + e.getClass().getName() + ":" + e.getMessage());
	    return false;
	}
    }
}
