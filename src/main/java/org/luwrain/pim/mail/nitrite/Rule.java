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

package org.luwrain.pim.mail.nitrite;

import org.luwrain.core.*;
import org.luwrain.pim.*;
import org.luwrain.pim.mail.*;

class Rule extends MailRule implements StoredMailRule
{
    private final Registry registry;
    private final org.luwrain.pim.mail.Settings.Rule sett;
    final int id;

    Rule(Registry registry, int id)
    {
	NullCheck.notNull(registry, "registry");
	this.registry = registry;
	this.id = id;
	this.sett = org.luwrain.pim.mail.Settings.createRule(registry, Registry.join(org.luwrain.pim.mail.Settings.RULES_PATH, "" + id));
    }

    @Override public Actions getAction() throws PimException
    {
	return action;
    }

    @Override public void setAction(Actions value) throws PimException
    {
	NullCheck.notNull(value, "value");
	final String valueStr = getActionStr(value);
	sett.setAction(valueStr);
	this.action = value;
    }

    @Override public String getHeaderRegex() throws PimException
    {
	return headerRegex != null?headerRegex:"";
    }

    @Override public void setHeaderRegex(String value) throws PimException
    {
	NullCheck.notNull(value, "value");
	sett.setHeaderRegex(value);
	this.headerRegex = value;
    }

    @Override public String getDestFolderUniRef() throws PimException
    {
	return destFolderUniRef;
    }

    @Override public void setDestFolderUniRef(String value) throws PimException
    {
	NullCheck.notNull(value, "value");
	sett.setDestFolderUniref(value);
	this.destFolderUniRef = value;
    }

    boolean load()
    {
	final String actionStr = sett.getAction("");
	headerRegex = sett.getHeaderRegex("");
	destFolderUniRef= sett.getDestFolderUniref("");
	switch(actionStr.trim().toLowerCase())
	{
	case "move-to-folder":
	    action = Actions.MOVE_TO_FOLDER;
	    break;
	default:
	    return false;
	}
	return true;
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
