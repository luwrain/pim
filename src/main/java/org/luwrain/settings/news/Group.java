
package org.luwrain.settings.news;

import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.controls.*;
import org.luwrain.cpanel.*;
import org.luwrain.pim.*;
import org.luwrain.pim.news.*;

class Group extends FormArea implements SectionArea
{
    private ControlPanel controlPanel;
    private Luwrain luwrain;
    private Strings strings;
    private NewsStoring storing;
    private StoredNewsGroup group;

    Group(ControlPanel controlPanel, Strings strings,
	  NewsStoring storing, StoredNewsGroup group) throws PimException
    {
	super(new DefaultControlEnvironment(controlPanel.getCoreInterface()), strings.groupFormName());
	NullCheck.notNull(controlPanel, "controlPanel");
	NullCheck.notNull(strings, "strings");
	NullCheck.notNull(storing, "storing");
	NullCheck.notNull(group, "group");
	this.controlPanel = controlPanel;
	this.strings = strings;
	this.group = group;
	this.storing = storing;
	this .luwrain = controlPanel.getCoreInterface();
	fillForm();
    }

    private void fillForm() throws PimException
    {
	addEdit("name", strings.groupFormTitle(), group.getName());
	final String[] urls = group.getUrls();
	final StringBuilder b = new StringBuilder();
	for(String s: urls)
	    b.append(s + "\n");
	activateMultilineEdit("", new String(b));
    }

    @Override public boolean saveSectionData()
    {
	try {
	    group.setName(getEnteredText("name"));
	    group.setUrls(getMultilineEditText().split("\n", -1));
	    return true;
	}
	catch(PimException e)
	{
	    luwrain.crash(e);
	    return true;
	}
    }

    @Override public boolean onInputEvent(KeyboardEvent event)
    {
	NullCheck.notNull(event, "event");
	if (controlPanel.onInputEvent(event))
	    return true;
	return super.onInputEvent(event);
    }

    @Override public boolean onSystemEvent(EnvironmentEvent event)
    {
	NullCheck.notNull(event, "event");
	if (controlPanel.onSystemEvent(event))
	    return true;
	return super.onSystemEvent(event);
    }

    static Group create(ControlPanel controlPanel,
			    NewsStoring storing, long id)
    {
	NullCheck.notNull(controlPanel, "controlPanel");
	NullCheck.notNull(storing, "storing");
	final Luwrain luwrain = controlPanel.getCoreInterface();
	final Strings strings = (Strings)luwrain.i18n().getStrings(Strings.NAME);
	try {
	    return new Group(controlPanel, strings, storing, storing.getGroups().loadById((int)id));
	}
	catch(PimException e)
	{
	    luwrain.crash(e);
	    return null;
	}
    }
}
