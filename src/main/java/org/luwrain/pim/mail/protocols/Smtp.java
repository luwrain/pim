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
import java.util.regex.*;

import org.luwrain.core.*;
import org.luwrain.pim.*;
import org.luwrain.pim.mail.*;
import org.luwrain.pim.fetching.*;
import org.luwrain.io.json.*;

public final class Smtp extends Base
{
    static private final String
	LOG_COMPONENT = "smtp";

        static public final class Result
    {
	public final int total;
	public final int sent;
	public final Map<Integer, Throwable> errors;
	Result()
	{
	    this.total = 0;
	    this.sent = 0;
	    this.errors = new HashMap<>();
	}
	Result(int total, int sent, Map<Integer, Throwable> errors)
	{
	    	    NullCheck.notNull(errors, "errors");
	    if (total < 0)
		throw new IllegalArgumentException("total (" + total + ") may not be negative");
	    if (sent < 0)
		throw new IllegalArgumentException("sent (" + sent + ") may not be negative");
	    this.total = total;
	    this.sent = sent;
	    this.errors = errors;
	}
    }


    private final MailStoring storing;
    private final MailFolder pending, sent;

    public Smtp(Control control, Strings strings)
    {
	super(control, strings);
	this.storing = org.luwrain.pim.Connections.getMailStoring(luwrain, false);
	if (storing == null)
	    throw new PimException("Mail storing is unavailable");
	this.pending = storing.getFolders().findFirstByProperty("defaultOutgoing", "true");
	this.sent = storing.getFolders().findFirstByProperty("defaultSent", "true");
	if (this.pending == null || this.sent == null)
throw new PimException("No default groups for mail sending");
    }

    public Result send() throws InterruptedException
    {
	final MailMessage[] messages = storing.getMessages().load(pending);
	Log.debug(LOG_COMPONENT, "loading " + messages.length + " message(s) to send");
	if (messages.length == 0)
	{
	    message("Нет сообщений для отправки");//FIXME :
	    return new Result();
	}
	final Map<Integer, List<MailMessage>> queues = new HashMap<>();
	for(MailMessage m: messages)
	{
	    checkInterrupted();
	    final Integer accountId = MessageSendingData.getAccountId(m);
	    if (accountId == null || accountId.intValue() < 0)
	    {
		Log.warning(LOG_COMPONENT, "encountering the message without associated mail account, skipping");
		continue;
	    }
	    List<MailMessage> mm = queues.get(accountId);
	    	    if (mm == null)
	    {
		mm = new ArrayList<>();
		queues.put(accountId, mm);
			   }
		    mm.add(m);
		    	}
	Log.debug(LOG_COMPONENT, "prepared " + queues.size() + " queue(s)");
	final Map<Integer, Throwable> errors = new HashMap<>();
	int sentCount = 0;
	for(Map.Entry<Integer, List<MailMessage>> e: queues.entrySet() )
	{
	    checkInterrupted();
	    try {
		sentCount += sendQueue(e.getKey().intValue(), e.getValue());
	    }
	    catch (Throwable ex)
	    {
		//		Log.error(LOG_COMPONENT, "unable to send the messages from the queue \'" + queue.accountUniRef + "\':" + e.getClass().getName() + ":" + e.getMessage());
		errors.put(e.getKey(), ex);
		message("Произошла ошибка отправки очереди для учётной записи " + e.getKey());
	    }
	}
	return new Result(messages.length, sentCount, errors);
    }

    private int sendQueue(int accountId, List<MailMessage> messages) throws IOException, InterruptedException
    {
	final MailAccount account = storing.getAccounts().loadById(accountId);
	if (account == null)
	{
	    message(strings.errorLoadingMailAccount(String.valueOf(accountId)));
	    throw new PimException("No account with id=" + accountId);
	}
	if (!account.getFlags().contains(MailAccount.Flags.ENABLED))
	{
	    message(strings.mailAccountDisabled(account.getTitle()));
	    throw new PimException("The mail account '" + account.getTitle() + "' is disabled");
	}
	    message(strings.messagesInQueueForAccount(account.getTitle(), String.valueOf(messages.size())));
	final MailConnections conn = new MailConnections(createMailServerParams(account), false);
	message(strings.connectingTo(account.getHost() + ":" + account.getPort()));
	control.message(strings.connectionEstablished(account.getHost() + ":" + account.getPort()));
	int count = 0;
	for(MailMessage message: messages)
	{
	    checkInterrupted();
	    message(strings.sendingMessage(String.valueOf(count), String.valueOf(messages.size())));
	    conn.send(message.getRawMessage());
	    storing.getMessages().moveToFolder(message, sent);
	    ++count;
	}
	return count;
    }

}
