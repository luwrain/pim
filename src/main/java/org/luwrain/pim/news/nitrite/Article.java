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
import java.io.*;

import org.dizitart.no2.*;
import org.dizitart.no2.objects.*;
import org.dizitart.no2.objects.Cursor;
import static org.dizitart.no2.objects.filters.ObjectFilters.eq;
import static org.dizitart.no2.objects.filters.ObjectFilters.not;
import static org.dizitart.no2.objects.filters.ObjectFilters.and;

import org.luwrain.core.*;
import org.luwrain.pim.*;
import org.luwrain.pim.news.*;

final class Article extends NewsArticle 
{
    private transient Storing storing;
    private transient ObjectRepository<Article> repo = null;

    private String id = null;
    private int groupId;

    public int getGroupId()
    {
	return this.groupId;
    }

    public void setGroupId(int groupId)
    {
	this.groupId = groupId;
    }

    void setStoring(Storing storing, ObjectRepository<Article> repo)
    {
	NullCheck.notNull(storing, "storing");
	NullCheck.notNull(repo, "repo");
	this.storing = storing;
	this.repo = repo;
    }

    @Override public void save() throws PimException
    {
	verifyStoring();
	try {
	    storing.execInQueue(()->{
		    repo.update(eq("id", id), this);
		    return null;
		});
	}
	catch(Exception e)
	{
	    throw new PimException(e);
	}
    }

    protected void verifyStoring()
    {
	if (storing == null || repo == null)
	    throw new IllegalStateException("No storing, setStoring() must be called prior to any modification operations");
	if (id == null || id.isEmpty())
	    throw new IllegalStateException("No ID");
    }

    public void genNewId()
    {
	this.id = UUID.randomUUID().toString();
    }
}
