
package org.luwrain.settings.mail;

import org.luwrain.core.*;
import org.luwrain.cpanel.*;

class AccountElement implements Element
{
    private Element parent;
    private long id;

    AccountElement(Element parent, long id)
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
	if (o == null || !(o instanceof AccountElement))
	    return false;
	return id == ((AccountElement)o).id;
    }

    @Override public int hashCode()
    {
	return (int)id;
    }
}
