/*
   Copyright 2012-2019 Michael Pozhidaev <michael.pozhidaev@gmail.com>
   Copyright 2015 Roman Volovodov <gr.rPman@gmail.com>

   This file is part of LUWRAIN.

   LUWRAIN is free software; you can redistribute it and/or
   modify it under the terms of the GNU General Public
   License as published by the Free Software Foundation; either
   version 3 of the License, or (at your option) any later version.

   LUWRAIN is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
   General Public License for more details.
*/

package org.luwrain.pim.mail.script;

import java.util.*;
import java.util.function.*;

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
	    	default:
	    return super.getMember(name);
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
}
