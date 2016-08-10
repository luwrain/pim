/*
   Copyright 2012-2016 Michael Pozhidaev <michael.pozhidaev@gmail.com>
   Copyright 2015 Roman Volovodov <gr.rPman@gmail.com>

   This file is part of the LUWRAIN.

   LUWRAIN is free software; you can redistribute it and/or
   modify it under the terms of the GNU General Public
   License as published by the Free Software Foundation; either
   version 3 of the License, or (at your option) any later version.

   LUWRAIN is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
   General Public License for more details.
*/

package org.luwrain.pim.mail;

import org.luwrain.core.Registry;
import org.luwrain.core.NullCheck;
import org.luwrain.util.*;
import org.luwrain.pim.RegistryKeys;

class StoredMailRuleRegistry extends MailRule implements StoredMailRule
{
    private final RegistryKeys registryKeys = new RegistryKeys();
    private Registry registry;
    int id;

    StoredMailRuleRegistry(Registry registry, int id)
    {
	this.registry = registry;
	this.id = id;
	NullCheck.notNull(registry, "registry");
    }

    @Override public int getAction() throws Exception
    {
	return action;
    }

    @Override public void setAction(int value) throws Exception
    {
	final String valueStr = getActionStr(value);
	if (!registry.setString(Registry.join(getPath(), "action"), valueStr))
	    updateError("action");
	action = value;
    }

    @Override public String getHeaderRegex() throws Exception
    {
	return headerRegex != null?headerRegex:"";
    }

    @Override public void setHeaderRegex(String value) throws Exception
    {
	NullCheck.notNull(value, "value");
	if (!registry.setString(Registry.join(getPath(), "header-regex"), value))
	    updateError("header-regex");
	headerRegex = value;
    }

    @Override public String getDestFolderUniRef() throws Exception
    {
	return destFolderUniRef != null?destFolderUniRef:"";
    }

    @Override public void setDestFolderUniRef(String value) throws Exception
    {
	NullCheck.notNull(value, "value");
	if (!registry.setString(Registry.join(getPath(), "dest-folder-uniref"), value))
	    updateError("dest-folder-uniref");
	destFolderUniRef = value;
    }

    boolean load()
    {
	final RegistryAutoCheck check = new RegistryAutoCheck(registry);
	final String path = getPath();
	final String actionStr = check.stringNotEmpty(Registry.join(path, "action"), "").toLowerCase().trim();
	headerRegex = check.stringAny(Registry.join(path, "header-regex"), "");
	destFolderUniRef=  check.stringAny(Registry.join(path, "dest-folder-uniref"), "");
	switch(actionStr)
	{
	case "move-to-folder":
	    action = ACTION_MOVE_TO_FOLDER;
	    break;
	default:
	    return false;
	}
	return true;
    }

    private String getPath()
    {
	return Registry.join(registryKeys.mailRules(), "" + id);
    }

    static String getActionStr(int code)
    {
	switch(code)
	{
	case ACTION_MOVE_TO_FOLDER:
	    return "move-to-folder";
	default:
	    return "";
	}
    }

    void updateError(String param) throws Exception
    {
	throw new Exception("Unable to update in the registry " + getPath() + "/" + param);
    }
}
