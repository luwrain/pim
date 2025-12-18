// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>
// Copyright 2015 Roman Volovodov <gr.rPman@gmail.com>

package org.luwrain.pim.fetching;

import java.net.*;
import java.util.*;

import org.luwrain.core.*;
import org.luwrain.core.events.SystemEvent;
import org.luwrain.pim.*;
import org.luwrain.pim.news.persist.*;

public final class News extends Base
{
    private final NewsPersistence persist;

    public News(Control control, Strings strings)
    {
	super(control, strings);
	this.persist = control.luwrain().createInstance(NewsPersistence.class);
    }

    public void fetch() throws InterruptedException
    {
	if (persist == null)
	{
	    message(strings.noNewsGroupsData());
	    return;
	}
	final var groups = persist.getGroupDAO().load();
	if (groups.isEmpty())
	{
	    message(strings.noNewsGroups());
	    return;
	}
	for(final var g: groups)
	{
	    removeOldArticles(g);
	    checkInterrupted();
	    if (!fetchGroup(g))
		return;
	    checkInterrupted();
	}
    }

    boolean 		fetchGroup(Group group) throws InterruptedException
    {
	final var freshNews = new ArrayList<Article>();
	int totalCount = 0;
	final var urls = group.getUrls();
	if (urls != null)
	    for (String url: urls)
		if (url != null && !url.trim().isEmpty())
		{
		    checkInterrupted();
		    final List<Article> articles;
		    try {
			articles = FeedUtils.readFeed(new URL(url.trim()));
		    }
		    catch(PimException  | MalformedURLException e)
		    {
			message(strings.newsFetchingError(group.getName()) + ":" + e.getMessage());
			return true;
		    }
		    totalCount += articles.size();
		    for(final var a: articles)
			if (persist.getArticleDAO().countByUriInGroup(group, a.getUri()) == 0)
			    freshNews.add(a);
		}
	for(final var a: freshNews)
	{
	    persist.getArticleDAO().add(group, a);
	    checkInterrupted();
	}
	if (freshNews.size() > 0 )
	    message(group.getName() + ": " + freshNews.size() + "/" + totalCount);
	luwrain.sendBroadcastEvent(new SystemEvent(SystemEvent.Type.BROADCAST, SystemEvent.Code.REFRESH, "", "newsgroup:"));
	return true;
    }

    void removeOldArticles(Group group)
    {
	final var articles = persist.getArticleDAO().load(group);
	for(final var a: articles)
	if (isOldArticle(a))
	    persist.getArticleDAO().delete(group, a);
    }

    boolean isOldArticle(Article article)
    {
	final Calendar cal = Calendar.getInstance();
cal.add(Calendar.MONTH, -1);
final Date result = cal.getTime();
return article.getPublishedTimestamp() < result.getTime();
    }
}
