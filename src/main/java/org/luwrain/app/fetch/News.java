/*
   Copyright 2012-2018 Michael Pozhidaev <michael.pozhidaev@gmail.com>

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

package org.luwrain.app.fetch;

import java.net.*;
import java.util.*;

import org.luwrain.core.*;
import org.luwrain.core.events.EnvironmentEvent;
import org.luwrain.pim.*;
import org.luwrain.pim.news.*;
import org.luwrain.network.*;

class News
{
    private WorkerControl control;
    private Strings strings;

    News(WorkerControl control, Strings strings)
    {
	NullCheck.notNull(control, "control");
	NullCheck.notNull(strings, "strings");
	this.control = control;
	this.strings = strings;
    }

    void work() throws InterruptedException
    {
	final NewsStoring newsStoring = org.luwrain.pim.Connections.getNewsStoring(control.luwrain(), false);
	if (newsStoring == null)
	{
	    control.message(strings.noNewsGroupsData());
	    return;
	}
	final StoredNewsGroup[] groups;
	try {
	    groups = newsStoring.getGroups().load();
	}
	catch (PimException e)
	{
	    control.luwrain().crash(e);
	    return;
	}
	if (groups == null || groups.length < 1)
	{
	    control.message(strings.noNewsGroups());
	    return;
	}
	for(StoredNewsGroup g: groups)
	{
	    if (!fetchNewsGroup(newsStoring, g))
		return;
	    control.checkInterrupted();
	}
    }

    private boolean 		fetchNewsGroup(NewsStoring newsStoring, StoredNewsGroup group) throws InterruptedException
    {
	try {
	    final LinkedList<NewsArticle> freshNews = new LinkedList<NewsArticle>();
	    int totalCount = 0;
	    String[] urls = group.getUrls();
	    for (int k = 0;k < urls.length;k++)
	    {
		control.checkInterrupted();
		NewsArticle[] articles = null;
		try {
		    articles = FeedUtils.readFeed(new URL(urls[k]));
		}
		catch(PimException  | MalformedURLException e)
		{
		    control.message(strings.newsFetchingError(group.getName()) + ":" + e.getMessage());
		    e.printStackTrace();
		    return true;
		}
		totalCount += articles.length;
		for(NewsArticle a: articles)
		    if (newsStoring.getArticles().countByUriInGroup(group, a.uri) == 0)
			freshNews.add(a);
	    }
	    for(NewsArticle a: freshNews)
	    {
		newsStoring.getArticles().save(group, a);
		control.checkInterrupted();
	    }
	    if (freshNews.size() > 0 )
		control.message(group.getName() + ": " + freshNews.size() + "/" + totalCount);
	    control.luwrain().sendBroadcastEvent(new EnvironmentEvent(EnvironmentEvent.Type.BROADCAST, EnvironmentEvent.Code.REFRESH, "", "newsgroup:"));
	    return true;
	}
	catch(PimException e)
	{
	    control.luwrain().crash(e);
	    return false;
	}
    }
}
