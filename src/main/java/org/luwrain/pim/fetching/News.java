/*
   Copyright 2012-2022 Michael Pozhidaev <msp@luwrain.org>
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

public final class News extends Base
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
	final NewsGroup[] groups;
	groups = storing.getGroups().load();
	if (groups == null || groups.length < 1)
	{
	    message(strings.noNewsGroups());
	    return;
	}
	for(NewsGroup g: groups)
	{
	    removeOldArticles(g);
	    checkInterrupted();
	    if (!fetchGroup(g))
		return;
	    checkInterrupted();
	}
    }

    boolean 		fetchGroup(NewsGroup group) throws InterruptedException
    {
	final List<NewsArticle> freshNews = new ArrayList<>();
	int totalCount = 0;
	final List<String> urls = group.getUrls();
	if (urls != null)
	    for (String url: urls)
		if (url != null && !url.trim().isEmpty())
		{
		    checkInterrupted();
		    NewsArticle[] articles = null;
		    try {
			articles = FeedUtils.readFeed(new URL(url.trim()));
		    }
		    catch(PimException  | MalformedURLException e)
		    {
			message(strings.newsFetchingError(group.getName()) + ":" + e.getMessage());
			return true;
		    }
		    totalCount += articles.length;
		    for(NewsArticle a: articles)
			if (storing.getArticles().countByUriInGroup(group, a.getUri()) == 0)
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

    void removeOldArticles(NewsGroup group)
    {
	final NewsArticle[] articles = storing.getArticles().load(group);
	for(NewsArticle a: articles)
	if (isOldArticle(a))
	    storing.getArticles().delete(group, a);
    }

    boolean isOldArticle(NewsArticle article)
    {

	final Calendar cal = Calendar.getInstance();
cal.add(Calendar.MONTH, -1);
final Date result = cal.getTime();
return (article.getPublishedDate().compareTo(result) < 0);
    }
}
