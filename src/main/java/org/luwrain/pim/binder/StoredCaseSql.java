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

package org.luwrain.pim.binder;

import java.sql.*;

class StoredCaseSql implements StoredCase, Comparable
{
    private Connection con;

    public long id;
    public String title;
    public int status;
    public int priority;
    public String[] tags;
    public String[] uniRefs;
    public String notes;

    public StoredCaseSql(Connection con)
    {
	this.con = con;
	if (con == null)
	    throw new NullPointerException("con may not be null");
    }

    @Override public String getTitle() throws Exception
    {
	return title != null?title:"";
    }

    @Override public void setTitle(String value) throws Exception
    {
	//FIXME:
    }

    @Override public int getStatus() throws Exception
    {
	return status;
    }

    @Override public void setStatus(int value) throws Exception
    {
	//FIXME:
    }

    @Override public int getPriority() throws Exception
    {
	return priority;
    }

    @Override public void setPriority(int value) throws Exception
    {
	//FIXME:
    }

    @Override public String[] getTags() throws Exception
    {
	return tags != null?tags:new String[0];
    }

    @Override public void setTags(String[] value) throws Exception
    {
	//FIXME:
    }

    @Override public String[] getUniRefs() throws Exception
    {
	return uniRefs != null?uniRefs:new String[0];
    }

    @Override public void setUniRefs(String[] value) throws Exception
    {
	//FIXME:
    }

    @Override public String getNotes() throws Exception
    {
	return notes != null?notes:"";
    }

    @Override public void setNotes(String value) throws Exception
    {
	//FIXME:

	//FIXME:
    }

    @Override public String toString()
    {
	return title != null?title:"";
    }

    @Override public boolean equals(Object o)
    {
	if (o == null || !(o instanceof StoredCaseSql))
	    return false;
	final StoredCaseSql c = (StoredCaseSql)o;
	return id == c.id;
    }

    @Override public int compareTo(Object o)
    {
	if (o == null || !(o instanceof StoredCaseSql))
	    return 0;
	final StoredCaseSql c = (StoredCaseSql)o;
	if (priority < c.priority)
	    return -1;
	if (priority > c.priority)
	    return 1;
	return 0;
    }
}
