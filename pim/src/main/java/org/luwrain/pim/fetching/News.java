// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>
// Copyright 2015 Roman Volovodov <gr.rPman@gmail.com>

package org.luwrain.pim.fetching;

import java.net.*;
import java.util.*;
import org.apache.logging.log4j.*;

import org.luwrain.core.*;
import org.luwrain.core.events.SystemEvent;
import org.luwrain.pim.*;
import org.luwrain.pim.news.persist.*;

public final class News extends Base
{
    static private final Logger log = LogManager.getLogger();
    
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
	    log.error("No news persistence");
	    message(strings.noNewsGroupsData());
	    return;
	}
	final var groups = persist.getGroupDAO().load();
	if (groups.isEmpty())
	{
	    log.trace("No news group");
	    message(strings.noNewsGroups());
	    return;
	}
	for(final var g: groups)
	{
	    log.trace("Fetching news from ", g.toString());
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
		if (url != null && !url.isBlank())
		{
		    log.trace("Getting news from {}", url);
		    checkInterrupted();
		    final List<Article> articles;
		    try {
			articles = FeedUtils.readFeed(new URL(url.trim()));
		    }
		    catch(PimException  | MalformedURLException e)
		    {
			log.error("Unable to get news from {}", url, e);
			message(strings.newsFetchingError(group.getName()) + ":" + e.getMessage());
			return true;
		    }
		    log.trace("Fetched {} articles", articles.size());
		    totalCount += articles.size();
		    for(final var a: articles)
		    {
			final int freshCount = persist.getArticleDAO().countByUriInGroup(group, a.getUri());
			log.trace("Fresh count {} for {}", freshCount, a.getUri());
			if (freshCount == 0)
			    freshNews.add(a);
		    }
		}
	log.trace("{} fresh articles", freshNews.size());
	for(final var a: freshNews)
	{
	    a.setStatus(Article.Status.NEW);
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
