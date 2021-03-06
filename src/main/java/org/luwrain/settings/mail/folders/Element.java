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
