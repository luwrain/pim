
package org.luwrain.pim.mail.script;

import java.util.*;

import org.luwrain.core.*;
import org.luwrain.script.*;
import org.luwrain.pim.*;
import org.luwrain.pim.mail.*;

public final class StoredFolderHookObject extends EmptyHookObject
{
    static private final String LOG_COMPONENT = MessageHookObject.LOG_COMPONENT;

    private final MailStoring storing;
    private final StoredMailFolder folder;

    public StoredFolderHookObject(MailStoring storing, StoredMailFolder folder)
    {
	NullCheck.notNull(storing, "storing");
	NullCheck.notNull(folder, "folder");
	this.storing = storing;
	this.folder = folder;
    }

    @Override public Object getMember(String name)
    {
	NullCheck.notNull(name, "name");
	switch(name)
	{
	case "title":
	    try{ 
		return folder.getTitle();
	    }
	    catch(PimException e)
	    {
		Log.error(LOG_COMPONENT, "unable to get a title of the stored mail folder:" + e.getClass().getName() + ":" + e.getMessage());
		return null;
	    }
	case "subfolders":
	    {
		final List<HookObject> res = new LinkedList();
		try {
		    for(StoredMailFolder f: storing.getFolders().load(folder))
			res.add(new StoredFolderHookObject(storing, f));
		}
		catch(PimException e)
		{
		    Log.error(LOG_COMPONENT, "unable to get subfolders of the stored mail folder:" + e.getClass().getName() + ":" + e.getMessage());
		    return null;
		}
		return ScriptUtils.createReadOnlyArray(res.toArray(new HookObject[res.size()]));
	    }
	    	default:
	    return super.getMember(name);
	}
    }

    
}
