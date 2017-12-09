/*
   Copyright 2012-2017 Michael Pozhidaev <michael.pozhidaev@gmail.com>
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

package org.luwrain.pim.mail.sql;

import org.luwrain.core.*;
import org.luwrain.util.*;

public class FolderUniRefProc implements UniRefProc
{
    static final String PREFIX = "mailfolder";

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
	final String path = Registry.join(org.luwrain.pim.mail.Settings.FOLDERS_PATH, "" + id);
	final org.luwrain.pim.mail.Settings.Folder sett = org.luwrain.pim.mail.Settings.createFolder(registry, path);
	final String title = sett.getTitle("");
    return new UniRefInfo(uniRef, "Почтовая группа", folderName(title));
}

		@Override public boolean openUniRef(String uniRef, Luwrain luwrain)
		{
		    if (uniRef == null || uniRef.isEmpty())
			return false;
		    if (!uniRef.startsWith(PREFIX + ":"))
			return false;
		    luwrain.launchApp("mail", new String[]{"--UNIREF",
							   uniRef.substring(PREFIX.length() + 1),
			});
		    return true;
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
