/*
   Copyright 2012-2021 Michael Pozhidaev <michael.pozhidaev@gmail.com>
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

package org.luwrain.pim.news;

import org.luwrain.core.*;

public interface Settings
{
    static public final String STORING_PATH = "/org/luwrain/pim/news/storing";
    static public final String GROUPS_PATH = "/org/luwrain/pim/news/groups";

    public interface Storing
    {
	String getType(String defValue);
	String getDriver(String defValue);
	String getUrl(String defValue);
    String getLogin(String defValue);
String getPasswd(String defValue);
String getInitProc(String defValue);
void setType(String value);
void setDriver(String value);
void setUrl(String value);
void setLogin(String value);
void setPasswd(String value);
void setInitProc(String value);
}

    public interface Group
    {
	String getName(String defValue);
	void setName(String value);
	int getExpireDays(int defValue);
	void setExpireDays(int value);
	int getOrderIndex(int defValue);
	void setOrderIndex(int value);
	String getMediaContentType(String defValue);
	void setMediaContentType(String defValue);
    }

    static public Group createGroup(Registry registry, String path)
    {
	NullCheck.notNull(registry, "registry");
	NullCheck.notNull(path, "path");
	return RegistryProxy.create(registry, path, Group.class);
    }

    static public Storing createStoring(Registry registry)
    {
	NullCheck.notNull(registry, "registry");
	return RegistryProxy.create(registry, STORING_PATH, Storing.class);
    }
}
