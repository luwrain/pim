
package org.luwrain.pim.news;

import java.util.*;

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

    StoredNewsGroupRegistry(Registry registry, long id)
    {
	NullCheck.notNull(registry, "registry");
	this.registry = registry;
	this.id = (int)id;
	this.settings = Settings.createGroup(registry, getPath());
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
	    settings.setName(name);
	    this.name = name;
    }

    @Override public void setMediaContentType(String value) throws PimException
    {
	NullCheck.notNull(value, "value");
	    settings.setMediaContentType(value);
	    this.mediaContentType = value;
    }

    @Override public void setOrderIndex(int index) throws PimException
    {
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

    @Override public void setExpireAfterDays(int count) throws PimException
    {
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

    @Override public void setUrls(String[] urls) throws PimException
    {
	NullCheck.notNullItems(urls, "urls");

	final String[] values = registry.getValues(getPath());
	final LinkedList<String> old = new LinkedList<String>();
	for(String s: values)
	{
	    final String path = Registry.join(getPath(), s);
	    if (s.indexOf("url") < 0 || registry.getTypeOf(path) != Registry.STRING)
		continue;
old.add(path);
	}
	for (String s: old)
	    registry.deleteValue(s);
	int k = 1;
	for(String s: urls)
	    if (!s.trim().isEmpty())
		registry.setString(Registry.join(getPath(), "url" + (k++)), s);
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

    void load()
    {
	final String path = getPath();
name = settings.getName("");
expireAfterDays = settings.getExpireDays(0);
orderIndex = settings.getOrderIndex(0);
mediaContentType = settings.getMediaContentType("");
	if (expireAfterDays < 0)
	    expireAfterDays = 0;
	if (orderIndex < 0)
	    orderIndex = 0;
	final String[] values = registry.getValues(path);
	final LinkedList<String> urls = new LinkedList<String>();
	for(String s: values)
	{
	    if (s.indexOf("url") < 0 || registry.getTypeOf(Registry.join(path, s)) != Registry.STRING)
		continue;
	    final String value = registry.getString(path + "/" + s);
	    if (!value.trim().isEmpty())
		urls.add(value);
	}
	this.urls = urls.toArray(new String[urls.size()]);
    }

    boolean delete()
    {
	return registry.deleteDirectory(getPath());
    }

    private String getPath()
    {
	return Registry.join(Settings.GROUPS_PATH, "" + id);
    }
}
