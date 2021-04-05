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

package org.luwrain.pim.contacts;

import lombok.*;

import org.luwrain.core.*;
import org.luwrain.pim.*;

@Data
@NoArgsConstructor
public class ContactsFolder implements Comparable
{
    private String title = "";
    private int orderIndex = 0;

    public boolean isRoot()
    {
	return false;
    }

    @Override public String toString()
    {
	return title;
    }

    @Override public int compareTo(Object o)
    {
	if (o == null || !(o instanceof ContactsFolder))
	    return 0;
	final ContactsFolder folder = (ContactsFolder)o;
	if (orderIndex < folder.orderIndex)
	    return -1;
	if (orderIndex > folder.orderIndex)
	    return 1;
	return 0;
    }

    @Override public boolean equals(Object o)
    {
	if (o == null || !(o instanceof ContactsFolder))
	    return false;
	final ContactsFolder folder = (ContactsFolder)o;
	return title.equals(folder.title);
    }
}
