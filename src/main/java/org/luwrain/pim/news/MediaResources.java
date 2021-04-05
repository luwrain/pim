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

package org.luwrain.pim.news;

import java.util.*;

import org.luwrain.core.*;

public final class MediaResources
{
    private Properties props;

    public MediaResources(Properties props)
    {
	NullCheck.notNull(props, "props");
	this.props = props;
    }

    public void save(MediaResource[] resources)
    {
	NullCheck.notNullItems(resources, "resources");
	props.setProperty("media.count", "" + resources.length);
	for(int i = 0;i < resources.length;++i)
	{
	    props.setProperty("media." + i + ".url", resources[i].getUrl());
	}
    }

    public MediaResource[] load()
    {
	final String countStr = props.getProperty("media.count");
	if (countStr == null || countStr.isEmpty())
	    return new MediaResource[0];
	final int count;
	try {
	    count = Integer.parseInt(countStr);
	}
	catch(NumberFormatException e)
	{
	    return new MediaResource[0];
	}
	final List<MediaResource> res = new LinkedList();
	for(int i = 0;i < count;++i)
	{
	    final String url = props.getProperty("media." + i + ".url");
	    if (url == null)
		return new MediaResource[0];
	    final MediaResource resource = new MediaResource();
	    resource.setUrl(url);
	    res.add(resource);
	}
	return res.toArray(new MediaResource[res.size()]);
    }
}
