
package org.luwrain.settings.news;

import java.util.*;

import org.luwrain.core.*;
import org.luwrain.cpanel.*;

import org.luwrain.pim.news.*;
import org.luwrain.pim.*;

public class Factory implements org.luwrain.cpanel.Factory
{
    static private final String STRINGS_NAME = "luwrain.settings.news";

    private Luwrain luwrain;
    private Strings strings;
    private NewsStoring storing;
    private SimpleElement newsElement = null;

    public Factory(Luwrain luwrain)
    {
	NullCheck.notNull(luwrain, "luwrain");
	this.luwrain = luwrain;
	final Object o = luwrain.i18n().getStrings(STRINGS_NAME);
	if (o != null && (o instanceof Strings))
	{
	    strings = (Strings)o;
	    newsElement = new SimpleElement(StandardElements.APPLICATIONS, this.getClass().getName());
	}
    }




    @Override public Element[] getElements()
    {
	return null;
    }

    @Override public Section createSection(Element el)
    {
	NullCheck.notNull(el, "el");
	return null;
    }

    private void fillGroupsElements(LinkedList<Element> elements)
    {
	if (storing == null)
	    return;
	try {
	    final StoredNewsGroup[] groups = storing.loadGroups();

	}
	catch(PimException e)
			      {
				  luwrain.crash(e);
			      }

    }
}
