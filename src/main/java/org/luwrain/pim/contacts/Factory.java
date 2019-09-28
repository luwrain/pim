
package org.luwrain.pim.contacts;

import java.sql.*;

import org.luwrain.core.*;
import org.luwrain.pim.util.*;

public final class Factory
{
    static private final String LOG_COMPONENT = "pim-contacts";
    static private final String SQLITE_INIT_RESOURCE = "org/luwrain/pim/contacts.sqlite";

    static public final String DEFAULT_TYPE = "jdbc";
    static public final String DEFAULT_DRIVER = "org.sqlite.JDBC";
    static public final String DEFAULT_URL = "jdbc:sqlite:$userdata/sqlite/contacts.db";
    static public final String DEFAULT_INIT_PROC = "sqlite";

    private final Luwrain luwrain;
    private final Registry registry;
    private final Settings.Storing sett;
    private final ExecQueues execQueues = new ExecQueues();
    private Connection con = null;

    public Factory(Luwrain luwrain)
    {
	NullCheck.notNull(luwrain, "luwrain");
	this.luwrain = luwrain;
	this.registry = luwrain.getRegistry();
	this.sett = Settings.createStoring(registry);
	this.execQueues.start();
    }

    public ContactsStoring newContactsStoring(boolean highPriority)
    {
	if (con != null)
	    return new org.luwrain.pim.contacts.sql.Storing(registry, con, execQueues, highPriority);
	final String type = sett.getType(DEFAULT_TYPE).trim().toLowerCase();
	if (type.isEmpty())
	{
	    Log.error(LOG_COMPONENT, "contacts storing type may not be empty");
	    return null;
	}
	switch(type)
	{
	case "jdbc":
	    {
	    	final String driver = sett.getDriver(DEFAULT_DRIVER);
		final String url = org.luwrain.pim.SQL.prepareUrl(luwrain, sett.getUrl(DEFAULT_URL));
		if (driver.isEmpty() || url.isEmpty())
		{
		    Log.error(LOG_COMPONENT, "in contacts storing settings for JDBC the driver and url values may not be empty");
		    return null;
		}
		final String login = sett.getLogin("");
		final String passwd = sett.getPasswd("");
		final String initProc = sett.getInitProc(DEFAULT_INIT_PROC);
		this.con = org.luwrain.pim.SQL.connect(driver, url, login, passwd);
		if (this.con == null)
		    return null;
		if (!org.luwrain.pim.SQL.initProc(con, initProc, SQLITE_INIT_RESOURCE))
		{
		    try {
			this.con.close();
		    }
		    catch(SQLException e)
		    {
		    }
		    this.con = null;
		    return null;
		}
		return new org.luwrain.pim.contacts.sql.Storing(registry, this.con, execQueues, highPriority);
	    }
	default:
	    Log.error(LOG_COMPONENT, "unknown contacts storing type \'" + type + "\'");
	    return null;
	}
    }

    public void close()
    {
	execQueues.cancel();
	if (con != null)
	{
	    try {
		con.close();
	    }
	    catch(SQLException e)
	    {
		Log.error(LOG_COMPONENT, "unable to close contacts JDBC connection normally:" + e.getClass().getName() + ":" + e.getMessage());
	    }
	    con = null;
	}
    }
}
