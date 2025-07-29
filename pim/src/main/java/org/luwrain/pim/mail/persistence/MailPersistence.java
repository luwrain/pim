/*
   Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>
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

package org.luwrain.pim.mail.persistence;

import java.util.*;
import java.util.concurrent.*;
import org.h2.mvstore.*;

import org.luwrain.pim.storage.*;
import org.luwrain.pim.mail.persistence.dao.*;
import org.luwrain.pim.mail.persistence.model.*;

import static java.util.Objects.*;
import static org.luwrain.pim.storage.ExecQueues.*;

public final class MailPersistence
{
    final ExecQueues queues;
    private Priority priority = Priority.MEDIUM;
    private Runner runner = null;
    private final MVMap<Integer, Account> accountsMap = null;
        private final MVMap<Long, MessageMetadata> messagesMap = null;

    public MailPersistence(ExecQueues queues)
    {
	this.queues = requireNonNull(queues, "queues can't be null");
	this.runner = new Runner(queues, priority);
    }

    public AccountDAO getAccountDAO()
    {
	return new AccountDAO(){
	    @Override public Account getById(int id)
	    {
		if (id < 0)
		    throw new IllegalArgumentException("id can't be negative");
		return runner.run(new FutureTask<>(() ->  accountsMap.get(Integer.valueOf(id)) ));
	    }

	    @Override public void delete(Account account)
	    {
		requireNonNull(account, "account can't be null");
		if (account.getId() < 0)
		    throw new IllegalArgumentException("An account can't have negative ID");
		runner.run(new FutureTask<>(() -> accountsMap.remove(Integer.valueOf(account.getId())) ));
	    }

	    @Override public int add(Account account)
	    {
		requireNonNull(account, "account can't be null");
		return runner.run(new FutureTask<>( () -> {
			    final Integer maxKey = accountsMap.floorKey(Integer.MAX_VALUE);
			    account.setId(maxKey.intValue() + 1);
			    accountsMap.put(Integer.valueOf(account.getId()), account);
			    return Integer.valueOf(account.getId());
		})).intValue();
	    }

	    @Override public List<Account> getAll()
	    {
		return runner.run(new FutureTask<>( () -> {
			    return accountsMap.entrySet().stream()
			    .map(e -> e.getValue())
			    .toList();
		}));
	    }

	    @Override public void update(Account account)
	    {
		requireNonNull(account, "account can't be null");
		if (account.getId() < 0)
		    throw new IllegalArgumentException("An account can't have negative ID");
		runner.run(new FutureTask<Object>( () ->  accountsMap.put(Integer.valueOf(account.getId()), account), null));
	    }
	};
    }

public FolderDAO getFolderDAO()
    {
	return new FolderDAO(){
	    @Override public void add(Folder folder)
	    {
			    }

	    	        @Override public List<Folder> getAll()
	    {
		return null;
			    }

	    	    	        @Override public List<Folder> getChildFolders(Folder folder)
	    {
		return null;
	    }

	    	    @Override public void update(Folder folder)
	    {
	    }

	    @Override public Folder getRoot()
	    {
		return null;
			    }

	    	    	    	        @Override public void setRoot(Folder folder)
	    {
	    }

	    @Override public Folder findFirstByProperty(String propName, String propValue)
	    {
		return null;
	    }
	};
    }

public MessageDAO getMessageDAO()
    {
	return new MessageDAO(){
	    @Override public long add(MessageMetadata message)
	    {
		requireNonNull(message, "message can't be null");
		return runner.run(new FutureTask<>( () -> {
			    final var maxKey = messagesMap.floorKey(Long.MAX_VALUE);
			    final long newId = maxKey.longValue() + 1;
			    message.setId(newId);
			    messagesMap.put(Long.valueOf(newId), message);
			    return Long.valueOf(newId);
		})).longValue();
			    }

	    	    @Override public void delete(MessageMetadata message)
	    {
		requireNonNull(message, "message can't be null");
		if (message.getId() < 0)
		    throw new IllegalArgumentException("A message can't has negative ID");
		runner.run(new FutureTask<>( () -> messagesMap.remove(Long.valueOf(message.getId())) ));
	    }

	    	        @Override public List<MessageMetadata> getAll()
	    {
				return runner.run(new FutureTask<>( () -> {
					    return messagesMap.entrySet().stream()
					    .map( e -> e.getValue() )
					    .toList();
		}));
			    }

	    	    	        @Override public List<MessageMetadata> getByFolderId(int folderId)
	    {
				return runner.run(new FutureTask<>( () -> {
					    return messagesMap.entrySet().stream()
					    .map( e -> e.getValue() )
					    .toList();
		}));
			    }

	    @Override public void update(MessageMetadata message)
	    {
		requireNonNull(message, "message can't be null");
		if (message.getId() < 0)
		    throw new IllegalArgumentException("A message can't have negative ID");
runner.run(new FutureTask<Object>( () -> {
					    messagesMap.put(Long.valueOf(message.getId()), message);
}, null));
    }

void deleteAllFolders()
    {

    }

void deleteAllAccounts()
    {
}
	};
	}

    public void setPriority(Priority priority)
    {
	this.priority = requireNonNull(priority, "priority can't be null");
	this.runner = new Runner(queues, priority);
    }
}
