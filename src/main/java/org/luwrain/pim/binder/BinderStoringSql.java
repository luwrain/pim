
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
