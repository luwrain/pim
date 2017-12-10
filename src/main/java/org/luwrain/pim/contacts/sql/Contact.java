
package org.luwrain.pim.contacts.sql;

import java.sql.*;

import org.luwrain.core.*;
import org.luwrain.pim.*;
import org.luwrain.pim.contacts.*;

class Contact extends org.luwrain.pim.contacts.Contact implements  StoredContact
{
    private final Connection con;
    final long id;
    int parentId = 0;

    Contact(Connection con, long id)
    {
		NullCheck.notNull(con, "con");
		if (id < 0)
		    throw new IllegalArgumentException("id (" + id + ") may not be negative");
	this.con = con;
	this.id = id;
    }

    @Override public String getTitle()
    {
	return title;
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

    @Override public ContactValue[] getValues()
    {
	return values;
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

    @Override public String[] getTags()
    {
	return tags;
    }

    @Override public void setTags(String[] value) throws PimException
    {
	NullCheck.notNullItems(value, "value");
	tags = value;
	saveValues();
    }

    @Override public String[] getUniRefs()
    {
	return uniRefs;
    }

    @Override public void setUniRefs(String[] value) throws PimException
    {
	NullCheck.notNullItems(value, "value");
	uniRefs = value;
	saveValues();
    }

    @Override public String getNotes()
    {
	return notes;
    }

    @Override public void setNotes(String value) throws PimException
    {
	NullCheck.notNull(value, "value");
	try {
	    final PreparedStatement st = con.prepareStatement(
							      "UPDATE contact SET notes=? WHERE id=?"
							      );
	    st.setString(1, value);
	    st.setLong(2, id);
	    if (st.executeUpdate() != 1)
		throw new PimException("Unable to update the notes in contact table");
	    notes = value;
	}
	catch(SQLException e)
	{
	    throw new PimException(e);
	}
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
		    st.setInt(2, Contacts.TAG_TYPE);
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
