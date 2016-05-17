
package org.luwrain.pim.news;

import java.util.*;

import org.luwrain.core.Registry;
import org.luwrain.core.NullCheck;
import org.luwrain.pim.*;
import org.luwrain.util .*;

abstract class NewsStoringRegistry implements NewsStoring
{
    private final RegistryKeys keys = new RegistryKeys();
    protected Registry registry;

    public NewsStoringRegistry(Registry registry)
    {
	NullCheck.notNull(registry, "registry");
	this.registry = registry;
    }

    @Override public StoredNewsGroup[] loadGroups() throws PimException
    {
	try {
	    final String[] groupsNames = registry.getDirectories(keys.newsGroups());
	    final LinkedList<StoredNewsGroup> groups = new LinkedList<StoredNewsGroup>();
	    for(String s: groupsNames)
	    {
		if (s == null || s.isEmpty())
		    continue;
		final StoredNewsGroupRegistry g = readNewsGroup(RegistryPath.join(keys.newsGroups(), s), s);
		if (g != null)
		    groups.add(g);
	    }
	    return groups.toArray(new StoredNewsGroup[groups.size()]);
	}
	catch(Exception e)
	{
	    throw new PimException(e.getMessage(), e);
	}
    }

    private StoredNewsGroupRegistry readNewsGroup(String path, String name)
    {
	final Settings.Group settings = Settings.createGroup(registry, path);
	final StoredNewsGroupRegistry g = new StoredNewsGroupRegistry(registry, settings);
	    g.id = Integer.parseInt(name.trim());
	    g.name = settings.getName("");
	g.expireAfterDays = settings.getExpireDays(0);
	g.orderIndex = settings.getOrderIndex(0);
	g.mediaContentType = settings.getMediaContentType("");
	if (g.name.isEmpty())
	    return null;
	if (g.expireAfterDays < 0)
	    g.expireAfterDays = 0;
	if (g.orderIndex < 0)
	    g.orderIndex = 0;


	final String[] values = registry.getValues(path);
	final LinkedList<String> urls = new LinkedList<String>();
	for(String s: values)
	{
	    if (s.indexOf("url") < 0 || registry.getTypeOf(path + "/" + s) != Registry.STRING)
		continue;
	    final String value = registry.getString(path + "/" + s);
	    if (!value.trim().isEmpty())
		urls.add(value);
	}
	g.urls = urls.toArray(new String[urls.size()]);
	return g;
    }

    @Override public long getGroupId(StoredNewsGroup group) throws PimException
    {
	NullCheck.notNull(group, "group");
	return ((StoredNewsGroupRegistry)group).id;
    }

    @Override public Object clone()
    {
	return null;
    }
}
