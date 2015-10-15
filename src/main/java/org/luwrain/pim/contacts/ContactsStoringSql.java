/*
   Copyright 2012-2015 Michael Pozhidaev <michael.pozhidaev@gmail.com>
   Copyright 2015 Roman Volovodov <gr.rPman@gmail.com>

   This file is part of the LUWRAIN.

   LUWRAIN is free software; you can redistribute it and/or
   modify it under the terms of the GNU General Public
   License as published by the Free Software Foundation; either
   version 3 of the License, or (at your option) any later version.

   LUWRAIN is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
   General Public License for more details.
*/

package org.luwrain.pim.contacts;

import java.util.*;
import java.sql.*;

import org.luwrain.core.Registry;
import org.luwrain.core.NullCheck;

class ContactsStoringSql extends ContactsStoringRegistry
{
    static final int TAG_TYPE = -1;
    static final int UNIREF_TYPE = -2;

    static private class ValueCollecting
    {
	long id;
	final LinkedList<ContactValue> values = new LinkedList<ContactValue>();
	final LinkedList<String> tags = new LinkedList<String>();
	final LinkedList<String> uniRefs = new LinkedList<String>();

	ValueCollecting(long id)
	{
	    this.id = id;
	}
    }

    private Connection con;

    ContactsStoringSql(Registry registry, Connection con)
    {
	super(registry);
	this.con = con;
	if (con == null)
	    throw new NullPointerException("con may not be null");
    }

    @Override public StoredContact[] loadContacts(StoredContactsFolder folder) throws Exception
    {
	NullCheck.notNull(folder, "folder");
	if (!(folder instanceof StoredContactsFolderRegistry))
	    throw new IllegalArgumentException("folder must be an instance of StoredContactsFolderRegistry");
	final StoredContactsFolderRegistry folderRegistry = (StoredContactsFolderRegistry)folder;
	final PreparedStatement st = con.prepareStatement(
							  "SELECT * FROM contact WHERE contacts_folder_id=?"
							  );
	st.setInt(1, folderRegistry.id);
	final LinkedList<StoredContactSql> contacts = new LinkedList<StoredContactSql>();
	final TreeMap<Long, ValueCollecting> valueCollecting = new TreeMap<Long, ValueCollecting>();
	ResultSet rs = st.executeQuery();
	while(rs.next())
	{
	    final StoredContactSql c = new StoredContactSql(con);
	    c.id = rs.getLong(1);
	    c.parentId = rs.getInt(2);
	    c.title = rs.getString(3);
	    c.notes = rs.getString(4);
	    contacts.add(c);
	    valueCollecting.put(new Long(c.id), new ValueCollecting(c.id));
	}
	final Statement st2 = con.createStatement();
	rs = st2.executeQuery(
			      "SELECT* FROM contact_value"
			      );
	while (rs.next())
	{
	    final long contactId = rs.getLong(2);
	    if (!valueCollecting.containsKey(new Long(contactId)))
		continue;
	    final int typeId = rs.getInt(3);
	    final String value = rs.getString(4);
	    final boolean preferable = rs.getBoolean(5);
	    final ValueCollecting c = valueCollecting.get(new Long(contactId));
	    switch(typeId)
	    {
	    case TAG_TYPE:
		c.tags.add(value);
		break;
	    case UNIREF_TYPE:
		c.uniRefs.add(value);
		break;
	    default:
		c.values.add(new ContactValue(typeId, value, preferable));
	    }
	}
	for(StoredContactSql contact: contacts)
	{
	    if (!valueCollecting.containsKey(new Long(contact.id)))//Should never happen
		continue;
	    final ValueCollecting v = valueCollecting.get(new Long(contact.id));
	    contact.values = v.values.toArray(new ContactValue[v.values.size()]);
	    contact.tags = v.tags.toArray(new String[v.tags.size()]);
	    contact.uniRefs = v.uniRefs.toArray(new String[v.uniRefs.size()]);
	}
	return contacts.toArray(new StoredContactSql[contacts.size()]);
    }

    @Override public void saveContact(StoredContactsFolder folder, Contact contact) throws Exception
    {
	NullCheck.notNull(folder, "folder");
	NullCheck.notNull(contact, "contact");
	if (!(folder instanceof StoredContactsFolderRegistry))
	    throw new IllegalArgumentException("folder must be an instance of StoredContactsFolderRegistry");
	final StoredContactsFolderRegistry folderRegistry = (StoredContactsFolderRegistry)folder;
    	PreparedStatement st = con.prepareStatement(
						    "INSERT INTO contact (contacts_folder_id,title,notes) VALUES (?,?,?)",
						    Statement.RETURN_GENERATED_KEYS);
	st.setLong(1, folderRegistry.id);
	st.setString(2, contact.title);
	st.setString(3, contact.notes);
	if (st.executeUpdate() != 1)
	    throw new Exception("Unable to execute initial INSERT query");
	final ResultSet generatedKeys = st.getGeneratedKeys();
	if (!generatedKeys.next()) 
	    throw new Exception("No autogenerated key");
	final long generatedKey = generatedKeys.getLong(1);
	if (contact.values != null)
	    for(ContactValue value: contact.values)
	    {
		st = con.prepareStatement(
					  "INSERT INTO contact_value (contact_id,type_id,value,preferable) VALUES (?,?,?,?)"
					  );
		st.setLong(1, generatedKey);
		st.setInt(2, value.type);
		st.setString(3, value.value);
		st.setBoolean(4, value.preferable);
		st.executeUpdate();
	    }
	if (contact.tags != null)
	    for(String tag: contact.tags)
	    {
		st = con.prepareStatement(
					  "INSERT INTO contact_value (contact_id,type_id,value,preferable) VALUES (?,?,?,?)"
					  );
		st.setLong(1, generatedKey);
		st.setInt(2, TAG_TYPE);
		st.setString(3, tag);
		st.setBoolean(4, false);
		st.executeUpdate();
	    }
	if (contact.uniRefs != null)
	    for(String uniRef: contact.uniRefs)
	    {
		st = con.prepareStatement(
					  "INSERT INTO contact_value (contact_id,type_id,value,preferable) VALUES (?,?,?,?)"
					  );
		st.setLong(1, generatedKey);
		st.setInt(2, UNIREF_TYPE);
		st.setString(3, uniRef);
		st.setBoolean(4, false);
		st.executeUpdate();
	    }
    }

    @Override public void deleteContact(StoredContact contact) throws Exception
    {
	NullCheck.notNull(contact, "contact");
	if (!(contact instanceof StoredContactSql))
	    throw new IllegalArgumentException("contact must be an instance of StoredContactSql");
	final StoredContactSql contactSql = (StoredContactSql)contact;
	PreparedStatement st = con.prepareStatement(
						    "DELETE from contact_value WHERE contact_id=?"
						    );
	st.setLong(1, contactSql.id);
	st.executeUpdate();
	st = con.prepareStatement(
				  "DELETE FROM contact WHERE id=?"
				  );
	st.setLong(1, contactSql.id);
	st.executeUpdate();
    }
}
