/*
   Copyright 2012-2017 Michael Pozhidaev <michael.pozhidaev@gmail.com>
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

package org.luwrain.pim.news.sql;

import java.util.*;
import java.sql.*;

import org.luwrain.core.*;
import org.luwrain.pim.*;
import org.luwrain.pim.news.*;

class Article implements StoredNewsArticle, Comparable
{
    Connection con;
    long id = 0;
long groupId = 0;

int state = 0;
String sourceUrl = "";
String sourceTitle = "";
String uri = "";
String title = "";
String extTitle = "";
String url = "";
String descr = "";
String author = "";
String categories = "";
java.util.Date publishedDate = new java.util.Date();
java.util.Date updatedDate = new java.util.Date();
String content = "";

Article(Connection con)
    {
	this.con = con;
	if (con == null)
	    throw new NullPointerException("con may not be null");
    }

  @Override public   int getState()
    {
	return state;
    }

    @Override public   void setState(int state) throws PimException
    {
	try {
	    PreparedStatement st = con.prepareStatement("UPDATE news_article SET state = ? WHERE id = ?;");
	    st.setInt(1, state);
	    st.setLong(2, id);
	    st.executeUpdate();
	    this.state = state;
	}
	catch(SQLException e)
	{
	    throw new PimException(e);
	}
    }

    @Override public   String getSourceUrl()
    {
	return sourceUrl;
    }

    @Override public   void setSourceUrl(String sourceUrl) throws PimException
    {
	//FIXME:
    }

    @Override public   String getSourceTitle()
    {
	return sourceTitle;
    }

    @Override public   void setSourceTitle(String sourceTitle) throws PimException
    {
	//FIXME:
    }

    @Override public   String getUri()
    {
	return uri;
    }

    @Override public   void setUri(String uri) throws PimException
    {
	//FIXME:
    }

    @Override public   String getTitle()
    {
	return title;
    }

    @Override public   void setTitle(String title) throws PimException
    {
	//FIXME:
    }

    @Override public   String getExtTitle()
    {
	return extTitle;
    }

    @Override public   void setExtTitle(String extTitle) throws PimException
    {
	//FIXME:
    }

    @Override public   String getUrl()
    {
	return url;
    }

    @Override public   void setUrl(String url) throws PimException
    {
	//FIXME:
    }

    @Override public   String getDescr()
    {
	return descr;
    }

    @Override public   void setDescr(String descr) throws PimException
    {
	//FIXME:
    }

    @Override public   String getAuthor()
    {
	return author;
    }

    @Override public   void setAuthor(String authro) throws PimException
    {
	//FIXME:
    }

    @Override public   String getCategories()
    {
	return categories;
    }

    @Override public   void setCategories(String categories) throws PimException
    {
	//FIXME:
    }

    @Override public   java.util.Date getPublishedDate()
    {
	return publishedDate;
    }

    @Override public   void setPublishedDate(java.util.Date publishedDate) throws PimException
    {
	//FIXME:
    }

    @Override public   java.util.Date getUpdatedDate()
    {
	return updatedDate;
    }

    @Override public   void setUpdatedDate(java.util.Date updatedDate) throws PimException
    {
	//FIXME:
    }

    @Override public   String getContent()
    {
	return content;
    }

    @Override public   void setContent(String content) throws PimException
    {
	//FIXME:
    }

    @Override public int compareTo(Object o)
    {
	if (o == null || !(o instanceof Article))
	    return 0;
	final Article article = (Article)o;
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
}
