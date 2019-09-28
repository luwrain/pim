
package org.luwrain.pim.contacts;

import org.luwrain.core.*;
import org.luwrain.pim.*;

public class Contact
{
    private String title = "";
    private ContactValue[] values = new ContactValue[0];
    private String[] tags = new String[0]; 
    private String[] uniRefs = new String[0];
    private String notes = "";

    public void setTitle(String title) throws PimException
    {
	NullCheck.notNull(title, "title");
	this.title = title;
    }

    public String getTitle() throws PimException
    {
	return this.title;
    }

    public void setValues(ContactValue[] values) throws PimException
    {
	NullCheck.notNullItems(values, "values");
	this.values = values.clone();
    }

    public ContactValue[] getValues() throws PimException
    {
	return this.values.clone();
    }

    public void setTags(String[] tags) throws PimException
    {
	NullCheck.notNullItems(tags, "tags");
	this.tags = tags.clone();
    }

    public String[] getTags() throws PimException
    {
	return this.tags.clone();
    }

    public void setUniRefs(String[] uniRefs) throws PimException
    {
	NullCheck.notNullItems(uniRefs, "uniRefs");
	this.uniRefs = uniRefs.clone();
    }

    public String[] getUniRefs() throws PimException
    {
	return this.uniRefs.clone();
    }

    public void setNotes(String notes) throws PimException
    {
	NullCheck.notNull(notes, "notes");
	this.notes = notes;
    }

    public String getNotes() throws PimException
    {
	return this.notes;
    }

    @Override public String toString()
    {
	return title != null?title:"";
    }
}
