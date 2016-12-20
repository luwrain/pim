
package org.luwrain.settings.mail.folders;

import java.util.*;

import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.popups.Popups;
import org.luwrain.cpanel.*;
import org.luwrain.pim.mail.*;
import org.luwrain.pim.*;
import org.luwrain.settings.mail.*;

public class Folders
{
    private final Luwrain luwrain;
    private final Strings strings;
    private final MailStoring storing;

    public Folders(Luwrain luwrain, Strings strings,
		   MailStoring storing)
    {
	NullCheck.notNull(luwrain, "luwrain");
	NullCheck.notNull(strings, "strings");
	NullCheck.notNull(storing, "storing");
	this.luwrain = luwrain;
	this.strings = strings;
	this.storing = storing;
    }

    public org.luwrain.cpanel.Element[] getElements(org.luwrain.cpanel.Element parent)
    {
	try {
	    final StoredMailFolder rootFolder = storing.getFoldersRoot();
	    final StoredMailFolder[] folders = storing.getFolders(rootFolder);
	    final Element[] res = new Element[folders.length];
	    for(int i = 0;i < folders.length;++i)
		res[i] = new Element(parent, storing.getFolderId(folders[i]), folders[i].getTitle());
	    return res;
	}
	catch(PimException e)
	{
	    luwrain.crash(e);
	    return new org.luwrain.cpanel.Element[0];
	}
    }

public Area createArea(ControlPanel controlPanel, long id)
    {
	/*
	NullCheck.notNull(controlPanel, "controlPanel");
	final Luwrain luwrain = controlPanel.getCoreInterface();
	try {
	    return new Area(controlPanel, strings, storing, storing.loadAccountById(id));
	}
	catch(PimException e)
	{
	    luwrain.crash(e);
	    return null;
	}
	*/
	return null;
    }

    public Action[] getActions()
    {
	return new Action[0];
	/*
				     return new Action[]{
					 new Action("add-mail-account", strings.addMailAccount(), new KeyboardEvent(KeyboardEvent.Special.INSERT)),
					 new Action("add-mail-account-predefined", strings.addAccountPredefined()),
					 new Action("delete-mail-account", strings.deleteAccount(), new KeyboardEvent(KeyboardEvent.Special.DELETE)),
				     };
	*/
    }

public boolean onActionEvent(ControlPanel controlPanel, ActionEvent event, long id)
    {
	NullCheck.notNull(controlPanel, "controlPanel");
	NullCheck.notNull(event, "event");
	//adding
	//	if (ActionEvent.isAction(event, "add-mail-account"))
	return false;
    }
}
