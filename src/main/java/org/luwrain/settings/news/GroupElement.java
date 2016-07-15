
package org.luwrain.settings.news;

import org.luwrain.core.*;
import org.luwrain.cpanel.*;
import org.luwrain.pim.news.*;

class GroupElement implements Element
{
    private Element parent;
    private long id;
    private String title;

    GroupElement(Element parent, long id, String title)
    {
	NullCheck.notNull(parent, "parent");
	NullCheck.notNull(title, "title");
	this.parent = parent;
	this.id = id;
	this.title = title;
    }

    @Override public Element getParentElement()
    {
	return parent;
    }

    @Override public boolean equals(Object o)
    {
	if (o == null || !(o instanceof GroupElement))
	    return false;
	return id == ((GroupElement)o).id;
    }

    @Override public int hashCode()
    {
	return (int)id;
    }

    long id() {return id;}
    String title() {return title;}
}
