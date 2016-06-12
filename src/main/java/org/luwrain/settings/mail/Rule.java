
package org.luwrain.settings.mail;

import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.controls.*;
import org.luwrain.cpanel.*;
import org.luwrain.pim.mail.*;

class Rule extends FormArea implements SectionArea
{
    private ControlPanel controlPanel;
    private MailStoring storing;
    private StoredMailRule rule;

    Rule(ControlPanel controlPanel,
	 MailStoring storing, 
	 StoredMailRule rule) throws Exception
    {
	super(new DefaultControlEnvironment(controlPanel.getCoreInterface()));
	this.storing = storing;
	this.rule = rule;
	this.controlPanel = controlPanel;
	NullCheck.notNull(storing, "storing");
	NullCheck.notNull(rule, "rule");
	NullCheck.notNull(controlPanel, "controlPanel");
	addEdit("header-regex", "Регулярное выражение для заголовка:", rule.getHeaderRegex(), null, true);
	addUniRef("dest-folder-uniref", "Почтовая группа:", rule.getDestFolderUniRef(), null, true);
    }

    void save() throws Exception
    {
	rule.setHeaderRegex(getEnteredText("header-regex"));
	final UniRefInfo uniRefInfo = getUniRefInfo("dest-folder-uniref");
	if (uniRefInfo != null)
	    rule.setDestFolderUniRef(uniRefInfo.toString()); else
	    rule.setDestFolderUniRef("");
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
}
