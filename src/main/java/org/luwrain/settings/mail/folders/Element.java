
package org.luwrain.settings.mail.folders;

import org.luwrain.core.*;
import org.luwrain.cpanel.*;

public class Element implements org.luwrain.cpanel.Element
{
    public final org.luwrain.cpanel.Element parent;
    public final int id;
    public final String title;

    Element(org.luwrain.cpanel.Element parent, int id, String title)
    {
	NullCheck.notNull(parent, "parent");
	NullCheck.notNull(title, "title");
	this.parent = parent;
	this.id = id;
	this.title = title;
    }

    @Override public org.luwrain.cpanel.Element getParentElement()
    {
	return parent;
    }

    @Override public boolean equals(Object o)
    {
	if (o == null || !(o instanceof Element))
	    return false;
	return id == ((Element)o).id;
    }

    @Override public int hashCode()
    {
	return (int)id;
    }
}
