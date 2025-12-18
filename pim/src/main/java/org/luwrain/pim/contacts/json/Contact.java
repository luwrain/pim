// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.pim.contacts.json;

import org.luwrain.core.*;
import org.luwrain.pim.contacts.*;

final class Contact extends org.luwrain.pim.contacts.Contact
{
    int id = 0;
    private transient Storing storing = null;

    public Contact()
    {
    }

    public Contact(int id)
    {
	this.id = id;
    }

    void setStoring(Storing storing)
    {
	NullCheck.notNull(storing, "storing");
	this.storing = storing;
    }

    @Override public void setTitle(String title)
    {
	NullCheck.notNull(title, "title");
	if (storing == null)
	    throw new IllegalStateException("The Contact object doesn't have a storing");
	super.setTitle(title);
	storing.save();
    }

    @Override public ContactValue[] getValues()
    {
	return super.getValues().clone();
    }

    @Override public void setValues(ContactValue[] values)
    {
	NullCheck.notNullItems(values, "values");
		if (storing == null)
	    throw new IllegalStateException("The Contact object doesn't have a storing");
		super.setValues(values.clone());
		storing.save();
    }

    @Override public String[] getTags()
    {
	return super.getTags().clone();
    }

    @Override public void setTags(String[] tags)
    {
	NullCheck.notNullItems(tags, "tags");
		if (storing == null)
	    throw new IllegalStateException("The Contact object doesn't have a storing");
		super.setTags(tags.clone());
		storing.save();
    }

    @Override public String[] getUniRefs()
    {
	return super.getUniRefs().clone();
    }

    @Override public void setUniRefs(String[] uniRefs)
    {
	NullCheck.notNull(uniRefs, "uniRefs");
		if (storing == null)
	    throw new IllegalStateException("The Contact object doesn't have a storing");
		super.setUniRefs(uniRefs.clone());
		storing.save();
    }

    @Override public void setNotes(String notes)
    {
	NullCheck.notNull(notes, "notes");
			if (storing == null)
	    throw new IllegalStateException("The Contact object doesn't have a storing");
		super.setNotes(notes);
		storing.save();
    }
}
