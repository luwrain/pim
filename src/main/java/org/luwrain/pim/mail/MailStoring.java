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

package org.luwrain.pim.mail;

import org.luwrain.pim.mail.MailStoringSql.Condition;

public interface MailStoring extends Cloneable
{
    StoredMailFolder getFoldersRoot() throws Exception;
    StoredMailFolder[] getFolders(StoredMailFolder folder) throws Exception;
    String getFolderUniRef(StoredMailFolder folder) throws Exception;
    StoredMailFolder getFolderByUniRef(String uniRef) throws Exception;
    StoredMailAccount[] loadAccounts() throws Exception;
    void saveAccount(MailAccount account) throws Exception;
    void deleteAccount(StoredMailAccount account) throws Exception;
    StoredMailRule[] getRules() throws Exception;
    void saveRule(MailRule rule) throws Exception;
    void deleteRule(StoredMailRule rule) throws Exception;
    void saveMessage(StoredMailFolder folder, MailMessage message) throws Exception;
    StoredMailMessage[] loadMessages(StoredMailFolder folder) throws Exception;
    void moveMessageToFolder(StoredMailMessage message, StoredMailFolder folder) throws Exception;
}
