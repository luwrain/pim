/*
   Copyright 2012-2021 Michael Pozhidaev <msp@luwrain.org>
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

package org.luwrain.pim.fetching;

import java.net.*;
import java.util.*;
import java.io.*;

//import com.sun.syndication.feed.synd.*;
import com.rometools.rome.feed.synd.*;
import com.rometools.rome.io.*;

import org.luwrain.core.*;
import org.luwrain.util.*;
import org.luwrain.pim.news.NewsArticle;
import org.luwrain.pim.PimException;

public final class FeedUtils
{
    static public NewsArticle[] readFeed(URL url) throws PimException, InterruptedException
    {
	NullCheck.notNull(url, "url");
	final List<NewsArticle> articles = new LinkedList();
	XmlReader reader = null;
	try {
	    URLConnection con = null;
	    InputStream is = null;
	    try {
		con = org.luwrain.util.Connections.connect(url.toURI(), 0);
		is = con.getInputStream();
		reader = new XmlReader(is);
		SyndFeed feed = new SyndFeedInput().build(reader);
		for (Iterator i = feed.getEntries().iterator(); i.hasNext();)
		{
		    if (Thread.currentThread().isInterrupted())
			throw new InterruptedException();
		    final SyndEntry entry = (SyndEntry) i.next();
		    final NewsArticle article = new NewsArticle();
		    /*
		    final List<SyndEnclosure> enclosures = entry.getEnclosures();
		    if (enclosures != null)
			for(SyndEnclosure e: enclosures)
			    Log.debug("podcast", e.getUrl());
		    */
		    if (feed.getTitle() != null)
			//FIXME:		    article.sourceTitle = MlTagStrip.run(feed.getTitle());
			article.setSourceTitle(MlTagStrip.run(feed.getTitle()));
		    if (entry.getTitle() != null)
			article.setTitle(entry.getTitle().replaceAll(" ", " "));
		    if (entry.getUri() != null)
			article.setUri(entry.getUri());
		    if (entry.getLink() != null)
			article.setUrl( entry.getLink());
		    if (entry.getPublishedDate() != null)
			article.setPublishedDate(entry.getPublishedDate());
		    if (entry.getUpdatedDate() != null)
			article.setUpdatedDate(entry.getUpdatedDate());
		    if (entry.getAuthor() != null)
			article.setAuthor(/*MlTagStrip.run(*/entry.getAuthor());
		    if (article.getUri() == null || article.getUri().isEmpty())
			article.setUri(article.getUrl());
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
					article.setContent(article.getContent() + content.getValue());
				}
			} else
			{
			    SyndContent content = entry.getDescription();
			    if (content != null)
				article.setContent(content.getValue());
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
		if (is != null)
		    is.close();
	    }
	}
	catch(IOException | FeedException | URISyntaxException e)
	{
	    throw new PimException(e);
	}
	return articles.toArray(new NewsArticle[articles.size()]);
    }
}
