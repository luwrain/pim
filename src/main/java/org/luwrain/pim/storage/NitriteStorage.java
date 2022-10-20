/*
   Copyright 2012-2022 Michael Pozhidaev <msp@luwrain.org>
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

package org.luwrain.pim.storage;

import java.io.*;

import org.dizitart.no2.*;
import org.dizitart.no2.objects.*;

import org.luwrain.core.*;

public final class NitriteStorage<E> implements AutoCloseable
{
    private final File file;
    private final Nitrite db;
    private final ObjectRepository<E> repo;

    public NitriteStorage(File file, Class cl)
    {
	NullCheck.notNull(file, "file");
	NullCheck.notNull(cl, "cl");
	this.file = file;
	this.db = Nitrite.builder()
        .filePath(file)
        .openOrCreate("luwrain", "passwd");
	this.repo = this.db.getRepository(cl);
    }

    @Override public void close()
    {
	db.close();
    }

    public ObjectRepository<E> get()
    {
	return repo;
    }
}
