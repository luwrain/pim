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
import lombok.*;

import org.luwrain.core.*;
import org.luwrain.pim.*;

@Data
@NoArgsConstructor
public class NewsArticle implements Comparable
{
    public static final int NEW = 0;
    public static final int READ = 1;
    public static final int MARKED = 2;

    private int state = NEW;
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

    public void save()
    {
	throw new RuntimeException("You shouldn't call NewsArticle.save() directly");
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
	return this.title != null?title.trim():"";
    }
}
