
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
