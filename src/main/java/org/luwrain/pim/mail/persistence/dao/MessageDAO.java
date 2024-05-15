/*
   Copyright 2012-2024 Michael Pozhidaev <msp@luwrain.org>

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

public interface MessageDAO
{
    void add(MessageMetadata message);
    void delete(MessageMetadata message);
    List<MessageMetadata> getAll();
        List<MessageMetadata> getByFolderId(int folderId);
    void update(MessageMetadata message);
}
