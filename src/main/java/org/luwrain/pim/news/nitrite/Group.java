/*
   Copyright 2012-2021 Michael Pozhidaev <michael.pozhidaev@gmail.com>
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

package org.luwrain.pim.news.nitrite;

import java.util.*;

import org.luwrain.core.*;
import org.luwrain.pim.*;
import org.luwrain.pim.news.*;

final class Group extends NewsGroup
{
    int id = 0;
    transient Groups groups = null;

    @Override public void setName(String value)
    {
	synchronized(groups) {
	    super.setName(value);
	}
    }

    @Override public void setUrls(List<String> value)
    {
	synchronized(groups) {
	    super.setUrls(value);
	}
    }

    @Override public void setMediaContentType(String value)
    {
	synchronized(groups) {
	    super.setMediaContentType(value);
	}
    }

    @Override public void setOrderIndex(int value)
    {
	synchronized(groups) {
	    super.setOrderIndex(value);
	}
    }

    @Override public void setExpireAfterDays(int value)
    {
	synchronized(groups) {
	    super.setExpireAfterDays(value);
	}
    }

    @Override public void save()
    {
	groups.save();
    }

    @Override public boolean equals(Object o)
    {
	if (o == null || !(o instanceof Group))
	    return false;
	return id == ((Group)o).id;
    }
}
