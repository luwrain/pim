
package org.luwrain.pim.mail.script;

import org.luwrain.core.*;
import org.luwrain.script.*;
import org.luwrain.pim.*;
import org.luwrain.pim.mail.*;

final class AddressHookObject extends EmptyHookObject
{
    static private final String LOG_COMPONENT = MailHookObject.LOG_COMPONENT;

    private final String full;
    private final String personal;
    private final String addr;

    AddressHookObject(String full)
    {
	NullCheck.notNull(full, "full");
	this.full = full;
	if (!this.full.trim().isEmpty())
	{
	    this.personal = AddressUtils.getPersonal(full);
	    this.addr = AddressUtils.getAddress(full);
	} else
	{
	this.personal = "";
	this.addr = "";
	}
    }

        @Override public Object getMember(String name)
    {
	NullCheck.notNull(name, "name");
	switch(name)
	{
	case "full":
	    return full;
	case "personal":
	    return personal;
	case "addr":
	    return addr;
	    	default:
	    return super.getMember(name);
	}
    }
}
