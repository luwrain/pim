
package org.luwrain.settings.news;

import org.luwrain.core.*;
import org.luwrain.cpanel.*;
import org.luwrain.pim.news.*;

class GroupElement implements Element
{
    private Element parent;
    private StoredNewsGroup group;
    private long id;

    GroupElement(Element parent, StoredNewsGroup group)
    {
	NullCheck.notNull(parent, "parent");
	NullCheck.notNull(group, "group");
	this.parent = parent;
	this.group = group;
	this.id = group.getId();
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

    StoredNewsGroup getGroup() { return group; }
}
