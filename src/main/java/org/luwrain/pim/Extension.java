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

package org.luwrain.pim;

import org.luwrain.base.*;
import org.luwrain.core.*;

public class Extension extends org.luwrain.core.extensions.EmptyExtension
{
    static public final String CONTACTS_SHARED_OBJECT = "luwrain.pim.contacts";
    static public final String MAIL_SHARED_OBJECT = "luwrain.pim.mail";



    private org.luwrain.pim.mail.sql.FolderUniRefProc mailFolderUniRefProc;

    @Override public String init(Luwrain luwrain)
    {
	NullCheck.notNull(luwrain, "luwrain");
	Connections.init(luwrain);
	return null;
    }

    @Override public org.luwrain.cpanel.Factory[] getControlPanelFactories(Luwrain luwrain)
    {
	NullCheck.notNull(luwrain, "luwrain");
	return new org.luwrain.cpanel.Factory[]{
	    new org.luwrain.settings.news.Factory(luwrain),
	    new org.luwrain.settings.mail.Factory(luwrain),
	};
    }

	@Override public UniRefProc[] getUniRefProcs(Luwrain luwrain)
	{
	    if (mailFolderUniRefProc == null)
		mailFolderUniRefProc = new org.luwrain.pim.mail.sql.FolderUniRefProc(luwrain);
	    return new UniRefProc[]{
		mailFolderUniRefProc,
	    };
    }

    @Override public void close()
    {
	Connections.close();
    }
}
