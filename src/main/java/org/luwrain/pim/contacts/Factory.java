
package org.luwrain.pim.contacts;

import java.sql.*;

import org.luwrain.core.*;

public class Factory
{
    private Registry registry;
    private Settings.Storing settings;
    private Connection con = null;

    public Factory(Registry registry)
    {
	NullCheck.notNull(registry, "registry");
	this.registry = registry;
	settings = Settings.createStoring(registry);
    }

    public ContactsStoring createContactsStoring()
    {
	if (settings.getSharedConnection(false) && con != null)
	    return new ContactsStoringSql(registry, con);
	final String type = settings.getType("");
	if (type.trim().isEmpty())
	{
	    Log.error("pim", "contacts storing type may not be empty");
	    return null;
	}
	if (!type.equals("jdbc"))
	{
	    Log.error("pim", "unknown storing type \'" + settings.getType("") + "\' for contacts");
	    return null;
	}
	final String driver = settings.getDriver("");
	final String url = settings.getUrl("");
	final String login = settings.getLogin("");
	final String passwd = settings.getPasswd("");
	if (driver.isEmpty() || url.isEmpty())
	{
	    Log.error("pim", "driver and url may not be empty in contacts storing settings");
	    return null;
	}
	try {
	    Class.forName (driver).newInstance ();
con = DriverManager.getConnection (url, login, passwd);
if (settings.getInitProc("").toLowerCase().equals("sqlite-wal"))
{
    Log.debug("pim", "performing sqlite-wal init procedure for contacts storing");
    final java.sql.ResultSet rs = con.createStatement().executeQuery("PRAGMA journal_mode = WAL;");
    while (rs.next());
}
    return new ContactsStoringSql(registry, con);
	}
	catch(ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException e)
	{
	    Log.error("pim", "unable to get JDBC connection for contacts:" + e.getClass().getName() + ":" + e.getMessage());
	    e.printStackTrace();
	    return null;
	}
    }

    public void close()
    {
	if (con == null)
	    return;
	Log.debug("pim", "closing JDBC connection for news");
	try {
	    con.close();
	}
	catch(SQLException e)
	{
	    Log.error("pim", "unable to close mail JDBC connection normally:" + e.getMessage());
	    e.printStackTrace();
	}
	con = null;
    }
}
