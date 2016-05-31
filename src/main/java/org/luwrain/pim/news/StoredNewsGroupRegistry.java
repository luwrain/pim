
package org.luwrain.pim.news;

import org.luwrain.core.Registry;
import org.luwrain.core.NullCheck;
import org.luwrain.pim.*;

class StoredNewsGroupRegistry implements StoredNewsGroup, Comparable
{
    private Registry registry;
    private Settings.Group settings;

    int id = 0;
    String name = "";
    String[] urls = new String[0];
    String mediaContentType = "";
    int orderIndex = 0;
    int expireAfterDays = 30;

    StoredNewsGroupRegistry(Registry registry, Settings.Group settings)
    {
	NullCheck.notNull(registry, "registry");
	NullCheck.notNull(settings, "settings");
	this.registry = registry;
	this.settings = settings;
    }

    @Override public long getId()
    {
	return id;
}

    @Override public String getName()
    {
	return name;
    }

    @Override public String getMediaContentType()
    {
	return mediaContentType;
    }

    @Override public int getOrderIndex()
    {
	return orderIndex;
    }

    @Override public int getExpireAfterDays()
    {
	return expireAfterDays;
    }

    @Override public String[] getUrls()
    {
	return urls;
    }

    @Override public void setName(String name) throws PimException
    {
	NullCheck.notNull(name, "name");
	try {
	    settings.setName(name);
	    this.name = name;
	}
	catch(Exception e)
	{
	    throw new PimException(e);
	}
    }

    @Override public void setMediaContentType(String value) throws PimException
    {
	NullCheck.notNull(value, "value");
	try {
	    settings.setMediaContentType(value);
	    this.mediaContentType = value;
	}
	catch(Exception e)
	{
	    throw new PimException(e);
	}
    }

    @Override public void setOrderIndex(int index) throws PimException
    {
	try {
	    if (index >= 0)
	    {
		settings.setOrderIndex(index);
		this.orderIndex = index;
	    } else
	    {
		settings.setOrderIndex(0);
		this.orderIndex = 0;
	    }
	}
	catch(Exception e)
	{
	    throw new PimException(e);
	}
    }

    @Override public void setExpireAfterDays(int count) throws PimException
    {
	try {
	    if (count >= 0)
	    {
		settings.setExpireDays(count);
		this.expireAfterDays = count;
	    } else
	    {
		settings.setExpireDays(0);
		this.expireAfterDays = 0;
	    }
	}
	catch(Exception e)
	{
	    throw new PimException(e);
	}
    }

    @Override public void setUrls(String[] urls) throws PimException
    {
	try {
	    //FIXME:
	}
	catch(Exception e)
	{
	    throw new PimException(e);
	}
    }

    @Override public String toString()
    {
	return name;
    }

    @Override public boolean equals(Object o)
    {
	if (o == null || !(o instanceof StoredNewsGroupRegistry))
	    return false;
	return id == ((StoredNewsGroupRegistry)o).id;
    }

    @Override public int compareTo(Object o)
    {
	if (o == null || !(o instanceof StoredNewsGroupRegistry))
	    return 0;
	StoredNewsGroupRegistry g = (StoredNewsGroupRegistry)o;
	if (orderIndex < g.orderIndex)
	    return -1;
	if (orderIndex > g.orderIndex)
	    return 1;
	return 0;
    }
}
