
package org.luwrain.pim.binder;

import java.sql.*;

import org.luwrain.pim.*;

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

    public String[] getTags() throws PimException
    {
	return tags != null?tags:new String[0];
    }

    public void setTags(String[] value) throws PimException
    {
	//FIXME:
    }

    public String[] getUniRefs() throws PimException
    {
	return uniRefs != null?uniRefs:new String[0];
    }

    public void setUniRefs(String[] value) throws PimException
    {
	//FIXME:
    }

    public String getNotes() throws PimException
    {
	return notes != null?notes:"";
    }

    public void setNotes(String value) throws PimException
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
