/*
   Copyright 2012-2017 Michael Pozhidaev <michael.pozhidaev@gmail.com>

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

package org.luwrain.pim;

import org.luwrain.core.*;

public class DummyRegistry implements Registry
{
    @Override public boolean addDirectory(String path)
    {
	return false;
    }

    @Override public boolean deleteDirectory(String path)
    {
	return false;
    }

    @Override public boolean deleteValue(String path)
    {
	return false;
    }

    @Override public boolean getBoolean(String path)
    {
	return false;
    }

    @Override public String[] getDirectories(String path)
    {
	return new String[0];
    }

    @Override public int getInteger(String path)
    {
	return 0;
    }

    @Override public String getString(String path)
    {
	return "";
    }

    @Override public String getStringDesignationOfType(int type)
    {
	return "";
    }

    @Override public int getTypeOf(String path)
    {
	return INVALID;
    }

    @Override public String[] getValues(String path)
    {
	return new String[0];
    }

    @Override public boolean hasDirectory(String path)
    {
	return false;
    }

    @Override public boolean hasValue(String path)
    {
	return false;
    }

    @Override public boolean setBoolean(String path, boolean value)
    {
	return false;
    }

    @Override public boolean setInteger(String path, int value)
    {
	return false;
    }

    @Override public boolean setString(String path, String value)
    {
	return false;
    }
}
