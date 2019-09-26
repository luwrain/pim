
package org.luwrain.pim.mail.script;

import java.util.*;
import java.util.function.*;

import org.luwrain.core.*;
import org.luwrain.script.*;
import org.luwrain.pim.*;
import org.luwrain.pim.mail.*;

final class FoldersHookObject extends EmptyHookObject
{
    static private final String LOG_COMPONENT = MailHookObject.LOG_COMPONENT;

    private final MailStoring storing;

    FoldersHookObject(MailStoring storing)
    {
	NullCheck.notNull(storing, "storing");
	this.storing = storing;
    }

    @Override public Object getMember(String name)
    {
	NullCheck.notNull(name, "name");
	switch(name)
	{
	case "local":
	    try{ 
		return new StoredFolderHookObject(storing, storing.getFolders().getRoot());
	    }
	    catch(PimException e)
	    {
		Log.error(LOG_COMPONENT, "unable to get the root of the local mail folders:" + e.getClass().getName() + ":" + e.getMessage());
		return null;
	    }
	case "findFirstByProperty":
	    return (BiFunction)this::findFirstByProperty;
	    	default:
	    return super.getMember(name);
	}
    }

    private Object findFirstByProperty(Object nameObj, Object valueObj)
    {
	final String name = ScriptUtils.getStringValue(nameObj);
	final String value = ScriptUtils.getStringValue(valueObj);
	if (name == null || value == null || name.trim().isEmpty())
	    return null;
	final StoredMailFolder res = storing.getFolders().findFirstByProperty(name, value);
	return res != null?new StoredFolderHookObject(storing, res):null;
    }
}
