
package org.luwrain.settings.news;

import java.util.*;

import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.cpanel.*;
import org.luwrain.popups.Popups;
import org.luwrain.pim.news.*;
import org.luwrain.pim.*;

public class Factory implements org.luwrain.cpanel.Factory
{
    private Luwrain luwrain;
    private Strings strings = null;
    private NewsStoring storing = null;
    private SimpleElement newsElement = null;
    private SimpleElement groupsElement = null;
    private SimpleElement storingElement = null;

    public Factory(Luwrain luwrain)
    {
	NullCheck.notNull(luwrain, "luwrain");
	this.luwrain = luwrain;
    }

    @Override public Element[] getElements()
    {
	if (!init())
	    return new Element[0];
	return new Element[]{
	    newsElement,
	    groupsElement,
	    storingElement,
	};
    }

    @Override public Element[] getOnDemandElements(Element parent)
    {
	NullCheck.notNull(parent, "parent");
	if (!initStoring())
	    return new Element[0];
	if (parent.equals(groupsElement))
	{
	    try {
		final StoredNewsGroup[] groups = storing.getGroups().load();
		final Element[] res = new Element[groups.length];
		for(int i = 0;i < groups.length;++i)
		    res[i] = new GroupElement(groupsElement, groups[i].getId(), groups[i].getName());
		return res;
	    }
	    catch(PimException e)
	    {
		luwrain.crash(e);
		return new Element[0];
	    }
	}
    return new Element[0];
    }

@Override public Section createSection(Element el)
{
    NullCheck.notNull(el, "el");
    if (el.equals(newsElement))
	return new SimpleSection(newsElement, "Новости");
    if (el.equals(groupsElement))
	return new SimpleSection(groupsElement, "Группы", null,
				 getGroupsActions(), (controlPanel, event)->onActionEvent(controlPanel, event, 0));
    if (el.equals(storingElement))
	return new SimpleSection(storingElement, "Хранение");
    if (el instanceof GroupElement)
	return new SimpleSection(el, ((GroupElement)el).title(), (controlPanel)->Group.create(controlPanel, storing, ((GroupElement)el).id()),
				 getGroupActions(), (controlPanel, event)->onActionEvent(controlPanel, event, ((GroupElement)el).id()));
    return null;
}

    private Action[] getGroupsActions()
    {
	return new Action[]{
	    new Action("add-news-group", strings.addGroup(), new KeyboardEvent(KeyboardEvent.Special.INSERT)),
	};
    }

    private Action[] getGroupActions()
    {
	return new Action[]{
	    new Action("add-news-group", strings.addGroup(), new KeyboardEvent(KeyboardEvent.Special.INSERT)),
	    new Action("delete-news-group", strings.deleteGroup(), new KeyboardEvent(KeyboardEvent.Special.DELETE)),
	};
    }

    private boolean onActionEvent(ControlPanel controlPanel, ActionEvent event, long arg)
    {
	NullCheck.notNull(controlPanel, "controlPanel");
	NullCheck.notNull(event, "event");
	if (ActionEvent.isAction(event, "add-news-group"))
	{
	    try {
		final NewsGroup group = new NewsGroup();
		group.name = strings.newGroupName();
		storing.getGroups().save(group);
		luwrain.message(strings.newGroupAdded(), Luwrain.MESSAGE_OK);
		controlPanel.refreshSectionsTree();
		return true;
	    }
	    catch(PimException e)
	    {
		luwrain.crash(e);
		return true;
	    }
	}

	if (ActionEvent.isAction(event, "delete-news-group"))
	{
	    try {
		final StoredNewsGroup group = storing.getGroups().loadById(arg);
		if (group == null)
		    return false;
		if (!Popups.confirmDefaultNo(controlPanel.getCoreInterface(), strings.deleteGroupPopupName(), strings.deleteGroupPopupQuestion(group.getName())))
	    return true;
		storing.getGroups().delete(group);
		controlPanel.refreshSectionsTree();
	    return true;
	    }
	    catch(PimException e)
	    {
		luwrain.crash(e);
		return true;
	    }
	}


	return false;
    }

private boolean init()
{
    if (strings == null)
    {
	final Object o = luwrain.i18n().getStrings(Strings.NAME);
	if (o != null && (o instanceof Strings))
	    strings = (Strings)o; else
	    return false;
    }
    if (newsElement == null)
	newsElement = new SimpleElement(StandardElements.APPLICATIONS, this.getClass().getName());
    if (groupsElement == null)
	groupsElement = new SimpleElement(newsElement, this.getClass().getName() + ":Groups");
    if (storingElement == null)
	storingElement = new SimpleElement(newsElement, this.getClass().getName() + ":Storing");
    return true;
}

private boolean initStoring()
{
    if (storing != null)
	return true;
    storing = org.luwrain.pim.Connections.getNewsStoring(luwrain);
    return storing != null;
}
}
