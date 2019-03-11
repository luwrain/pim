/*
   Copyright 2012-2019 Michael Pozhidaev <michael.pozhidaev@gmail.com>
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

import java.sql.*;
import java.util.*;

import org.luwrain.core.*;
import org.luwrain.pim.*;
import org.luwrain.pim.news.*;

final class Articles implements NewsArticles
{
    private final Storing storing;
    private final Connection con;

    Articles(Storing storing, Connection con)
    {
	NullCheck.notNull(storing, "storing");
	NullCheck.notNull(con, "con");
	this.storing = storing;
	this.con = con;
    }

    @Override public void save(StoredNewsGroup newsGroup, NewsArticle article) throws PimException
    {
	NullCheck.notNull(newsGroup, "newsGroup");
	final Group g = (Group)newsGroup;
	try {
	    storing.execInQueue(()->{
		    final PreparedStatement st = con.prepareStatement("INSERT INTO news_article (news_group_id,state,source_url,source_title,uri,title,ext_title,url,descr,author,categories,published_date,updated_date,content,ext_data) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);");
		    st.setLong(1, g.id);
		    st.setInt(2, NewsArticle.NEW);
		    st.setString(3, article.sourceUrl);
		    st.setString(4, article.sourceTitle);
		    st.setString(5, article.uri);
		    st.setString(6, article.title);
		    st.setString(7, article.extTitle);
		    st.setString(8, article.url);
		    st.setString(9, article.descr);
		    st.setString(10, article.author);
		    st.setString(11, article.categories);
		    st.setDate(12, new java.sql.Date(article.publishedDate.getTime()));
		    st.setDate(13, new java.sql.Date(article.updatedDate.getTime()));
		    st.setString(14, article.content);
		    st.setString(15, "");//FIXME:
		    st.executeUpdate();
		    return null;
		});
	}
	catch(Exception e)
	{
	    throw new PimException(e);
	}
    }

    @Override public     StoredNewsArticle[] load(StoredNewsGroup newsGroup) throws PimException
    {
	NullCheck.notNull(newsGroup, "newsGroup");
	try {
	    return (StoredNewsArticle[])storing.execInQueue(()->{
		    final Group g = (Group)newsGroup;
		    final PreparedStatement st = con.prepareStatement("SELECT id,news_group_id,state,source_url,source_title,uri,title,ext_title,url,descr,author,categories,published_date,updated_date,content,ext_data FROM news_article WHERE news_group_id = ?;");
		    st.setLong(1, g.id);
		    final ResultSet rs = st.executeQuery();
		    final List<Article> articles = new LinkedList();
		    while (rs.next())
		    {
			final Article a = new Article(con);
			a.id = rs.getLong(1);
			a.groupId = rs.getLong(2);
			a.state = rs.getInt(3);
			a.sourceUrl = rs.getString(4).trim();
			a.sourceTitle = rs.getString(5).trim();
			a.uri = rs.getString(6).trim();
			a.title = rs.getString(7).trim();
			a.extTitle = rs.getString(8).trim();
			a.url = rs.getString(9).trim();
			a.descr = rs.getString(10).trim();
			a.author = rs.getString(11).trim();
			a.categories = rs.getString(12).trim();
			a.publishedDate = new java.util.Date(rs.getTimestamp(13).getTime());
			a.updatedDate = new java.util.Date(rs.getTimestamp(14).getTime());
			a.content = rs.getString(15).trim();
			rs.getString(16);//FIXME:
			articles.add(a);
		    }
		    return articles.toArray(new StoredNewsArticle[articles.size()]);
		});
	}
	catch(Exception e)
	{
	    throw new PimException(e);
	}
    }

    @Override public     StoredNewsArticle[] loadWithoutRead(StoredNewsGroup newsGroup) throws PimException
    {
	NullCheck.notNull(newsGroup, "newsGroup");
	final Group g = (Group)newsGroup;
	try {
	    return (StoredNewsArticle[])storing.execInQueue(()->{
		    final PreparedStatement st = con.prepareStatement("SELECT id,news_group_id,state,source_url,source_title,uri,title,ext_title,url,descr,author,categories,published_date,updated_date,content,ext_data FROM news_article WHERE news_group_id = ? AND state <> 1;");
		    st.setLong(1, g.id);
		    final ResultSet rs = st.executeQuery();
		    final List<Article> articles = new LinkedList();
		    while (rs.next())
		    {
			final Article a = new Article(con);
			a.id = rs.getLong(1);
			a.groupId = rs.getLong(2);
			a.state = rs.getInt(3);
			a.sourceUrl = rs.getString(4).trim();
			a.sourceTitle = rs.getString(5).trim();
			a.uri = rs.getString(6).trim();
			a.title = rs.getString(7).trim();
			a.extTitle = rs.getString(8).trim();
			a.url = rs.getString(9).trim();
			a.descr = rs.getString(10).trim();
			a.author = rs.getString(11).trim();
			a.categories = rs.getString(12).trim();
			a.publishedDate = new java.util.Date(rs.getTimestamp(13).getTime());
			a.updatedDate = new java.util.Date(rs.getTimestamp(14).getTime());
			a.content = rs.getString(15).trim();
			rs.getString(16);//FIXME:
			articles.add(a);
		    }
		    return articles.toArray(new StoredNewsArticle[articles.size()]);
		});
	}
	catch(Exception e)
	{
	    throw new PimException(e);
	}
    }

    @Override public int countByUriInGroup(StoredNewsGroup newsGroup, String uri) throws PimException
    {
	NullCheck.notEmpty(uri, "uri");
	final Group g = (Group)newsGroup;
	try {
	    final Object resValue = storing.execInQueue(()->{
		    final PreparedStatement st = con.prepareStatement("SELECT count(*) FROM news_article WHERE news_group_id = ? AND uri = ?;");
		    st.setLong(1, g.id);
		    st.setString(2, uri);
		    final ResultSet rs = st.executeQuery();
		    if (!rs.next())
			return new Integer(0);
		    return new Integer(rs.getInt(1));
		});
	    if (resValue == null)
		return 0;
	    return ((Integer)resValue).intValue();
	}
	catch(Exception e)
	{
	    throw new PimException(e);
	}
    }

    @Override public int countNewInGroup(StoredNewsGroup group) throws PimException
    {
	NullCheck.notNull(group, "group");
	if (!(group instanceof Group))
	    return 0;
	final Group g = (Group)group;
	try {
	    final Object resValue = storing.execInQueue(()->{
		    final PreparedStatement st = con.prepareStatement("SELECT count(*) FROM news_article WHERE news_group_id=? AND state=?;");
		    st.setLong(1, g.id);
		    st.setLong(2, NewsArticle.NEW);
		    final ResultSet rs = st.executeQuery();
		    if (!rs.next())
			return new Integer(0);
		    return new Integer(rs.getInt(1));
		});
	    if (resValue == null)
		return 0;
	    return ((Integer)resValue).intValue();
	}
	catch(Exception e)
	{
	    throw new PimException(e);
	}
    }

    @Override public int[] countNewInGroups(StoredNewsGroup[] groups) throws PimException
    {
	NullCheck.notNullItems(groups, "groups");
	try {
	    return (int[])storing.execInQueue(()->{
		    final Group[] g = new Group[groups.length];
		    for(int i = 0;i < groups.length;++i)
			g[i] = (Group)groups[i];
		    final int[] res = new int[g.length];
		    for(int i = 0;i < res.length;++i)
			res[i] = 0;
		    final Statement st = con.createStatement();
		    final ResultSet rs = st.executeQuery("select news_group_id,count(*) from news_article where state=0 group by news_group_id;");
		    while (rs.next())
		    {
			final long id = rs.getLong(1);
			final int count = rs.getInt(2);
			int k = 0;
			while (k < g.length && g[k].id != id)
			    ++k;
			if (k < g.length)
			    res[k] = count;
		    }
		    return res;
		});
	}
	catch(Exception e)
	{
	    throw new PimException(e);
	}
    }

    @Override public int[] countMarkedInGroups(StoredNewsGroup[] groups) throws PimException
    {
	NullCheck.notNullItems(groups, "groups");
	try {
	    return (int[])storing.execInQueue(()->{
		    final Group[] g = new Group[groups.length];
		    for(int i =- 0;i < groups.length;++i)
			g[i] = (Group)groups[i];
		    final int[] res = new int[g.length];
		    for(int i = 0;i < res.length;++i)
			res[i] = 0;
		    final Statement st = con.createStatement();
		    final ResultSet rs = st.executeQuery("select news_group_id,count(*) from news_article where state=2 group by news_group_id;");
		    while (rs.next())
		    {
			final long id = rs.getLong(1);
			final int count = rs.getInt(2);
			int k = 0;
			while (k < g.length && g[k].id != id)
			    ++k;
			if (k < g.length)
			    res[k] = count;
		    }
		    return res;
		});
	}
	catch(Exception e)
	{
	    throw new PimException(e.getMessage(), e);
	}
    }

    @Override public Set<String> loadUrisInGroup(StoredNewsGroup group) throws PimException
    {
	NullCheck.notNull(group, "group");
	return null;
    }
}
