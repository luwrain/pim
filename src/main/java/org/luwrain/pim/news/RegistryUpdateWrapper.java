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

package org.luwrain.pim.news;

import org.luwrain.core.Registry;

class RegistryUpdateWrapper
{
    public static void setString(Registry registry,
				 String path,
				 String value)  throws RegistryUpdateException
    {
	if (registry == null || path == null || path.trim().isEmpty() || value == null)
	    return;
	if (!registry.setString(path, value))
	    throw new RegistryUpdateException("Registry refuses to set value \'" + value + "\' to " + path);
    }

    public static void setInteger(Registry registry,
				 String path,
				 int value) throws RegistryUpdateException
    {
	if (registry == null || path == null || path.trim().isEmpty())
	    return;
	if (!registry.setInteger(path, value))
	    throw new RegistryUpdateException("Registry refuses to set value \'" + value + "\' to " + path);
    }

    public static void setBoolean(Registry registry,
				 String path,
				 boolean value) throws RegistryUpdateException
    {
	if (registry == null || path == null || path.trim().isEmpty())
	    return;
	if (!registry.setBoolean(path, value))
	    throw new RegistryUpdateException("Registry refuses to set value \'" + value + "\' to " + path);
    }

	    public static void deleteValue(Registry registry, String path) throws RegistryUpdateException
	    {
		if (registry == null || path == null || path.trim().isEmpty())
		    return;
		if (!registry.deleteValue(path))
		    throw new RegistryUpdateException("Registry refuses to delete value " + path);
	    }





}
