/*
   Copyright 2012-2019 Michael Pozhidaev <msp@luwrain.org>
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

package org.luwrain.pim.contacts;

import org.luwrain.core.*;

public interface Settings
{
    static public final String STORING_PATH = "/org/luwrain/pim/contacts/storing";
    static public final String FOLDERS_PATH = "/org/luwrain/pim/contacts/folders";

public interface Folder
{
    String getTitle(String defValue);
    int getOrderIndex(int defValue);
    int getParentId(int defValue );
    void setTitle(String value);
    void setOrderIndex(int value);
    void setParentId(int value);
}

    public interface Storing
    {
	String getType(String defValue);
	String getDriver(String defValue);
	String getUrl(String defValue);
	String getLogin(String defValue);
	String getPasswd(String defValue);
	boolean getSharedConnection(boolean defValue);
	String getInitProc(String defValue);
	void setType(String value);
	void setDriver(String value);
	void setUrl(String value);
	void setLogin(String value);
	void setPasswd(String value);
	void setSharedConnection(boolean value);
	void setInitProc(String value);
    }

    static public Storing createStoring(Registry registry)
    {
	NullCheck.notNull(registry, "registry");
	return RegistryProxy.create(registry, STORING_PATH, Storing.class);
    }

    static public Folder createFolder(Registry registry, String path)
    {
	NullCheck.notNull(registry, "registry");
	NullCheck.notEmpty(path, "path");
	return RegistryProxy.create(registry, path, Folder.class);
    }
}
