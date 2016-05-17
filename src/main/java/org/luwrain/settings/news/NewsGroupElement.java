
package org.luwrain.settings.news;

import org.luwrain.core.*;
import org.luwrain.cpanel.*;

class NewsGroupElement implements Element
{
    private Element parent;
    private long id;

    NewsGroupElement(Element parent, long id)
    {
	NullCheck.notNull(parent, "parent");
	this.parent = parent;
	this.id = id;
    }

    @Override public Element getParentElement()
    {
	return parent;
    }

    @Override public boolean equals(Object o)
    {
	if (o == null || !(o instanceof NewsGroupElement))
	    return false;
	return id == ((NewsGroupElement)o).id;
    }

    @Override public int hashCode()
    {
	return (int)id;
    }
}
