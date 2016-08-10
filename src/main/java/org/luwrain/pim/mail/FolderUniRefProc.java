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

import org.luwrain.core.*;
import org.luwrain.util.*;
import org.luwrain.pim.RegistryKeys;

public class FolderUniRefProc implements UniRefProc
{
    static final String PREFIX = "mailfolder";

    private final RegistryKeys registryKeys = new RegistryKeys();
    private Luwrain luwrain;

    public FolderUniRefProc(Luwrain luwrain)
    {
	this.luwrain = luwrain;
	NullCheck.notNull(luwrain, "luwrain");
    }

    @Override public String getUniRefType()
    {
	return PREFIX;
    }

    @Override public UniRefInfo getUniRefInfo(String uniRef)
    {
	if (uniRef == null || uniRef.isEmpty())
	    return null;
	if (!uniRef.startsWith(PREFIX + ":"))
	    return new UniRefInfo(uniRef);
	int id = 0;
	try {
	    id = Integer.parseInt(uniRef.substring(PREFIX.length() + 1));
	}
	catch (NumberFormatException e)
	{
	    return new UniRefInfo(uniRef);
	}
	final Registry registry = luwrain.getRegistry();
	final RegistryAutoCheck check = new RegistryAutoCheck(registry);
	final String dirPath = Registry.join(registryKeys.mailFolders(), "" + id);
	final String path = Registry.join(dirPath, "title");
    final String title = check.stringAny(path, "");
    return new UniRefInfo(uniRef, "Почтовая группа", folderName(title));
}

		@Override public void openUniRef(String uniRef, Luwrain luwrain)
		{
		    if (uniRef == null || uniRef.isEmpty())
			return;
		    if (!uniRef.startsWith(PREFIX + ":"))
			return;
		    luwrain.launchApp("mail", new String[]{"--UNIREF",
							   uniRef.substring(PREFIX.length() + 1),
			});
		}

    private String folderName(String title)
    {
	//FIXME:
	switch(title)
	{
	case "luwrain-mail-folder-root":
	    return "Электронная почта";
	case "luwrain-mail-folder-inbox":
	    return "Входящие";
	case "luwrain-mail-folder-pending":
	    return "Исходящие";
	case "luwrain-mail-folder-sent":
	    return "Отправленные";
	case "luwrain-mail-folder-drafts":
	    return "Черновики";
	case "luwrain-mail-folder-private":
	    return "Личные";
	case "luwrain-mail-folder-lists":
	    return "Списки рассылок";
	default:
	    return title;
	}

    }
}
