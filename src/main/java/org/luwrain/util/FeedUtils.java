/*
   Copyright 2012-2015 Michael Pozhidaev <michael.pozhidaev@gmail.com>
   Copyright 2015 Roman Volovodov <gr.rPman@gmail.com>

   This file is part of the LUWRAIN.

   LUWRAIN is free software; you can redistribute it and/or
   modify it under the terms of the GNU General Public
   License as published by the Free Software Foundation; either
   version 3 of the License, or (at your option) any later version.

   LUWRAIN is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
   General Public License for more details.
*/

package org.luwrain.util;

import java.net.URL;
import java.util.*;
import java.io.*;
import com.sun.syndication.feed.synd.*;
import com.sun.syndication.io.*;

import org.luwrain.pim.news.NewsArticle;

public class FeedUtils
{
    static public NewsArticle[] readFeed(URL url) throws IOException, FeedException, InterruptedException
    {
	if (url == null)
	    throw new NullPointerException("url may not be null");
	final LinkedList<NewsArticle> articles = new LinkedList<NewsArticle>();
	XmlReader reader = null;
	try {
	    reader = new XmlReader(url);
	    SyndFeed feed = new SyndFeedInput().build(reader);
	    for (Iterator i = feed.getEntries().iterator(); i.hasNext();)
	    {
		if (Thread.currentThread().interrupted())
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
	    if (Thread.currentThread().interrupted())
		throw new InterruptedException();
	}
	finally {
	    if (reader != null)
		reader.close();
	}
	return articles.toArray(new NewsArticle[articles.size()]);
    }
}
