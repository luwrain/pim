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

package org.luwrain.pim.news.nitrite;

import java.util.*;

import org.dizitart.no2.*;
import org.dizitart.no2.objects.*;
import org.dizitart.no2.objects.Cursor;
import static org.dizitart.no2.objects.filters.ObjectFilters.eq;
import static org.dizitart.no2.objects.filters.ObjectFilters.not;
import static org.dizitart.no2.objects.filters.ObjectFilters.and;

import org.luwrain.core.*;
import org.luwrain.pim.*;
import org.luwrain.pim.news.*;

final class Articles implements NewsArticles
{
    private final Storing storing;
    private final ObjectRepository<Article> repo;

    Articles(Storing storing)
    {
	NullCheck.notNull(storing, "storing");
	this.storing = storing;
	//	this.storage = storing.storage;
	//	this.repo = this.db.getRepository(Article.class);
	this.repo = storing.storage.get();
    }

    @Override public void save(NewsGroup newsGroup, NewsArticle article) throws PimException
    {
	NullCheck.notNull(newsGroup, "newsGroup");
	final Group g = (Group)newsGroup;
	try {
	    storing.execInQueue(()->{
		    final Article a = new Article();
		    a.copyValues(article);
		    a.setGroupId(g.id);
		    a.genNewId();
		    this.repo.insert(a);
		    return null;
		});
	}
	catch(Exception e)
	{
	    throw new PimException(e);
	}
    }

        @Override public void delete(NewsGroup newsGroup, NewsArticle article) throws PimException
    {
	NullCheck.notNull(newsGroup, "newsGroup");
	final Group g = (Group)newsGroup;
	try {
	    storing.execInQueue(()->{
		    final Article a = (Article)article;
		    Log.debug("proba", "Deleting " + a.id);
		    this.repo.remove(eq("id", a.id));
		    return null;
		});
	}
	catch(Exception e)
	{
	    throw new PimException(e);
	}
    }

    @Override public     NewsArticle[] load(NewsGroup group)
    {
	final Group g = (Group)group;
	NullCheck.notNull(group, "group");
	try {
	    return (NewsArticle[])storing.execInQueue(()->{
		    final List<Article> res = new LinkedList();
		    final Cursor<Article> c = this.repo.find(eq("groupId", g.id));
		    for(Article a: c)
		    {
			a.setStoring(storing, repo);
			res.add(a);
		    }
		    return res.toArray(new Article[res.size()]);
		});
	}
	catch(Exception e)
	{
	    throw new PimException(e);
	}
    }

    @Override public     NewsArticle[] loadWithoutRead(NewsGroup newsGroup)
    {
	NullCheck.notNull(newsGroup, "newsGroup");
	final Group g = (Group)newsGroup;
	try {
	    return (NewsArticle[])storing.execInQueue(()->{
		    final List<Article> res = new LinkedList();
		    final Cursor<Article> c = this.repo.find(and( eq("groupId", g.id), not(eq("state", NewsArticle.READ))));
		    for(Article a: c)
		    {
			a.setStoring(storing, repo);
			res.add(a);
		    }
		    return res.toArray(new Article[res.size()]);
		});
	}
	catch(Exception e)
	{
	    throw new PimException(e);
	}
    }

    @Override public int countByUriInGroup(NewsGroup newsGroup, String uri)
    {
	NullCheck.notEmpty(uri, "uri");
	final Group g = (Group)newsGroup;
	final Cursor<Article> c = repo.find(and(eq("uri", uri), eq("groupId", g.id)));
	return c.totalCount();
    }

    @Override public int countNewInGroup(NewsGroup group) throws PimException
    {
	NullCheck.notNull(group, "group");
	if (!(group instanceof Group))
	    return 0;
	final Group g = (Group)group;
	try {
	    return (Integer)storing.execInQueue(()->{
		    return repo.find(and(eq("groupId", g.id), eq("state", NewsArticle.NEW))).totalCount();
		});
	}
	catch(Exception e)
	{
	    throw new PimException(e);
	}
    }

    @Override public int[] countNewInGroups(NewsGroup[] groups) throws PimException
    {
	NullCheck.notNullItems(groups, "groups");
	final Group[] g = Arrays.copyOf(groups, groups.length, Group[].class);
	try {
	    return (int[])storing.execInQueue(()->{
		    final int[] res = new int[g.length];
		    Arrays.fill(res, 0);
		    final Cursor<Article> c = this.repo.find(eq("state", NewsArticle.NEW));
		    for(Article a: c)
			for(int i = 0;i < g.length;i++)
			    if (a.getGroupId() == g[i].id)
			    {
				res[i]++;
				break;
			    }
		    return res;
		});
	}
	catch(Exception e)
	{
	    throw new PimException(e);
	}
    }

    @Override public int[] countMarkedInGroups(NewsGroup[] groups)
    {
	NullCheck.notNullItems(groups, "groups");
	final Group[] g = Arrays.copyOf(groups, groups.length, Group[].class);
	try {
	    return (int[])storing.execInQueue(()->{
		    final int[] res = new int[g.length];
		    Arrays.fill(res, 0);
		    final Cursor<Article> c = this.repo.find(eq("state", NewsArticle.MARKED));
		    for(Article a: c)
			for(int i = 0;i < g.length;i++)
			    if (a.getGroupId() == g[i].id)
			    {
				res[i]++;
				break;
			    }
		    return res;
		});
	}
	catch(Exception e)
	{
	    throw new PimException(e);
	}
    }

    @Override public Set<String> loadUrisInGroup(NewsGroup group)
    {
	NullCheck.notNull(group, "group");
	final Group g = (Group)group;
	try {
	    return (Set<String>)storing.execInQueue(()->{
		    final Set<String> res = new HashSet();
		    for(Article a: repo.find(eq("groupId", g.id)))
			res.add(a.getUri());
		    return res;
		});
	}
	catch(Exception e)
	{
	    throw new PimException(e);
	}
    }
}
