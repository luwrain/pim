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
import java.util.*;

class BinderStoringSql implements BinderStoring
{
    private Connection con;

    public BinderStoringSql(Connection con)
    {
	this.con = con;
	if (con == null)
	    throw new NullPointerException("con may not be null");
    }

    @Override public StoredCase[] getCases() throws Exception
    {
	Statement st = con.createStatement();
	ResultSet rs = st.executeQuery("SELECT id,title,status,priority,notes FROM binder_cases ORDER BY priority");
	LinkedList<StoredCase> res = new LinkedList<StoredCase>();
	while (rs.next())
	{
	    final StoredCaseSql c = new StoredCaseSql(con);
	    c.id = rs.getLong(1);
	    c.title = rs.getString(2).trim();
	    c.status = rs.getInt(3);
	    c.priority = rs.getInt(4);
	    c.tags = new String[0];//FIXME:
	    c.uniRefs = new String[0];
	    c.notes = rs.getString(5).trim();
	    res.add(c);
	}

	//Tags;
	st = con.createStatement();
	rs = st.executeQuery("SELECT case_id,value FROM binder_case_tag");
	TreeMap<Long, LinkedList<String>> treeMap = new TreeMap<Long, LinkedList<String>>();
	while(rs.next())
	{
	    final long caseId = rs.getLong(1);
	    final String value = rs.getString(2).trim();
	    if (!treeMap.containsKey(new Long(caseId)))
	    {
		LinkedList<String> newList = new LinkedList<String>();
		newList.add(value);
		treeMap.put(new Long(caseId), newList);
	    } else
		treeMap.get(new Long(caseId)).add(value);
	}
	for(StoredCase sc: res)
	{
	    final StoredCaseSql c = (StoredCaseSql)sc;
	    if (!treeMap.containsKey(new Long(c.id)))
		continue;
	    final LinkedList<String> tagsList = treeMap.get(new Long(c.id));
	    c.tags = tagsList.toArray(new String[tagsList.size()]);
	}

	//UniRefs;
	st = con.createStatement();
	rs = st.executeQuery("SELECT case_id,value FROM binder_case_uniref");
treeMap = new TreeMap<Long, LinkedList<String>>();
	while(rs.next())
	{
	    final long caseId = rs.getLong(1);
	    final String value = rs.getString(2).trim();
	    if (!treeMap.containsKey(new Long(caseId)))
	    {
		LinkedList<String> newList = new LinkedList<String>();
		newList.add(value);
		treeMap.put(new Long(caseId), newList);
	    } else
		treeMap.get(new Long(caseId)).add(value);
	}
	for(StoredCase sc: res)
	{
	    final StoredCaseSql c = (StoredCaseSql)sc;
	    if (!treeMap.containsKey(new Long(c.id)))
		continue;
	    final LinkedList<String> uniRefsList = treeMap.get(new Long(c.id));
	    c.uniRefs = uniRefsList.toArray(new String[uniRefsList.size()]);
	}


	return res.toArray(new StoredCase[res.size()]);
    }

    @Override public void saveCase(Case c) throws Exception
    {
	//FIXME:
    }

    @Override public void deleteCase(StoredCase c) throws Exception
    {
	//FIXME:
    }
}
