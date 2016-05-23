
package org.luwrain.util;

import java.net.URL;
import java.util.*;
import java.io.*;
import com.sun.syndication.feed.synd.*;
import com.sun.syndication.io.*;

import org.luwrain.core.NullCheck;
import org.luwrain.pim.news.NewsArticle;
import org.luwrain.pim.PimException;

public class FeedUtils
{
    static public NewsArticle[] readFeed(URL url) throws PimException, InterruptedException
    {
	NullCheck.notNull(url, "url");
	final LinkedList<NewsArticle> articles = new LinkedList<NewsArticle>();
	XmlReader reader = null;
	try {
	    try {
		reader = new XmlReader(url);
		SyndFeed feed = new SyndFeedInput().build(reader);
		for (Iterator i = feed.getEntries().iterator(); i.hasNext();)
		{
		    if (Thread.currentThread().isInterrupted())
			throw new InterruptedException();
		    SyndEntry entry = (SyndEntry) i.next();
		    NewsArticle article = new NewsArticle();
		    if (feed.getTitle() != null)
			//FIXME:		    article.sourceTitle = MlTagStrip.run(feed.getTitle());
			article.sourceTitle = feed.getTitle();
		    if (entry.getTitle() != null)
			article.title = /*MlTagStrip.run(*/entry.getTitle();
		    if (entry.getUri() != null)
			article.uri = entry.getUri();
		    if (entry.getLink() != null)
			article.url = entry.getLink();
		    if (entry.getPublishedDate() != null)
			article.publishedDate = entry.getPublishedDate();
		    if (entry.getUpdatedDate() != null)
			article.updatedDate = entry.getUpdatedDate();
		    if (entry.getAuthor() != null)
			article.author = /*MlTagStrip.run(*/entry.getAuthor();
		    List contents = entry.getContents();
		    if (contents != null)
		    {
			if (contents.size() > 0)
			{
			    for(Object o: contents)
				if (o != null && o instanceof SyndContentImpl)
				{
				    SyndContentImpl content = (SyndContentImpl)o;
				    if (content.getValue() != null)
					article.content += content.getValue();
				}
			} else
			{
			    SyndContent content = entry.getDescription();
			    if (content != null)
				article.content = content.getValue();
			}
		    }
		    articles.add(article);
		}
		if (Thread.currentThread().isInterrupted())
		    throw new InterruptedException();
	    }
	    finally {
		if (reader != null)
		    reader.close();
	    }
	}
	catch(IOException | FeedException e)
	{
	    throw new PimException(e);
	}
	return articles.toArray(new NewsArticle[articles.size()]);
    }
}
