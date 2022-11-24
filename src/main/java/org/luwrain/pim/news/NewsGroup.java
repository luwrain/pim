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

//LWR_API 1.0

package org.luwrain.pim.news;

import java.util.*;
import lombok.*;

import org.luwrain.core.*;

@Data
@NoArgsConstructor
public class NewsGroup implements Comparable
{
    private String
	name = "",
mediaContentType = "";
    private List<String> urls = new ArrayList<String>();
    private int orderIndex = 0;
    private int expireAfterDays = 30;

    public void copyValues(NewsGroup g)
    {
	this.name = g.name;
	this.urls = new ArrayList<>(g.urls);
	this.mediaContentType = g.mediaContentType;
	this.orderIndex = g.orderIndex;
	this.expireAfterDays = g.expireAfterDays;
    }

    public void save()
    {
	throw new UnsupportedOperationException("You can't call this method directly");
    }

    @Override public String toString()
    {
	return name != null?name.trim():"";
    }

    @Override public int compareTo(Object o)
    {
	if (o == null || !(o instanceof NewsGroup))
	    return 0;
	final NewsGroup g = (NewsGroup)o;
	if (orderIndex < g.orderIndex)
	    return -1;
	if (orderIndex > g.orderIndex)
	    return 1;
	return 0;
    }
}
