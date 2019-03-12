/*
   Copyright 2012-2019 Michael Pozhidaev <michael.pozhidaev@gmail.com>
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

package org.luwrain.pim.contacts;

import org.luwrain.core.*;
import org.luwrain.pim.*;

public interface ContactsFolders
{
    StoredContactsFolder getRoot() throws PimException;
    StoredContactsFolder[] load(StoredContactsFolder folder) throws PimException;
    void save(StoredContactsFolder addTo, ContactsFolder folder) throws PimException;
    void delete(StoredContactsFolder folder) throws PimException;
}
