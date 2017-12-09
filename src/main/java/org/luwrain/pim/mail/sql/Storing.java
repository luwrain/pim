
package org.luwrain.pim.mail.sql;

import java.sql.*;
import java.util.*;

import org.luwrain.core.*;
import org.luwrain.pim.*;
import org.luwrain.pim.mail.*;

public final class Storing implements MailStoring
{
    private final Registry registry;
    private final Connection con;

    public Storing(Registry registry,Connection con)
    {
	NullCheck.notNull(registry, "registry");
	NullCheck.notNull(con, "con");
	this.registry = registry;
	this.con = con;
	    }

    @Override public MailRules getRules()
    {
	return null;//FIXME:
    }

    @Override public MailFolders getFolders()
    {
	return null;
    }

    @Override public MailAccounts getAccounts()
    {
	return null;
    }

    @Override public MailMessages getMessages()
    {
	return null;
    }
}
