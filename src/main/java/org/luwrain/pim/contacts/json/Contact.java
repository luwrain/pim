/*
   Copyright 2012-2021 Michael Pozhidaev <msp@luwrain.org>
   Copyright 2015 Roman Volovodov <gr.rPman@gmail.com>

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
}
