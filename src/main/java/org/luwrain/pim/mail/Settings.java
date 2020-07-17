/*
   Copyright 2012-2020 Michael Pozhidaev <msp@luwrain.org>
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

//LWR_API 1.0

package org.luwrain.pim.mail;

import org.luwrain.core.*;

public interface Settings
{
    static public final String
	STORING_PATH = "/org/luwrain/pim/mail/storing",
	FOLDERS_PATH = "/org/luwrain/pim/mail/folders",
	RULES_PATH = "/org/luwrain/pim/mail/rules",
	ACCOUNTS_PATH = "/org/luwrain/pim/mail/accounts";

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

    public interface Accounts
    {
	String getAccounts(String defValue);
	void setAccounts(String value);
	int getNextId(int defValue);
	void setNextId(int value);
    }

public interface Folder
{
    String getTitle(String defValue);
    	void setTitle(String value);
    int getOrderIndex(int defValue);
        void setOrderIndex(int defValue);
    int getParentId(int defValue);

    void setParentId(int defValue);
    String getProperties(String defValue);
    void setProperties(String value);
}

public interface Rule
{
    String getAction(String defValue);
    String getHeaderRegex(String defValue);
    String getDestFolderUniref(String defValue);
    void setAction(String value);
    void setHeaderRegex(String value);
    void setDestFolderUniref(String value);
}

    static public Accounts createAccounts(Registry registry)
    {
	NullCheck.notNull(registry, "registry");
	registry.addDirectory(ACCOUNTS_PATH);
	return RegistryProxy.create(registry, ACCOUNTS_PATH, Accounts.class);
    }

    static public Folder createFolder(Registry registry, String path)
    {
	NullCheck.notNull(registry, "registry");
	NullCheck.notNull(path, "path");
	return RegistryProxy.create(registry, path, Folder.class);
    }

    static public Rule createRule(Registry registry, String path)
    {
	NullCheck.notNull(registry, "registry");
	NullCheck.notNull(path, "path");
	return RegistryProxy.create(registry, path, Rule.class);
    }

    static public Storing createStoring(Registry registry)
    {
	NullCheck.notNull(registry, "registry");
	return RegistryProxy.create(registry, STORING_PATH, Storing.class);
    }
}
