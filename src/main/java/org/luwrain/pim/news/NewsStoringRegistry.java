
package org.luwrain.pim.news;

import java.util.*;

import org.luwrain.core.*;
import org.luwrain.pim.*;

abstract class NewsStoringRegistry implements NewsStoring
{
protected final org.luwrain.pim.RegistryKeys keys = new org.luwrain.pim.RegistryKeys();
    protected Registry registry;

    public NewsStoringRegistry(Registry registry)
    {
	NullCheck.notNull(registry, "registry");
	this.registry = registry;
    }

    @Override public StoredNewsGroup[] loadGroups() throws PimException
    {
	    final LinkedList<StoredNewsGroup> groups = new LinkedList<StoredNewsGroup>();
	    for(String s: registry.getDirectories(keys.newsGroups()))
	    {
		if (s.isEmpty())
		    continue;
		long id;
		try {
		    id = Integer.parseInt(s);
		}
		catch(NumberFormatException e)
		{
		    Log.warning("pim", "news group with bad registry directory:" + s);
		    continue;
		}
		final StoredNewsGroupRegistry g = new StoredNewsGroupRegistry(registry, id);
		g.load();
		    groups.add(g);
	    }
final StoredNewsGroup[] res = groups.toArray(new StoredNewsGroup[groups.size()]);
Arrays.sort(res);
return res;
    }

    @Override public StoredNewsGroup loadGroupById(long id) throws PimException
    {
	final StoredNewsGroupRegistry group = new StoredNewsGroupRegistry(registry, id);
	group.load();
	return group;
    }

    @Override public void saveGroup(NewsGroup group) throws PimException
    {
	NullCheck.notNull(group, "group");
	final int id = Registry.nextFreeNum(registry, keys.newsGroups());
	final String path = Registry.join(keys.newsGroups(), "" + id);
	registry.addDirectory(path);
	final Settings.Group settings = Settings.createGroup(registry, path);
	settings.setName(group.name);
	settings.setOrderIndex(group.orderIndex);
	settings.setExpireDays(group.expireAfterDays);
	settings.setMediaContentType(group.mediaContentType);
    }

    @Override public void deleteGroup(StoredNewsGroup group) throws PimException
    {
	if (!(group instanceof StoredNewsGroupRegistry))
	    return;
	final StoredNewsGroupRegistry groupReg = (StoredNewsGroupRegistry)group;
	groupReg.delete();
    }



    @Override public Object clone()
    {
	return null;
    }
}
