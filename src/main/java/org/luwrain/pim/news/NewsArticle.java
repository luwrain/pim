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

//LWR_API 1.0

package org.luwrain.pim.news;

import java.util.*;

import org.luwrain.core.*;
import org.luwrain.pim.*;

public class NewsArticle implements StoredNewsArticle, Comparable
{
    public static final int NEW = 0;
    public static final int READ = 1;
    public static final int MARKED = 2;

    private int state = 0;
    private String sourceUrl = "";
    private String sourceTitle = "";
    private String uri = "";
    private String title = "";
    private  String extTitle = "";
    private String url = "";
    private String descr = "";
    private String author = "";
    private String categories = "";
    private Date publishedDate = new Date();
    private Date updatedDate = new Date();
    private String content = "";

    public   int getState()
    {
	return state;
    }

    public   void setState(int state)
    {
	this.state = state;
    }

    public   String getSourceUrl()
    {
	return sourceUrl;
    }

    public   void setSourceUrl(String sourceUrl)
    {
	this.sourceUrl = sourceUrl;
    }

    public   String getSourceTitle()
    {
	return sourceTitle;
    }

    public   void setSourceTitle(String sourceTitle)
    {
	this.sourceTitle = sourceTitle;
    }

    public   String getUri()
    {
	return uri;
    }

    public   void setUri(String uri)
    {
	this.uri = uri;
    }

    public   String getTitle()
    {
	return this.title;
    }

    public   void setTitle(String title)
    {
	NullCheck.notNull(title, "title");
	this.title = title;
    }

    public   String getExtTitle()
    {
	return extTitle;
    }

    public   void setExtTitle(String extTitle)
    {
	this.extTitle = extTitle;
    }

    public   String getUrl()
    {
	return url;
    }

    public   void setUrl(String url)
    {
	this.url = url;
    }

    public   String getDescr()
    {
	return descr;
    }

    public   void setDescr(String descr)
    {
	this.descr = descr;
    }

    public   String getAuthor()
    {
	return author;
    }

    public   void setAuthor(String authro)
    {
	this.author = author;
    }

    public   String getCategories()
    {
	return categories;
    }

    public   void setCategories(String categories)
    {
	this.categories = categories;
    }

    public   java.util.Date getPublishedDate()
    {
	return publishedDate;
    }

    public   void setPublishedDate(java.util.Date publishedDate)
    {
	this.publishedDate = publishedDate;
    }

    public   java.util.Date getUpdatedDate()
    {
	return updatedDate;
    }

    public   void setUpdatedDate(java.util.Date updatedDate)
    {
	this.updatedDate = updatedDate;
    }

    public   String getContent()
    {
	return content;
    }

    public   void setContent(String content)
    {
	this.content = content;
    }

    public void save() throws PimException
    {
    }

    public void copyValues(NewsArticle article)
    {
	this.state = article.state;
	this.sourceUrl = article.sourceUrl;
	this.sourceTitle = article.sourceTitle;
	this.uri = article.uri;
	this.title = article.title;
	this.extTitle = article.extTitle;
	this.url = article.url;
	this.descr = article.descr;
	this.author = article.author;
	this.categories = article.categories;
	this.publishedDate = article.publishedDate;
	this.updatedDate = article.updatedDate; 
	this.content = article.content;
    }

    @Override public int compareTo(Object o)
    {
	if (o == null || !(o instanceof NewsArticle))
	    return 0;
	final NewsArticle article = (NewsArticle)o;
	if (state != article.state)
	{
	    if (state > article.state)
		return -1;
	    if (state < article.state)
		return 1;
	    return 0;
	}
	if (publishedDate == null || article.publishedDate == null)
	    return 0;
	return -1 * publishedDate.compareTo(article.publishedDate);
    }

    @Override public String toString()
    {
	return this.title != null?title:"";
    }
}
