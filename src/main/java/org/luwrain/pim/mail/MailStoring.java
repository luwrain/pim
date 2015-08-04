/*
   Copyright 2012-2015 Michael Pozhidaev <msp@altlinux.org>

   This file is part of the Luwrain.

   Luwrain is free software; you can redistribute it and/or
   modify it under the terms of the GNU General Public
   License as published by the Free Software Foundation; either
   version 3 of the License, or (at your option) any later version.

   Luwrain is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
   General Public License for more details.
*/

package org.luwrain.pim.mail;

import org.luwrain.pim.mail.MailStoringSql.Condition;

public interface MailStoring extends Cloneable
{
    StoredMailFolder getFoldersRoot() throws Exception;
    StoredMailFolder[] getFolders(StoredMailFolder folder) throws Exception;
    void saveMessage(StoredMailFolder folder, MailMessage message) throws Exception;
    StoredMailMessage[] loadMessages(StoredMailFolder folder) throws Exception;
}
