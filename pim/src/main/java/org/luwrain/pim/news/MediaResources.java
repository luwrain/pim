// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>
// Copyright 2015 Roman Volovodov <gr.rPman@gmail.com>

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
	final List<MediaResource> res = new ArrayList<>();
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
