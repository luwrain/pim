
package org.luwrain.settings.news;

import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.controls.*;
import org.luwrain.cpanel.*;
import org.luwrain.pim.news.*;

class GroupArea extends FormArea implements SectionArea
{
    private ControlPanel controlPanel;
    private NewsStoring storing;
    private StoredNewsGroup group;

    GroupArea(ControlPanel controlPanel, NewsStoring storing,
	      StoredNewsGroup group)
    {
	super(new DefaultControlEnvironment(controlPanel.getCoreInterface()));
	NullCheck.notNull(controlPanel, "controlPanel");
	NullCheck.notNull(storing, "storing");
	NullCheck.notNull(group, "group");
	this.controlPanel = controlPanel;
	this.group = group;
	this.storing = storing;
	fillForm();
    }

    private void fillForm()
    {
	addEdit("name", "Название:", group.getName());
    }

    @Override public boolean onKeyboardEvent(KeyboardEvent event)
    {
	NullCheck.notNull(event, "event");
	if (controlPanel.onKeyboardEvent(event))
	    return true;
	return super.onKeyboardEvent(event);
    }

    @Override public boolean onEnvironmentEvent(EnvironmentEvent event)
    {
	NullCheck.notNull(event, "event");
	if (controlPanel.onEnvironmentEvent(event))
	    return true;
	return super.onEnvironmentEvent(event);
    }

    @Override public boolean saveSectionData()
    {
	return true;
    }

    static GroupArea create(ControlPanel controlPanel,
			    NewsStoring storing, StoredNewsGroup group)
    {
	NullCheck.notNull(controlPanel, "controlPanel");
	NullCheck.notNull(storing, "storing");
	NullCheck.notNull(group, "group");
	return new GroupArea(controlPanel, storing, group);
    }
}
