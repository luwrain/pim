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

package org.luwrain.pim.contacts.sql;

import java.util.*;
import java.sql.*;

import org.luwrain.core.*;
import org.luwrain.pim.*;
import org.luwrain.pim.contacts.*;

final class Contacts implements org.luwrain.pim.contacts.Contacts
{
    static final int TAG_TYPE = -1;
    static final int UNIREF_TYPE = -2;

    private final Connection con;

    Contacts(Connection con)
    {
	NullCheck.notNull(con, "con");
	this.con = con;
    }

    @Override public org.luwrain.pim.contacts.Contact[] load(StoredContactsFolder folder) throws PimException
    {
	NullCheck.notNull(folder, "folder");
	try {
	final Folder folderRegistry = (Folder)folder;
	final PreparedStatement st = con.prepareStatement(
							  "SELECT * FROM contact WHERE contacts_folder_id=?"
							  );
	st.setInt(1, folderRegistry.id);
	final List<Contact> contacts = new LinkedList();
	final Map<Long, ValueCollecting> valueCollecting = new HashMap();
	ResultSet rs = st.executeQuery();
	while(rs.next())
	{
	    final Contact c = new org.luwrain.pim.contacts.sql.Contact(con, rs.getLong(1));
	    c.parentId = rs.getInt(2);
	    c.setTitle(rs.getString(3));
	    c.setNotes(rs.getString(4));
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
	for(Contact contact: contacts)
	{
	    if (!valueCollecting.containsKey(new Long(contact.id)))//Should never happen
		continue;
	    final ValueCollecting v = valueCollecting.get(new Long(contact.id));
	    contact.setValues(v.values.toArray(new ContactValue[v.values.size()]));
	    contact.setTags(v.tags.toArray(new String[v.tags.size()]));
	    contact.setUniRefs(v.uniRefs.toArray(new String[v.uniRefs.size()]));
	    contact.commit();
	}
	return contacts.toArray(new Contact[contacts.size()]);
	}
	catch(SQLException e)
	{
	    throw new PimException(e);
	}
    }

    @Override public void save(StoredContactsFolder folder, org.luwrain.pim.contacts.Contact contact) throws PimException
    {
	NullCheck.notNull(folder, "folder");
	NullCheck.notNull(contact, "contact");
	try {
	final Folder folderRegistry = (Folder)folder;
    	PreparedStatement st = con.prepareStatement(
						    "INSERT INTO contact (contacts_folder_id,title,notes) VALUES (?,?,?)",
						    Statement.RETURN_GENERATED_KEYS);
	st.setLong(1, folderRegistry.id);
	st.setString(2, contact.getTitle());
	st.setString(3, contact.getNotes());
	if (st.executeUpdate() != 1)
	    throw new PimException("Unable to execute initial INSERT query");
	final ResultSet generatedKeys = st.getGeneratedKeys();
	if (!generatedKeys.next()) 
	    throw new PimException("No autogenerated key");
	final long generatedKey = generatedKeys.getLong(1);
	for(ContactValue value: contact.getValues())
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
	for(String tag: contact.getTags())
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
	for(String uniRef: contact.getUniRefs())
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
	catch(SQLException e)
	{
	    throw new PimException(e);
	}
    }

    @Override public void delete(org.luwrain.pim.contacts.Contact contact) throws PimException
    {
	NullCheck.notNull(contact, "contact");
	try {
	final Contact contactSql = (Contact)contact;
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
    catch(SQLException e)
    {
	throw new PimException(e);
    }
    }

        static private class ValueCollecting
    {
	final long id;
	final List<ContactValue> values = new LinkedList();
	final List<String> tags = new LinkedList();
	final List<String> uniRefs = new LinkedList();

	ValueCollecting(long id)
	{
	    this.id = id;
	}
    }
  }
