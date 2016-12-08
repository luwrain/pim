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

import java.sql.*;

import org.luwrain.core.*;
import org.luwrain.pim.*;

class StoredContactSql extends Contact implements  StoredContact
{
    Connection con;

    long id;
    int parentId;

    StoredContactSql(Connection con)
    {
	this.con = con;
	NullCheck.notNull(con, "con");
    }

    @Override public String getTitle() throws PimException
    {
	return title != null?title:"";
    }

    @Override public void setTitle(String value) throws PimException
    {
	NullCheck.notNull(value, "value");
	try {
	final PreparedStatement st = con.prepareStatement(
"UPDATE contact SET title=? WHERE id=?"
);
	st.setString(1, value);
	st.setLong(2, id);
	if (st.executeUpdate() != 1)
	    throw new PimException("Unable to update the title in contact table");
	title = value;
	}
	catch(SQLException e)
	{
	    throw new PimException(e);
	}
    }

    @Override public ContactValue[] getValues() throws PimException
    {
	return values != null?values:new ContactValue[0];
    }

    @Override public void setValues(ContactValue[] values) throws PimException
    {
	NullCheck.notNull(values, "values");
	for(int i = 0;i < values.length;++i)
	{
	    NullCheck.notNull(values[i], "values[" + i + "]");
	    NullCheck.notNull(values[i].value, "values[" + i + "].value");
	}
	this.values = values;
	saveValues();
    }

    @Override public String[] getTags() throws PimException
    {
	return tags != null?tags:new String[0];
    }

    @Override public void setTags(String[] value) throws Exception
    {
	NullCheck.notNullItems(value, "value");
	tags = value;
	saveValues();
    }

    @Override public String[] getUniRefs() throws Exception
    {
	return uniRefs != null?uniRefs:new String[0];
    }

    @Override public void setUniRefs(String[] value) throws Exception
    {
	NullCheck.notNullItems(value, "value");
	uniRefs = value;
	saveValues();
    }

    @Override public String getNotes() throws Exception
    {
	return notes != null?notes:"";
    }

    @Override public void setNotes(String value) throws Exception
    {
	NullCheck.notNull(value, "value");
	final PreparedStatement st = con.prepareStatement(
"UPDATE contact SET notes=? WHERE id=?"
);
	st.setString(1, value);
	st.setLong(2, id);
	if (st.executeUpdate() != 1)
	    throw new Exception("Unable to update the notes in contact table");
	notes = value;
    }

    private void saveValues() throws PimException
    {
	try {
	PreparedStatement st = con.prepareStatement("DELETE FROM contact_value WHERE contact_id=?");
	st.setLong(1, id);
	st.executeUpdate();
	if (values != null)
	    for(ContactValue v: values)
	    {
		st = con.prepareStatement(
					  "INSERT INTO contact_value (contact_id,type_id,value,preferable) VALUES (?,?,?,?)"
					  );
		st.setLong(1, id);
		st.setInt(2, v.type);
		st.setString(3, v.value);
		st.setBoolean(4, v.preferable);
		if (st.executeUpdate() != 1)
		    throw new PimException("Unable to save a contact value");
	    }
	if (tags != null)
	    for(String s: tags)
		if (!s.trim().isEmpty())
		{
		    st = con.prepareStatement(
					      "INSERT INTO contact_value (contact_id,type_id,value,preferable) VALUES (?,?,?,?)"
					      );
		    st.setLong(1, id);
		    st.setInt(2, ContactsStoringSql.TAG_TYPE);
		    st.setString(3, s);
		    st.setBoolean(4, false);
		    if (st.executeUpdate() != 1)
			throw new PimException("Unable to save a contact tag");
		}
	if (uniRefs != null)
	    for(String s: uniRefs)
		if (!s.trim().isEmpty())
		{
		    st = con.prepareStatement(
					      "INSERT INTO contact_value (contact_id,type_id,value,preferable) VALUES (?,?,?,?)"
					      );
		    st.setLong(1, id);
		    st.setInt(2, ContactsStoringSql.UNIREF_TYPE);
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
