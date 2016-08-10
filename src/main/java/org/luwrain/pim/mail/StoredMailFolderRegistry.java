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
import org.luwrain.pim.*;
import org.luwrain.util.*;

class StoredMailFolderRegistry extends MailFolder implements StoredMailFolder
{
    private final RegistryKeys registryKeys = new RegistryKeys();
    private Registry registry;

    int id;
    int parentId;
    StoredMailFolderRegistry(Registry registry, int id)
    {
	this.registry = registry;
	this.id = id;
	NullCheck.notNull(registry, "registry");
    }

    @Override public String getTitle() throws PimException
    {
	return title != null?title:"";
    }

    @Override public void setTitle(String value) throws PimException
    {
	//FIXME:
    }

    @Override public int getOrderIndex() throws PimException
    {
	return orderIndex;
    }

    @Override public void setOrderIndex(int value) throws PimException
    {
	//FIXME:
    }

    @Override public boolean equals(Object o)
    {
	if (o == null || !(o instanceof StoredMailFolderRegistry))
	    return false;
	final StoredMailFolderRegistry folder = (StoredMailFolderRegistry)o;
	return id == folder.id;
    }

    boolean load()
    {
	final RegistryAutoCheck check = new RegistryAutoCheck(registry);
	final String path = getPath();
	title = check.stringNotEmpty(Registry.join(path, "title"), "");
	orderIndex = check.intPositive(Registry.join(path, "order-index"), -1);
	parentId = check.intPositive(Registry.join(path, "parent-id"), -1);
	if (title.isEmpty() || parentId < 0)
	    return false;
	if (orderIndex < 0)
	    orderIndex = 0;
	return true;
    }

    private String getPath()
    {
	return Registry.join(registryKeys.mailFolders(), "" + id);
    }

    void updateError(String param) throws Exception
    {
	throw new Exception("Unable to update in the registry " + getPath() + "/" + param);
    }
}
