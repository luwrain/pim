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
