
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
    private final MailFolder folder;
    private final Strings strings;

    Area(ControlPanel controlPanel, Strings strings,
	 MailStoring storing, MailFolder folder) throws PimException
    {
	super(new DefaultControlContext(controlPanel.getCoreInterface()), strings.folderFormName(folder.getTitle()));
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
}
