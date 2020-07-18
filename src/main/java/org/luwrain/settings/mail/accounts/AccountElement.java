/*
   Copyright 2012-2020 Michael Pozhidaev <msp@luwrain.org>

   This file is part of LUWRAIN.

   LUWRAIN is free software; you can redistribute it and/or
   modify it under the terms of the GNU General Public
   License as published by the Free Software Foundation; either
   version 3 of the License, or (at your option) any later version.

   LUWRAIN is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
   General Public License for more details.
*/

package org.luwrain.settings.mail.accounts;

import org.luwrain.core.*;
import org.luwrain.cpanel.*;

public final class AccountElement implements org.luwrain.cpanel.Element
{
    private final Element parent;
    final int id;
    final String title;

AccountElement(Element parent, int id, String title)
    {
	NullCheck.notNull(parent, "parent");
	NullCheck.notNull(title, "title");
	if (id < 0)
	    throw new IllegalArgumentException("id (" + String.valueOf(id) + ") can't be empty");
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
	if (o == null || !(o instanceof AccountElement))
	    return false;
	return id == ((AccountElement)o).id;
    }

    @Override public int hashCode()
    {
	return (int)id;
    }
}
