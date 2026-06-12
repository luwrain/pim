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
    private final MVMap<String, Long> keysMap;

    public ContactsPersistence(ExecQueues queues,
			       MVMap<Long, Contact> contactsMap,
			       MVMap<String, Long> keysMap)
    {
	this.queues = requireNonNull(queues, "queues can't be null");
	this.contactsMap = requireNonNull(contactsMap, "contactsMap can't be null");
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
		log.trace("Deleting " + contact);
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
		log.trace("Updating " + contact);
		runner.run(() -> {
		    contactsMap.put(Long.valueOf(contact.getId()), contact);
		    return null;
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
