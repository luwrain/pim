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

package org.luwrain.pim.mail;

import org.luwrain.pim.*;

public interface MailFolders
{
    StoredMailFolder getRoot() throws PimException;
    StoredMailFolder[] load(StoredMailFolder folder) throws PimException;
    StoredMailFolder loadByUniRef(String uniRef) throws PimException;
    StoredMailFolder loadById(int id) throws PimException;
    void save(StoredMailFolder parentFolder, MailFolder newFolder) throws PimException;
    String getUniRef(StoredMailFolder folder) throws PimException;
    int getId(StoredMailFolder folder) throws PimException;
}
