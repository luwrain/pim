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

package org.luwrain.pim.contacts;

public interface ContactsStoring
{
    StoredContactsFolder getFoldersRoot() throws Exception;
    StoredContactsFolder[] getFolders(StoredContactsFolder folder) throws Exception;
    void saveFolder(StoredContactsFolder addTo, ContactsFolder folder) throws Exception;
    void deleteFolder(StoredContactsFolder folder) throws Exception;
    StoredContact[] loadContacts(StoredContactsFolder folder) throws Exception;
    void saveContact(StoredContactsFolder folder, Contact contact) throws Exception;
    void deleteContact(StoredContact contact) throws Exception;
}
