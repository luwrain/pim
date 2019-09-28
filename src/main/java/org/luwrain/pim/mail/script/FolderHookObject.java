
package org.luwrain.pim.mail.script;

import java.util.*;
import java.util.function.*;

import org.luwrain.core.*;
import org.luwrain.script.*;
import org.luwrain.pim.*;
import org.luwrain.pim.mail.*;

final class FolderHookObject extends EmptyHookObject
{
    static private final String LOG_COMPONENT = MailHookObject.LOG_COMPONENT;

    private final MailStoring storing;
    private final MailFolder folder;

    FolderHookObject(MailStoring storing, MailFolder folder)
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
		    for(MailFolder f: storing.getFolders().load(folder))
			res.add(new FolderHookObject(storing, f));
		}
		catch(PimException e)
		{
		    Log.error(LOG_COMPONENT, "unable to get subfolders of the stored mail folder:" + e.getClass().getName() + ":" + e.getMessage());
		    return null;
		}
		return ScriptUtils.createReadOnlyArray(res.toArray(new HookObject[res.size()]));
	    }
	case "saveMessage":
	    return (Predicate)this::saveMessage;
	case "properties":
	    try {
		return new PropertiesHookObject(folder.getProperties(), "");
		}
		catch(PimException e)
		{
		    Log.error(LOG_COMPONENT, "unable to get properties of the stored mail folder:" + e.getClass().getName() + ":" + e.getMessage());
		    return null;
		}
	    	case "saveProperties":
		    return (Supplier)this::saveProperties;
	case "newSubfolder":
	    return (Supplier)this::newSubfolder;
	    	default:
	    return super.getMember(name);
	}
    }

    @Override public void setMember(String name, Object obj)
    {
	NullCheck.notNull(name, "name");
	final String value = ScriptUtils.getStringValue(obj);
	if (name.isEmpty() || value == null)
	    return;
	switch(name)
	{
	    case "title":
		try {
		    folder.setTitle(value);
		    return;
		}
		catch(PimException e)
		{
		    Log.error(LOG_COMPONENT, "unable to set the title of the stored mail folder:" + e.getClass().getName() + ":" + e.getMessage());
		    return;
		}
	default:
	    return;
	}
    }

    private boolean saveMessage(Object o)
    {
	if (o == null || !(o instanceof MessageHookObject))
	    return false;
	try {
	    storing.getMessages().save(folder, ((MessageHookObject)o).message);
	    return true;
	}
	catch(PimException e)
	{
	    Log.error(LOG_COMPONENT, "unable to save the message in the stored mail folder:" + e.getClass().getName() + ":" + e.getMessage());
	    return false;
	}
    }

    private Object saveProperties()
    {
		    try {
		folder.saveProperties();
		return new Boolean(true);
		}
		catch(PimException e)
		{
		    Log.error(LOG_COMPONENT, "unable to save properties of the stored mail folder:" + e.getClass().getName() + ":" + e.getMessage());
		    return new Boolean(false);
		}
    }

    private Object newSubfolder()
    {
	try {
	    return new FolderHookObject(storing, storing.getFolders().save(folder, new MailFolder()));
	}
	catch(PimException e)
	{
	    Log.error(LOG_COMPONENT, "unable to create a mail subfolder:" + e.getClass().getName() + ":" + e.getMessage());
	    return null;
	}
    }
}
