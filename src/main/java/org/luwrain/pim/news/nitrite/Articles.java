/*
   Copyright 2012-2020 Michael Pozhidaev <michael.pozhidaev@gmail.com>
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

import org.luwrain.core.*;
import org.luwrain.pim.*;
import org.luwrain.pim.news.*;

final class Articles implements NewsArticles
{
    private final Storing storing;
    private final Nitrite db;
    private final ObjectRepository<Article> repo;

    Articles(Storing storing, Nitrite db)
    {
	NullCheck.notNull(storing, "storing");
	NullCheck.notNull(db, "db");
	this.storing = storing;
	this.db = db;
	this.repo = this.db.getRepository(Article.class);
    }

    @Override public void save(StoredNewsGroup newsGroup, NewsArticle article) throws PimException
    {
	NullCheck.notNull(newsGroup, "newsGroup");
	final Group g = (Group)newsGroup;
	try {
	    storing.execInQueue(()->{
		    final Article a = new Article();
		    a.copyValues(article);
		    a.groupId = g.id;
		    //		    Log.debug("proba", a.title);
		    this.repo.insert(a);
		    return null;
		});
	}
	catch(Exception e)
	{
	    throw new PimException(e);
	}
    }

    @Override public     StoredNewsArticle[] load(StoredNewsGroup group) throws PimException
    {
	final Group g = (Group)group;
	NullCheck.notNull(group, "group");
	try {
	    return (StoredNewsArticle[])storing.execInQueue(()->{
		    final List<Article> res = new LinkedList();
		    final Cursor<Article> c = this.repo.find(eq("groupId", g.id));
		    for(Article a: c)
		    {
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

    @Override public     StoredNewsArticle[] loadWithoutRead(StoredNewsGroup newsGroup) throws PimException
    {
	NullCheck.notNull(newsGroup, "newsGroup");
	final Group g = (Group)newsGroup;
	try {
	    return (StoredNewsArticle[])storing.execInQueue(()->{
		    return new Article[0];
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
	return 0;
    }

    @Override public int countNewInGroup(StoredNewsGroup group) throws PimException
    {
	NullCheck.notNull(group, "group");
	if (!(group instanceof Group))
	    return 0;
	final Group g = (Group)group;
	return 0;
    }

    @Override public int[] countNewInGroups(StoredNewsGroup[] groups) throws PimException
    {
	NullCheck.notNullItems(groups, "groups");
	final Group[] g = Arrays.copyOf(groups, groups.length, Group[].class);
	try {
	    return (int[])storing.execInQueue(()->{
	    final int[] res = new int[g.length];
	    Arrays.fill(res, 0);
	    final Cursor<Article> c = this.repo.find();
	    for(Article a: c)
		for(int i = 0;i < g.length;i++)
		    if (a.groupId == g[i].id)
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

    @Override public int[] countMarkedInGroups(StoredNewsGroup[] groups) throws PimException
    {
	NullCheck.notNullItems(groups, "groups");
	return new int[0];
    }

    @Override public Set<String> loadUrisInGroup(StoredNewsGroup group) throws PimException
    {
	NullCheck.notNull(group, "group");
	return null;
    }
}
