/*
   Copyright 2012-2015 Michael Pozhidaev <michael.pozhidaev@gmail.com>
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

package org.luwrain.pim.contacts;

import org.luwrain.core.Registry;
import org.luwrain.core.NullCheck;
import org.luwrain.util.RegistryPath;
import org.luwrain.util.RegistryAutoCheck;
import org.luwrain.pim.RegistryKeys;

class StoredContactsFolderRegistry extends ContactsFolder implements StoredContactsFolder
{
    private final RegistryKeys registryKeys = new RegistryKeys();
    private Registry registry;

    int id;
    int parentId; 

    StoredContactsFolderRegistry(Registry registry)
    {
	this.registry = registry;
	NullCheck.notNull(registry, "registry");
    }

    @Override public String getTitle() throws Exception
    {
	return title != null?title:"";
    }

    @Override public void setTitle(String value) throws Exception
    {
	NullCheck.notNull(value, "value");
	if (!registry.setString(RegistryPath.join(getPath(), "title"), value))
	    updateError("title");
	title = value;
    }

    @Override public int getOrderIndex() throws Exception
    {
	return orderIndex;
    }

    @Override public void setOrderIndex(int value) throws Exception
    {

	if (!registry.setInteger(RegistryPath.join(getPath(), "order-index"), value))
	    updateError("order-index");
	orderIndex = value;
    }

    void setParentId(int value) throws Exception
    {
	if (!registry.setInteger(RegistryPath.join(getPath(), "parent-id"), value))
	    updateError("parent-id");
	parentId = value;
    }

    boolean load()
    {
	final RegistryAutoCheck check = new RegistryAutoCheck(registry);
	final String path = getPath();
	title = check.stringNotEmpty(RegistryPath.join(path, "title"), "");
	orderIndex = check.intPositive(RegistryPath.join(path, "order-index"), -1);
	parentId = check.intPositive(RegistryPath.join(path, "parent-id"), -1);
	if (title.isEmpty() || parentId < 0)
	    return false;
	if (orderIndex < 0)
	    orderIndex = 0;
	return true;
    }

    @Override public boolean equals(Object o)
    {
	if (o == null || !(o instanceof StoredContactsFolderRegistry))
	    return false;
	final StoredContactsFolderRegistry folder = (StoredContactsFolderRegistry)o;
	return id == folder.id;
    }

    String getPath()
    {
return RegistryPath.join(registryKeys.contactsFolders(), "" + id);
    }

    private void updateError(String param) throws Exception
    {
	throw new Exception("Unable to update in the registry " + getPath() + "/" + param);
    }
}
