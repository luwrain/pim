/*
   Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

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

package org.luwrain.pim.mail.persistence.dao;

import java.util.*;
import org.luwrain.pim.mail.persistence.model.*;

public interface FolderDAO
{
    int add(Folder folder);
    List<Folder> getAll();
    List<Folder> getChildFolders(Folder folder);
    void update(Folder folder);
    Folder getRoot();
    Folder findFirstByProperty(String propName, String propValue);
}
