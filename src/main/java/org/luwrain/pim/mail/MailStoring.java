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

import org.luwrain.pim.*;
import org.luwrain.pim.mail.MailStoringSql.Condition;

public interface MailStoring extends Cloneable
{
    StoredMailFolder getFoldersRoot() throws PimException;
    StoredMailFolder[] getFolders(StoredMailFolder folder) throws PimException;
    String getFolderUniRef(StoredMailFolder folder) throws PimException;
    StoredMailFolder getFolderByUniRef(String uniRef) throws PimException;
    StoredMailAccount[] loadAccounts() throws PimException;
    StoredMailAccount loadAccountById(long id) throws PimException;
    void saveAccount(MailAccount account) throws PimException;
    void deleteAccount(StoredMailAccount account) throws PimException;
    String getAccountUniRef(StoredMailAccount account) throws PimException;
    StoredMailAccount getAccountByUniRef(String uniRef) throws PimException;
    StoredMailRule[] getRules() throws PimException;
    void saveRule(MailRule rule) throws PimException;
    void deleteRule(StoredMailRule rule) throws PimException;
    void saveMessage(StoredMailFolder folder, MailMessage message) throws PimException;
    StoredMailMessage[] loadMessages(StoredMailFolder folder) throws PimException;
    void moveMessageToFolder(StoredMailMessage message, StoredMailFolder folder) throws PimException;
    void deleteMessage(StoredMailMessage message) throws PimException;
}
