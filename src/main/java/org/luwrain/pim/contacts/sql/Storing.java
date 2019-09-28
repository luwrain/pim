
package org.luwrain.pim.contacts.sql;

import java.sql.*;

import org.luwrain.core.*;
import org.luwrain.pim.*;
import org.luwrain.pim.contacts.*;
import org.luwrain.pim.util.*;

public class Storing implements ContactsStoring
{
    private final Registry registry;
    private final Connection con;
    private final ExecQueues execQueues;
    private final boolean highPriority;

    public Storing(Registry registry, Connection con, ExecQueues execQueues, boolean highPriority)
    {
	NullCheck.notNull(registry, "registry");
	NullCheck.notNull(con, "con");
	NullCheck.notNull(execQueues, "execQueues");
	this.registry = registry;
	this.con = con;
	this.execQueues = execQueues;
	this.highPriority = highPriority;
    }

    @Override public org.luwrain.pim.contacts.Contacts getContacts()
    {
	return null;
    }

    @Override public ContactsFolders getFolders()
    {
	return null;
    }
}
