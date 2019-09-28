
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
	try {
	final PreparedStatement st = con.prepareStatement(
"UPDATE contact SET title=? WHERE id=?"
);
	st.setString(1, title);
	st.setLong(2, id);
	if (st.executeUpdate() != 1)
	    throw new PimException("Unable to update the title in contact table");
	super.setTitle(title);
	}
	catch(SQLException e)
	{
	    throw new PimException(e);
	}
    }

    @Override public void setValues(ContactValue[] values) throws PimException
    {
	NullCheck.notNull(values, "values");
	for(int i = 0;i < values.length;++i)
	{
	    NullCheck.notNull(values[i], "values[" + i + "]");
	    NullCheck.notNull(values[i].value, "values[" + i + "].value");
	}
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

    private void saveValues() throws PimException
    {
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
		st.setInt(2, v.type);
		st.setString(3, v.value);
		st.setBoolean(4, v.preferable);
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
