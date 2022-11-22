/*
   Copyright 2012-2021 Michael Pozhidaev <msp@luwrain.org>
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

import org.graalvm.polyglot.*;
import org.graalvm.polyglot.proxy.*;

import org.luwrain.core.*;
import org.luwrain.pim.*;
import org.luwrain.pim.mail.*;

final class FolderHookObject
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

    @HostAccess.Export
    public ProxyExecutable getTitle = (ProxyExecutable)this::getTitleImpl;
    public Object getTitleImpl(Value[] args)
    {
			return folder.getTitle();
    }

    public Object setTitleImpl(Value[] args)
    {
	//FIXME:
	return null;
    }

    public Object getSubfolders()
    {
			    final List<Object> res = new ArrayList<>();
		    for(MailFolder f: storing.getFolders().load(folder))
			res.add(new FolderHookObject(storing, f));
		    return ProxyArray.fromArray(res.toArray(new Object[res.size()]));
    }


    private boolean saveMessage(Object o)
    {
	if (o == null || !(o instanceof MessageObj))
	    return false;
	try {
	    storing.getMessages().save(folder, ((MessageObj)o).message);
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
		folder.save();
		return new Boolean(true);
		}
		catch(Exception e)
		{
		    Log.error(LOG_COMPONENT, "unable to save properties of the stored mail folder:" + e.getClass().getName() + ":" + e.getMessage());
		    return new Boolean(false);
		}
    }

    private Object newSubfolder()
    {
	try {
	    return new FolderHookObject(storing, storing.getFolders().save(folder, new MailFolder(), 0));
	}
	catch(PimException e)
	{
	    Log.error(LOG_COMPONENT, "unable to create a mail subfolder:" + e.getClass().getName() + ":" + e.getMessage());
	    return null;
	}
    }
}
