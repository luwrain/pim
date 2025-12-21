// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>
// Copyright 2015 Roman Volovodov <gr.rPman@gmail.com>

package org.luwrain.pim.mail.persistence;

import java.util.*;
import java.util.concurrent.*;
import org.apache.logging.log4j.*;
import org.h2.mvstore.*;

import org.luwrain.pim.*;

import static java.util.Objects.*;
import static org.luwrain.pim.ExecQueues.*;

public final class MailPersistence
{
    static private final Logger log = LogManager.getLogger();
    
    final ExecQueues queues;
    private Priority priority = Priority.MEDIUM;
    private Runner runner = null;
    private final MVMap<Integer, Account> accountsMap ;
            private final MVMap<Integer, Folder> foldersMap;
        private final MVMap<Long, MessageMetadata> messagesMap;
    private final MVMap<String, Long> keysMap;

    public MailPersistence(ExecQueues queues,
			   MVMap<Integer, Account> accountsMap,
			   MVMap<Integer, Folder> foldersMap,
			   MVMap<Long, MessageMetadata> messagesMap,
			   MVMap<String, Long> keysMap)
    {
	this.queues = requireNonNull(queues, "queues can't be null");
			this.accountsMap = requireNonNull(accountsMap, "accountsMap can't be null");
		this.foldersMap = requireNonNull(foldersMap, "foldersMap can't be null");
	this.messagesMap = requireNonNull(messagesMap, "messagesMap can't be null");
	this.keysMap = requireNonNull(keysMap, "keysMap can't be null");
	this.runner = new Runner(queues, priority);
    }

    public AccountDAO getAccountDAO()
    {
	return new AccountDAO(){
	    @Override public Account getById(int id)
	    {
		if (id < 0)
		    throw new IllegalArgumentException("id can't be negative");
		return runner.run(() -> accountsMap.get(Integer.valueOf(id)) );
	    }

	    @Override public void delete(Account account)
	    {
		requireNonNull(account, "account can't be null");
		if (account.getId() < 0)
		    throw new IllegalArgumentException("An account can't have negative ID");
		log.trace("Deleting " + account);
		runner.run(new FutureTask<>(() -> accountsMap.remove(Integer.valueOf(account.getId())) ));
	    }

	    @Override public int add(Account account)
	    {
		requireNonNull(account, "account can't be null");
		return runner.run(() -> {
			    final int newId = getNewKey(Account.class).intValue();
			    account.setId(newId);
			    log.trace("Adding " + account);
			    accountsMap.put(Integer.valueOf(newId), account);
			    return Integer.valueOf(newId);
		    }).intValue();
	    }

	    @Override public List<Account> getAll()
	    {
		return runner.run(() -> accountsMap.entrySet().stream()
			    .map(e -> e.getValue())
			    .toList() );
	    }

	    @Override public void update(Account account)
	    {
		requireNonNull(account, "account can't be null");
		if (account.getId() < 0)
		    throw new IllegalArgumentException("An account can't have negative ID");
		log.trace("Updating " + account);
		runner.run(new FutureTask<Object>( () ->  accountsMap.put(Integer.valueOf(account.getId()), account), null));
	    }
	};
    }

public FolderDAO getFolderDAO()
    {
	return new FolderDAO(){
	    @Override public int add(Folder folder)
	    {
		requireNonNull(folder, "folder can't be null");
		return runner.run(new FutureTask<>( () -> {
			    final int newId = getNewKey(Folder.class).intValue();
			folder.setId(newId);
			foldersMap.put(Integer.valueOf(newId), folder);
			return Integer.valueOf(newId);
		})).intValue();
			    }

	    	        @Override public List<Folder> getAll()
	    {
		return runner.run(new FutureTask<>( () -> foldersMap.entrySet().stream()
			    .map( e -> e.getValue() )
			    .toList() ));
			    }

	    @Override public List<Folder> getChildFolders(Folder folder)
	    {
		requireNonNull(folder, "folder can't be null");
		if (folder.getId() < 0)
		    throw new IllegalArgumentException("A folder can't have negative ID");
		final int id = folder.getId();
				return runner.run(new FutureTask<>( () -> foldersMap.entrySet().stream()
								    .filter( e -> (e.getValue().getParentFolderId() == id))
			    .map( e -> e.getValue() )
			    .toList() ));
	    }

	    	    @Override public Folder findFirstByProperty(String propName, String propValue)
	    {
		requireNonNull(propName, "propName can't be null");
		requireNonNull(propValue, "propValue can't be null");
		if (propName.isEmpty())
		    throw new IllegalArgumentException("propName can't be empty");
		return runner.run(new FutureTask<Folder>( () -> {
			    final var res = foldersMap.entrySet().stream()
										    .filter( e -> e.getValue().getProperties().getProperty(propName).equals(propValue)) //FIXME: no property
			    .map( e -> e.getValue() )
			    .findFirst();
			    return res.isPresent()?res.get():null;
			    }));
	    }

	    	    @Override public void update(Folder folder)
	    {
		requireNonNull(folder, "folder can't be null");
		if (folder.getId() < 0)
		    throw new IllegalArgumentException("A folder can't have negative ID");
		runner.run(new FutureTask<Object>(() ->  foldersMap.put(Integer.valueOf(folder.getId()), folder), null));
	    }

	    @Override public Folder getRoot()
	    {
				return runner.run(new FutureTask<Folder>( () -> {
			    final var res = foldersMap.entrySet().stream()
			    .filter( e -> (e.getValue().getParentFolderId() ==  e.getValue().getId()))
			    .map( e -> e.getValue() )
			    .findFirst();
			    return res.isPresent()?res.get():null;
			    }));
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
				return runner.run(new FutureTask<>( () -> messagesMap.entrySet().stream()
					    .map( e -> e.getValue() )
					    .toList() ));
			    }

	    	    	        @Override public List<MessageMetadata> getByFolderId(int folderId)
	    {
						return runner.run(new FutureTask<>( () -> messagesMap.entrySet().stream()
										    								    .filter( e -> e.getValue().getFolderId() == folderId )
					    .map( e -> e.getValue() )
					    .toList() ));
									    }

	    @Override public void update(MessageMetadata message)
	    {
		requireNonNull(message, "message can't be null");
		if (message.getId() < 0)
		    throw new IllegalArgumentException("A message can't have negative ID");
		runner.run(new FutureTask<Object>( () -> messagesMap.put(Long.valueOf(message.getId()), message) , null));
	    }
	};
	}

public void deleteAllFolders()
    {
	runner.run(new FutureTask<Object>( () -> foldersMap.clear(), null));
    }

void deleteAllAccounts()
    {
}

    Long getNewKey(Class c)
    {
	final var res = keysMap.get(c.getName());
	if (res == null)
	{
	    keysMap.put(c.getName(), Long.valueOf(0));
	    return Long.valueOf(0);
	}
	final var newVal = Long.valueOf(res.longValue() + 1);
	keysMap.put(c.getName(), newVal);
	return newVal;
    }

    public void setPriority(Priority priority)
    {
	this.priority = requireNonNull(priority, "priority can't be null");
	this.runner = new Runner(queues, priority);
    }
}
