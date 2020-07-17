/*
   Copyright 2012-2019 Michael Pozhidaev <msp@luwrain.org>
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

package org.luwrain.pim.fetching;

import java.net.*;
import java.util.*;

import org.luwrain.core.*;
import org.luwrain.core.events.SystemEvent;
import org.luwrain.pim.*;
import org.luwrain.pim.news.*;

public class News extends Base
{
    private final NewsStoring storing;
    
    public News(Control control, Strings strings)
    {
	super(control, strings);
	this.storing = org.luwrain.pim.Connections.getNewsStoring(control.luwrain(), false);
    }

    public void fetch() throws InterruptedException
    {
	if (storing == null)
	{
message(strings.noNewsGroupsData());
	    return;
	}
	final StoredNewsGroup[] groups;
	try {
	    groups = storing.getGroups().load();
	}
	catch (PimException e)
	{
crash(e);
	    return;
	}
	if (groups == null || groups.length < 1)
	{
message(strings.noNewsGroups());
	    return;
	}
	for(StoredNewsGroup g: groups)
	{
	    if (!fetchGroup(g))
		return;
checkInterrupted();
	}
    }

    protected boolean 		fetchGroup(StoredNewsGroup group) throws InterruptedException
    {
	try {
	    final List<NewsArticle> freshNews = new LinkedList();
	    int totalCount = 0;
	    final String[] urls = group.getUrls();
	    for (int k = 0;k < urls.length;k++)
	    {
checkInterrupted();
		NewsArticle[] articles = null;
		try {
		    articles = FeedUtils.readFeed(new URL(urls[k]));
		}
		catch(PimException  | MalformedURLException e)
		{
message(strings.newsFetchingError(group.getName()) + ":" + e.getMessage());
		    return true;
		}
		totalCount += articles.length;
		for(NewsArticle a: articles)
		    if (storing.getArticles().countByUriInGroup(group, a.uri) == 0)
			freshNews.add(a);
	    }
	    for(NewsArticle a: freshNews)
	    {
storing.getArticles().save(group, a);
checkInterrupted();
	    }
	    if (freshNews.size() > 0 )
message(group.getName() + ": " + freshNews.size() + "/" + totalCount);
luwrain.sendBroadcastEvent(new SystemEvent(SystemEvent.Type.BROADCAST, SystemEvent.Code.REFRESH, "", "newsgroup:"));
	    return true;
	}
	catch(PimException e)
	{
crash(e);
	    return false;
	}
    }
}
