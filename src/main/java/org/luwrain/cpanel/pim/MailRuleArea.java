
package org.luwrain.cpanel.pim;

import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.controls.*;
import org.luwrain.cpanel.*;
import org.luwrain.pim.mail.*;

class MailRuleArea extends FormArea implements SectionArea
{
    private Environment environment;
    private MailStoring storing;
    private StoredMailRule rule;

    MailRuleArea(Environment environment,
	 MailStoring storing, 
	 StoredMailRule rule) throws Exception
    {
	super(new DefaultControlEnvironment(environment.getLuwrain()));
	this.storing = storing;
	this.rule = rule;
	this.environment = environment;
	NullCheck.notNull(storing, "storing");
	NullCheck.notNull(rule, "rule");
	NullCheck.notNull(environment, "environment");
	addEdit("header-regex", "Регулярное выражение для заголовка:", rule.getHeaderRegex(), null, true);
	addUniRef("dest-folder-uniref", "Почтовая группа:", rule.getDestFolderUniRef(), null, true);
    }

    @Override public boolean onKeyboardEvent(KeyboardEvent event)
    {
	NullCheck.notNull(event, "event");
	if (event.isSpecial() && !event.isModified())
	    switch(event.getSpecial())
	    {
	    case TAB:
		environment.gotoSectionsTree();
	    }
	return super.onKeyboardEvent(event);
    }

    void save() throws Exception
    {
	rule.setHeaderRegex(getEnteredText("header-regex"));
	final UniRefInfo uniRefInfo = getUniRefInfo("dest-folder-uniref");
	if (uniRefInfo != null)
	    rule.setDestFolderUniRef(uniRefInfo.toString()); else
	    rule.setDestFolderUniRef("");
    }

    @Override public boolean onEnvironmentEvent(EnvironmentEvent event)
    {
	NullCheck.notNull(event, "event");
	switch(event.getCode())
	{
	case CLOSE:
	    environment.close();
	    return true;
	default:
	    return super.onEnvironmentEvent(event);
	}
    }

    @Override public String getAreaName()
    {
	return "Настройка правила фильтрации";//FIXME:
    }
}
