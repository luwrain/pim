/*
   Copyright 2012-2023 Michael Pozhidaev <msp@luwrain.org>
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

import org.luwrain.pim.*;

public interface MailFolders
{
    static public final String
	PROP_DEFAULT_INCOMING  = "defaultIncoming",
	PROP_DEFAULT_OUTGOING  = "defaultOutgoing",
	PROP_DEFAULT_SENT = "defaultSent",
	PROP_DEFAULT_MAILING_LISTS = "defaultMailingLists",
	PROP_DEFAULT_DRAFTS = "defaultDrafts";

    MailFolder getRoot();
    int getId(MailFolder folder);
    MailFolder findFirstByProperty(String propName, String propValue);
    MailFolder[] load(MailFolder folder);
    MailFolder loadById(int id);
    MailFolder save(MailFolder parentFolder, MailFolder newFolder, int saveAtIndex);
    boolean remove(MailFolder parent, int index);
    boolean hasSubfolders(MailFolder folder);
}
