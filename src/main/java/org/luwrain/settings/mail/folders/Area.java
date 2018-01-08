
package org.luwrain.settings.mail.folders;

import java.util.*;

import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.controls.*;
import org.luwrain.popups.Popups;
import org.luwrain.cpanel.*;
import org.luwrain.pim.*;
import org.luwrain.pim.mail.*;
import org.luwrain.settings.mail.*;

class Area extends FormArea implements SectionArea
{
    private final ControlPanel controlPanel;
    private final Luwrain luwrain;
    private final MailStoring storing;
    private final StoredMailFolder folder;
    private final Strings strings;

    Area(ControlPanel controlPanel, Strings strings,
	 MailStoring storing, StoredMailFolder folder) throws PimException
    {
	super(new DefaultControlEnvironment(controlPanel.getCoreInterface()), strings.folderFormName(folder.getTitle()));
	NullCheck.notNull(controlPanel, "controlPanel");
	NullCheck.notNull(strings, "strings");
	NullCheck.notNull(storing, "storing");
	NullCheck.notNull(folder, "folder");
	this.storing = storing;
	this.strings = strings;
	this.folder = folder;
	this.controlPanel = controlPanel;
	this.luwrain = controlPanel.getCoreInterface();
	fillForm();
    }

    private void fillForm() throws PimException
    {
	addEdit("title", "Имя почтовой группы:", folder.getTitle());
	addEdit("order-index", "Индекс сортировки:", "" + folder.getOrderIndex());
    }

    @Override public boolean saveSectionData()
    {
	try {
	    final int orderIndex;
	    try {
		String value = getEnteredText("order-index");
		if (value.trim().isEmpty())
		    value = "0";
		orderIndex = Integer.parseInt(value);
	    }
	    catch(NumberFormatException e)
	    {
		luwrain.message(strings.mailFolderFormBadOrderIndex(getEnteredText("order-index")), Luwrain.MessageType.ERROR);
		return false;
	    }
	    folder.setTitle(getEnteredText("title"));
	    folder.setOrderIndex(orderIndex);
	    return true;
	}
	catch(PimException e)
	{
	    luwrain.crash(e);
	    return false;
	}
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
}
