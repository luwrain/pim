
package org.luwrain.pim.news;

import org.luwrain.core.*;

interface Settings
{
    interface Group
    {
	String getName(String defValue);
	void setName(String value);
	int getExpireDays(int defValue);
	void setExpireDays(int value);
	int getOrderIndex(int defValue);
	void setOrderIndex(int value);
	String getMediaContentType(String defValue);
	void setMediaContentType(String defValue);
    }

    static Group createGroup(Registry registry, String path)
    {
	NullCheck.notNull(registry, "registry");
	NullCheck.notNull(path, "path");
	return RegistryProxy.create(registry, path, Group.class);
    }
}
