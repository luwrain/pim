
package org.luwrain.pim.news;

import java.sql.*;

import org.luwrain.pim.RegistryKeys;
import org.luwrain.core.*;
import org.luwrain.util.RegistryAutoCheck;

public class Factory
{
    private Registry registry;
    private RegistryKeys registryKeys = new RegistryKeys();

    public Factory(Registry registry)
    {
	this.registry = registry;
	NullCheck.notNull(registry, "registry");
    }

    public NewsStoring createNewsStoring()
    {
	final RegistryAutoCheck check = new RegistryAutoCheck(registry);
	final String type = check.stringNotEmpty(registryKeys.newsType(), "");
	if (!type.equals("jdbc"))
	    return null;
	final String driver = check.stringNotEmpty(registryKeys.newsDriver(), "");
	final String url = check.stringNotEmpty(registryKeys.newsUrl(), "");
	final String login = check.stringAny(registryKeys.newsLogin(), "");
	final String passwd = check.stringAny(registryKeys.newsPasswd(), "");
	if (driver.isEmpty() || url.isEmpty())
	    return null;
	try {
	    Class.forName (driver).newInstance ();
	    return new NewsStoringSql(registry, DriverManager.getConnection (url, login, passwd));
	}
	catch(Exception e)
	{
	    e.printStackTrace();
	    return null;
	}
    }
}
