/*
   Copyright 2012-2019 Michael Pozhidaev <michael.pozhidaev@gmail.com>
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
import java.util.regex.*;

import org.luwrain.core.*;
import org.luwrain.pim.*;
import org.luwrain.pim.mail.*;

public class Smtp extends Base
{
    static private final String LOG_COMPONENT = "pim-smtp";

    private final MailStoring storing;
    private final StoredMailFolder pending;
    private StoredMailFolder sent;

    public Smtp(Control control, Strings strings) throws PimException
    {
	super(control, strings);
	this.storing = org.luwrain.pim.Connections.getMailStoring(luwrain, false);
	if (storing == null)
	    throw new FetchingException("Отсутствует соединение");
	final org.luwrain.pim.Settings.MailFolders sett = org.luwrain.pim.Settings.createMailFolders(luwrain.getRegistry());
	this.pending = storing.getFolders().loadByUniRef(sett.getFolderPending(""));
	this.sent = storing.getFolders().loadByUniRef(sett.getFolderSent(""));
	if (this.pending == null || this.sent == null)
throw new FetchingException("Не удалось подготовить почтовые группы, доставка сообщений отменена");
    }

    public Result fetch() throws PimException, InterruptedException
    {
	final StoredMailMessage[] messages = storing.getMessages().load(pending);
	Log.debug(LOG_COMPONENT, "loading " + messages.length + " message(s) to send");
	if (messages.length == 0)
	{
	    message("Нет сообщений для отправки");//FIXME :
	    return new Result();
	}
	final List<PendingQueue> queues = new LinkedList();
	for(StoredMailMessage m: messages)
	{
	    checkInterrupted();
	    final String uniRef = m.getExtInfo();
	    if (uniRef.trim().isEmpty())
	    {
		Log.warning(LOG_COMPONENT, "encountering the message without associated mail account, skipping");
		continue;
	    }
	    PendingQueue queue = null;
	    for(PendingQueue q: queues)
		if (q.accountUniRef.equals(uniRef))
		{
		    queue = q;
		    break;
		}
	    if (queue != null)
	    {
		queue.messages.add(m);
		continue;
	    }
	    queue = new PendingQueue(uniRef);
	    queue.messages.add(m);
	    queues.add(queue);
	}
	Log.debug(LOG_COMPONENT, "prepared " + queues.size() + " queue(s)");
	final Map<String, Throwable> errors = new HashMap();
			for(PendingQueue q: queues)
		    Log.debug(LOG_COMPONENT, q.accountUniRef + " with " + q.messages.size() + " message(s)");
			int sentCount = 0;
		    	for(PendingQueue queue: queues)
	{
	    checkInterrupted();
	    try {
		sentCount += sendQueue(queue);
	    }
	    catch(InterruptedException e)
	    {
		throw e;
	    }
	    catch (Throwable e)
	    {
				Log.error(LOG_COMPONENT, "unable to send the messages from the queue \'" + queue.accountUniRef + "\':" + e.getClass().getName() + ":" + e.getMessage());
		errors.put(queue.accountUniRef, e);
		message("Произошла ошибка отправки очереди для учётной записи " + queue.accountUniRef);
	    }
	}
			return new Result(messages.length, sentCount, errors);
    }

    private int sendQueue(PendingQueue queue) throws IOException, PimException, InterruptedException
    {
	NullCheck.notNull(queue, "queue");
	final StoredMailAccount account = storing.getAccounts().loadByUniRef(queue.accountUniRef);
	if (account == null)
	{
	    message(strings.errorLoadingMailAccount(queue.accountUniRef));
	    throw new FetchingException("No mail account for the uniref \'" + queue.accountUniRef + "\'");
	}
	if (!account.getFlags().contains(MailAccount.Flags.ENABLED))
	{
	    message(strings.mailAccountDisabled(account.getTitle()));
	    throw new FetchingException("The mail account \'" + account.getTitle() + "\' is disabled");
	}
	message(strings.messagesInQueueForAccount(account.getTitle(), "" + queue.messages.size()));
	final MailConversations conversation = new MailConversations(createMailServerParams(account), false);
	message(strings.connectingTo(account.getHost() + ":" + account.getPort()));
	control.message(strings.connectionEstablished(account.getHost() + ":" + account.getPort()));
	int count = 0;
	for(StoredMailMessage message: queue.messages)
	{
	    checkInterrupted();
	    message(strings.sendingMessage("" + count, "" + queue.messages.size()));
	    conversation.send(message.getRawMessage());
	    storing.getMessages().moveToFolder(message, sent);
	    ++count;
	}
	return count;
    }

    static public final class Result
    {
	public final int total;
	public final int sent;
	public final Map<String, Throwable> errors;

	Result()
	{
	    this.total = 0;
	    this.sent = 0;
	    this.errors = new HashMap();
	}

	Result(int total, int sent, Map<String, Throwable> errors)
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

    static private final class PendingQueue
    {
	final String accountUniRef; 
	final List<StoredMailMessage> messages = new LinkedList();

	PendingQueue(String accountUniRef)
	{
	    NullCheck.notEmpty(accountUniRef, "accountUniRef");
	    this.accountUniRef = accountUniRef;
	}
    }
}
