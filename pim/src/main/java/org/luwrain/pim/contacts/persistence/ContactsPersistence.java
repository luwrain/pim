// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.pim.contacts.persistence;

import java.util.*;

import org.apache.logging.log4j.*;
import org.h2.mvstore.*;

import org.luwrain.pim.*;

import static java.util.Objects.*;
import static org.luwrain.pim.ExecQueues.*;

public final class ContactsPersistence
{
    static private final Logger log = LogManager.getLogger();

    final ExecQueues queues;
    private Priority priority = Priority.MEDIUM;
    private Runner runner = null;
    private final MVMap<Long, Contact> contactsMap;
    private final MVMap<Long, ContactsFolder> foldersMap;
    private final MVMap<String, Long> keysMap;

    public ContactsPersistence(ExecQueues queues,
			       MVMap<Long, Contact> contactsMap,
			       MVMap<Long, ContactsFolder> foldersMap,
			       MVMap<String, Long> keysMap)
    {
	this.queues = requireNonNull(queues, "queues can't be null");
	this.contactsMap = requireNonNull(contactsMap, "contactsMap can't be null");
	this.foldersMap = requireNonNull(foldersMap, "foldersMap can't be null");
	this.keysMap = requireNonNull(keysMap, "keysMap can't be null");
	this.runner = new Runner(queues, priority);
    }

    public ContactDAO getContactDAO()
    {
	return new ContactDAO(){
	    @Override public long add(Contact contact)
	    {
		requireNonNull(contact, "contact can't be null");
		return runner.run(() -> {
		    final long newId = getNewKey(Contact.class).longValue();
		    contact.setId(newId);
		    log.trace("Adding " + contact);
		    contactsMap.put(Long.valueOf(newId), contact);
		    return Long.valueOf(newId);
		}).longValue();
	    }

	    @Override public void delete(Contact contact)
	    {
		requireNonNull(contact, "contact can't be null");
		if (contact.getId() < 0)
		    throw new IllegalArgumentException("A contact can't have negative ID");
		log.trace("Deleting {}", contact.toString());
		runner.run(() -> contactsMap.remove(Long.valueOf(contact.getId())));
	    }

	    @Override public List<Contact> getAll()
	    {
		return runner.run(() -> contactsMap.entrySet().stream()
				  .map(e -> e.getValue())
				  .toList());
	    }

	    @Override public void update(Contact contact)
	    {
		requireNonNull(contact, "contact can't be null");
		if (contact.getId() < 0)
		    throw new IllegalArgumentException("A contact can't have negative ID");
		log.trace("Updating ", contact.toString());
		runner.run(() -> {
		    contactsMap.put(Long.valueOf(contact.getId()), contact);
		    return null;
		});
	    }

	        @Override public List<Contact> getByFolderId(long folderId)
	    {
		if (folderId < 0)
		    throw new IllegalArgumentException("folderId can't be negative (" + folderId + ")");
		return runner.run(() -> contactsMap.entrySet().stream()
				  .filter(e -> e.getValue().getFolderId() == folderId)
				  .map(e -> e.getValue())
				  .toList());


	    }
	};
    }

    public ContactsFolderDAO getContactsFolderDAO()
    {
	return new ContactsFolderDAO(){
	    @Override public long add(ContactsFolder folder)
	    {
		requireNonNull(folder, "folder can't be null");
		return runner.run(() -> {
		    final long newId = getNewKey(ContactsFolder.class).longValue();
		    folder.setId(newId);
		    log.trace("Adding {}", folder.toString());
		    foldersMap.put(Long.valueOf(newId), folder);
		    return Long.valueOf(newId);
		}).longValue();
	    }

	    @Override public void delete(ContactsFolder folder)
	    {
		requireNonNull(folder, "folder can't be null");
		if (folder.getId() < 0)
		    throw new IllegalArgumentException("A folder can't have negative ID");
		log.trace("Deleting folder {}", folder);
		runner.run(() -> foldersMap.remove(Long.valueOf(folder.getId())));
	    }

	    @Override public List<ContactsFolder> getAll()
	    {
		return runner.run(() -> foldersMap.entrySet().stream()
				  .map(e -> e.getValue())
				  .toList());
	    }

	    @Override public List<ContactsFolder> getChildFolders(ContactsFolder folder)
	    {
		requireNonNull(folder, "folder can't be null");
		if (folder.getId() < 0)
		    throw new IllegalArgumentException("A folder can't have negative ID");
		final long id = folder.getId();
		return runner.run(() -> foldersMap.entrySet().stream()
				  .filter(e -> e.getValue().getParentFolderId() == id)
				  .map(e -> e.getValue())
				  .toList());
	    }

	    @Override public void update(ContactsFolder folder)
	    {
		requireNonNull(folder, "folder can't be null");
		if (folder.getId() < 0)
		    throw new IllegalArgumentException("A folder can't have negative ID");
		log.trace("Updating folder " + folder);
		runner.run(() -> {
		    foldersMap.put(Long.valueOf(folder.getId()), folder);
		    return null;
		});
	    }

	    @Override public ContactsFolder getRoot()
	    {
		return runner.run(() -> {
		    final var res = foldersMap.entrySet().stream()
		    .filter(e -> (e.getValue().getParentFolderId() == e.getValue().getId()))
		    .map(e -> e.getValue())
		    .findFirst();
		    return res.isPresent() ? res.get() : null;
		});
	    }
	};
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
