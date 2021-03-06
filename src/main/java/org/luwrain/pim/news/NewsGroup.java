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

public class NewsGroup implements Comparable
{
    public String name ="";
    public String[] urls = new String[0];
    public String mediaContentType = "";
    public int orderIndex = 0;
    public int expireAfterDays = 30;

    @Override public String toString()
    {
	return name;
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
