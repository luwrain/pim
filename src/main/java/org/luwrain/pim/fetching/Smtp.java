/*
   Copyright 2012-2018 Michael Pozhidaev <michael.pozhidaev@gmail.com>

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
import org.luwrain.network.*;

public class Smtp extends Base
{
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

    void send() throws Exception
    {
	final StoredMailMessage[] messages = storing.getMessages().load(pending);
	if (messages.length == 0)
	{
	    message("Нет сообщений для отправки");//FIXME :
	    return;
	}
	final List<PendingQueue> queues = new LinkedList();
	for(StoredMailMessage m: messages)
	{
	    checkInterrupted();
	    final String uniRef = m.getExtInfo();
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
	for(PendingQueue queue: queues)
	{
	    checkInterrupted();
	    try {
		sendQueue(queue);
	    }
	    catch(InterruptedException e)
	    {
		throw e;
	    }
	    catch (Exception e)
	    {
		message("Произошла ошибка отправки очереди для учётной записи " + queue.accountUniRef);
	    }
	}
    }

    private void sendQueue(PendingQueue queue) throws IOException, PimException, InterruptedException
    {
	NullCheck.notNull(queue, "queue");
	final StoredMailAccount account = storing.getAccounts().loadByUniRef(queue.accountUniRef);
	if (account == null)
	{
	    message(strings.errorLoadingMailAccount(queue.accountUniRef));
	    return;
	}
	if (!account.getFlags().contains(MailAccount.Flags.ENABLED))
	{
	    message(strings.mailAccountDisabled(account.getTitle()));
	    return;
	}
	message(strings.messagesInQueueForAccount(account.getTitle(), "" + queue.messages.size()));
	final MailServerConversations.Params params = new MailServerConversations.Params();
	params.host = account.getHost();
	params.port = account.getPort();
	params.ssl = account.getFlags().contains(MailAccount.Flags.SSL);
	params.tls = account.getFlags().contains(MailAccount.Flags.TLS);
	params.login = account.getLogin();
	params.passwd = account.getPasswd();
	final MailServerConversations conversation = new MailServerConversations(params);
	message(strings.connectingTo(account.getHost() + ":" + account.getPort()));
	//	conversation.initSmtp(settings, account.getHost(), 
	//			      account.getLogin(), account.getPasswd());
	control.message(strings.connectionEstablished(account.getHost() + ":" + account.getPort()));
	int count = 1;
	for(StoredMailMessage message: queue.messages)
	{
	    checkInterrupted();
	    message(strings.sendingMessage("" + count, "" + queue.messages.size()));
	    conversation.send(message.getRawMessage());
	    storing.getMessages().moveToFolder(message, sent);
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
