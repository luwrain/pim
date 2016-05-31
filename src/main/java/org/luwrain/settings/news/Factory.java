
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
    private NewsStoring storing = null;
    private SimpleElement newsElement = null;
    private SimpleElement groupsElement = null;
    private SimpleElement storingElement = null;

    public Factory(Luwrain luwrain)
    {
	NullCheck.notNull(luwrain, "luwrain");
	this.luwrain = luwrain;
	final Object o = luwrain.i18n().getStrings(STRINGS_NAME);
	if (o != null && (o instanceof Strings))
	{
	    strings = (Strings)o;
	}
	    newsElement = new SimpleElement(StandardElements.APPLICATIONS, this.getClass().getName());
	    groupsElement = new SimpleElement(newsElement, this.getClass().getName() + ":Groups");
storingElement = new SimpleElement(newsElement, this.getClass().getName() + ":Storing");
    }

    @Override public Element[] getElements()
    {
	return new Element[]{
	    newsElement,
	    groupsElement,
	    storingElement,
	};
    }

    @Override public Element[] getOnDemandElements(Element parent)
    {
	if (parent.equals(groupsElement))
	{
	    final LinkedList<Element> res = new LinkedList<Element>();
	    fillGroupsElements(res);
	    //	    System.out.println("res " + res.size());
	    return res.toArray(new Element[res.size()]);
	}
	return new Element[0];
    }

    @Override public Section createSection(Element el)
    {
	NullCheck.notNull(el, "el");
	if (el.equals(newsElement))
	    return new SimpleSection(newsElement, "Новости");
	if (el.equals(groupsElement))
	    return new SimpleSection(groupsElement, "Группы");
	if (el.equals(storingElement))
	    return new SimpleSection(storingElement, "Хранение");
	if (el instanceof GroupElement)
	{
	    final GroupElement groupElement = (GroupElement)el;
	    return new SimpleSection(el, groupElement.getGroup().getName());
	}
	return null;
    }

    private void fillGroupsElements(LinkedList<Element> elements)
    {
	NullCheck.notNull(elements, "elements");
	if (!connect())
	    return;
	try {
	    final StoredNewsGroup[] groups = storing.loadGroups();
	    for(StoredNewsGroup g: groups)
		elements.add(new GroupElement(groupsElement, g));
	}
	catch(PimException e)
			      {
				  luwrain.crash(e);
			      }

    }

    private boolean connect()
    {
	if (storing != null)
	    return true;
	final Object f =  luwrain.getSharedObject("luwrain.pim.news");
	if (f == null || !(f instanceof org.luwrain.pim.news.Factory))
	    return false;
	storing = ((org.luwrain.pim.news.Factory)f).createNewsStoring();
	return storing != null;
    }
}
