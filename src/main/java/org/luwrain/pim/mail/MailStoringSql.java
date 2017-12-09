
package org.luwrain.pim.mail;

import java.sql.*;
import java.util.*;

import org.luwrain.core.Registry;
import org.luwrain.core.NullCheck;
import org.luwrain.pim.*;

class MailStoringSql extends MailStoringRegistry
{
    static private final int FIELD_TYPE_TO = 1;
    static private final int FIELD_TYPE_CC = 2;
    static private final int FIELD_TYPE_BCC = 3;
    static private final int FIELD_TYPE_ATTACHMENT = 4;

    private final Connection con;

    MailStoringSql(Registry registry,Connection con)
    {
	super(registry);
	NullCheck.notNull(con, "con");
	this.con = con;
    }

    @Override public MailRules getRules()
    {
	return null;//FIXME:
    }

    @Override public MailMessages getMessages()
    {
	return null;
    }
}
