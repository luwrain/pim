
package org.luwrain.pim.mail.script;

import java.util.*;
import java.util.function.*;

import org.luwrain.core.*;
import org.luwrain.script.*;
import org.luwrain.pim.*;
import org.luwrain.pim.mail.*;

public final class MailHookObject extends EmptyHookObject
{
    static final String LOG_COMPONENT = "pim";

    private final MailStoring storing;

    public MailHookObject(MailStoring storing)
    {
	NullCheck.notNull(storing, "storing");
	this.storing = storing;
    }

    @Override public Object getMember(String name)
    {
	NullCheck.notNull(name, "name");
	switch(name)
	{
	case "folders":
	    return new FoldersHookObject(storing);
	    	default:
	    return super.getMember(name);
	}
    }
}
