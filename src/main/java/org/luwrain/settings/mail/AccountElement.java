/*
   Copyright 2012-2016 Michael Pozhidaev <michael.pozhidaev@gmail.com>

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

package org.luwrain.settings.mail;

import org.luwrain.core.*;
import org.luwrain.cpanel.*;

class AccountElement implements Element
{
    private Element parent;
    private long id;
    private String title;

    AccountElement(Element parent, long id, String title)
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
	if (o == null || !(o instanceof AccountElement))
	    return false;
	return id == ((AccountElement)o).id;
    }

    @Override public int hashCode()
    {
	return (int)id;
    }

    long id() {return id;}
    String title() {return title;}
}
