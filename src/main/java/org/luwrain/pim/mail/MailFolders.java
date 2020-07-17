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

import org.luwrain.pim.*;

public interface MailFolders
{
    int getId(MailFolder folder) throws PimException;
    MailFolder getRoot() throws PimException;
    String getUniRef(MailFolder folder) throws PimException;
    MailFolder findFirstByProperty(String propName, String propValue) throws PimException;
    MailFolder[] load(MailFolder folder) throws PimException;
    MailFolder loadById(int id) throws PimException;
    MailFolder loadByUniRef(String uniRef) throws PimException;
    MailFolder save(MailFolder parentFolder, MailFolder newFolder) throws PimException;
}
