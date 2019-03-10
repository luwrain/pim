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

package org.luwrain.pim.mail.mem;

import org.luwrain.core.*;
import org.luwrain.pim.*;
import org.luwrain.pim.mail.*;

class Rule extends MailRule implements StoredMailRule
{
    final int id;

    Rule(int id)
    {
	if (id < 0)
	    throw new IllegalArgumentException("id (" + id + ") may not be negative");
	this.id = id;
    }

    @Override public Actions getAction()
    {
	return action;
    }

    @Override public void setAction(Actions value)
    {
	NullCheck.notNull(value, "value");
	this.action = value;
    }

    @Override public String getHeaderRegex()
    {
	return headerRegex;
    }

    @Override public void setHeaderRegex(String value)
    {
	NullCheck.notNull(value, "value");
	this.headerRegex = value;
    }

    @Override public String getDestFolderUniRef()
    {
	return destFolderUniRef;
    }

    @Override public void setDestFolderUniRef(String value)
    {
	NullCheck.notNull(value, "value");
	this.destFolderUniRef = value;
    }
    
    static String getActionStr(Actions action)
    {
	NullCheck.notNull(action, "action");
	switch(action)
	{
	case MOVE_TO_FOLDER:
	    return "move-to-folder";
	default:
	    return "";
	}
    }
}
