
package org.luwrain.pim.mail;

import java.sql.*;

import org.luwrain.core.*;
import org.luwrain.pim.*;

public class Factory
{
    private final Luwrain luwrain;
    private final Registry registry;
    private final Settings.Storing settings;
    private Connection con = null;

    public Factory(Luwrain luwrain)
    {
	NullCheck.notNull(luwrain, "luwrain");
	this.luwrain = luwrain;
	this.registry = luwrain.getRegistry();
	this.settings = Settings.createStoring(registry);
    }

    private MailStoring createMailStoring()
    {
	if (settings.getSharedConnection(false) && con != null)
	    return new MailStoringSql(registry, con);
	final String type = settings.getType("");
	if (type.trim().isEmpty())
	{
	    Log.error("pim", "mail storing type may not be empty");
	    return null;
	}
	if (!type.equals("jdbc"))
	{
	    Log.error("pim", "unknown storing type \'" + settings.getType("") + "\' for mail");
	    return null;
	}
	final String driver = settings.getDriver("");
	final String url = org.luwrain.pim.SQL.prepareUrl(luwrain, settings.getUrl(""));
	final String login = settings.getLogin("");
	final String passwd = settings.getPasswd("");
	if (driver.isEmpty() || url.isEmpty())
	{
	    Log.error("pim", "driver and url may not be empty in mail storing settings");
	    return null;
	}
	Log.debug("pim", "opening connection for mail:URL=" + url + ",driver=" + driver + ",login=" + login);
	try {
	    Class.forName (driver).newInstance ();
con = DriverManager.getConnection (url, login, passwd);
if (settings.getInitProc("").toLowerCase().equals("sqlite-wal"))
{
    Log.debug("pim", "performing sqlite-wal init procedure for mail storing");
    final java.sql.ResultSet rs = con.createStatement().executeQuery("PRAGMA journal_mode = WAL;");
    while (rs.next());
}
    return new MailStoringSql(registry, con);
	}
	catch(ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException e)
	{
	    Log.error("pim", "unable to get JDBC connection for mail:" + e.getClass().getName() + ":" + e.getMessage());
	    e.printStackTrace();
	    return null;
	}
    }

    public void close()
    {
	if (con == null)
	    return;
	Log.debug("pim", "closing JDBC connection for mail");
	try {
	    con.close();
	}
	catch (SQLException e)
	{
	    Log.error("pim", "unable to close mail JDBC connection normally:" + e.getMessage());
	    e.printStackTrace();
	}
	con = null;
    }

    static public MailStoring getMailStoring(Luwrain luwrain)
    {
	NullCheck.notNull(luwrain, "luwrain");
	final Object o = luwrain.getSharedObject(Extension.MAIL_SHARED_OBJECT);
	if (o == null)
	{
	    Log.error("pim", "unable to get mail storing:no shared object " + Extension.MAIL_SHARED_OBJECT);
	    return null;
	}
	if (!(o instanceof Factory))
	{
	    Log.error("pim", "unable to get mail storing:shared object " + Extension.MAIL_SHARED_OBJECT + " is not an instance of " + Factory.class.getName());
	    return null;
	}
	final Factory factory = (Factory)o;
	return factory.createMailStoring();
    }
}
