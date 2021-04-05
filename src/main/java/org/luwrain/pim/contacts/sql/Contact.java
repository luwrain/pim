/*
   Copyright 2012-2021 Michael Pozhidaev <msp@luwrain.org>
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

import java.sql.*;

import org.luwrain.core.*;
import org.luwrain.pim.*;
import org.luwrain.pim.contacts.*;

final class Contact extends org.luwrain.pim.contacts.Contact
{
    private final Connection con;
    final long id;
    int parentId = 0;
    private boolean committed = false;

    Contact(Connection con, long id)
    {
	NullCheck.notNull(con, "con");
	if (id < 0)
	    throw new IllegalArgumentException("id (" + id + ") may not be negative");
	this.con = con;
	this.id = id;
    }

    @Override public void setTitle(String title) throws PimException
    {
	NullCheck.notNull(title, "title");
	if (!committed)
	try {
	final PreparedStatement st = con.prepareStatement(
"UPDATE contact SET title=? WHERE id=?"
);
	st.setString(1, title);
	st.setLong(2, id);
	if (st.executeUpdate() != 1)
	    throw new PimException("Unable to update the title in contact table");
	}
	catch(SQLException e)
	{
	    throw new PimException(e);
	}
		super.setTitle(title);
    }

    @Override public void setValues(ContactValue[] values) throws PimException
    {
	NullCheck.notNullItems(values, "values");
	super.setValues(values);
	saveValues();
    }

    @Override public void setTags(String[] tags) throws PimException
    {
	NullCheck.notNullItems(tags, "tags");
	super.setTags(tags);
	saveValues();
    }

    @Override public void setUniRefs(String[] uniRefs) throws PimException
    {
	NullCheck.notNullItems(uniRefs, "uniRefs");
	super.setUniRefs(uniRefs);
	saveValues();
    }

    @Override public void setNotes(String notes) throws PimException
    {
	NullCheck.notNull(notes, "notes");
	if (!committed)
	try {
	    final PreparedStatement st = con.prepareStatement(
							      "UPDATE contact SET notes=? WHERE id=?"
							      );
	    st.setString(1, notes);
	    st.setLong(2, id);
	    if (st.executeUpdate() != 1)
		throw new PimException("Unable to update the notes in contact table");
	}
	catch(SQLException e)
	{
	    throw new PimException(e);
	}
	super.setNotes(notes);
    }

    void commit()
    {
	this.committed = true;
    }

    private void saveValues() throws PimException
    {
	if (!committed)
	    return;
	try {
	PreparedStatement st = con.prepareStatement(
						    "DELETE FROM contact_value WHERE contact_id=?"
						    );
	st.setLong(1, id);
	st.executeUpdate();
	for(ContactValue v: getValues())
	    {
		st = con.prepareStatement(
					  "INSERT INTO contact_value (contact_id,type_id,value,preferable) VALUES (?,?,?,?)"
					  );
		st.setLong(1, id);
		st.setInt(2, v.getType());
		st.setString(3, v.getValue());
		st.setBoolean(4, v.isPreferable());
		if (st.executeUpdate() != 1)
		    throw new PimException("Unable to save a contact value");
	    }
	for(String s: getTags())
		if (!s.trim().isEmpty())
		{
		    st = con.prepareStatement(
					      "INSERT INTO contact_value (contact_id,type_id,value,preferable) VALUES (?,?,?,?)"
					      );
		    st.setLong(1, id);
		    st.setInt(2, Contacts.TAG_TYPE);
		    st.setString(3, s);
		    st.setBoolean(4, false);
		    if (st.executeUpdate() != 1)
			throw new PimException("Unable to save a contact tag");
		}
	for(String s: getUniRefs())
		if (!s.trim().isEmpty())
		{
		    st = con.prepareStatement(
					      "INSERT INTO contact_value (contact_id,type_id,value,preferable) VALUES (?,?,?,?)"
					      );
		    st.setLong(1, id);
		    st.setInt(2, Contacts.UNIREF_TYPE);
		    st.setString(3, s);
		    st.setBoolean(4, false);
		    if (st.executeUpdate() != 1)
			throw new PimException("Unable to save a contact tag");
		}
	}
	catch(SQLException e)
	{
	    throw new PimException(e);
	}
    }
}
