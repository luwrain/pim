
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
	    final StoredMailFolder[] folders;
	    if (parent instanceof Element)
	    {
		final Element el = (Element)parent;
		final StoredMailFolder parentFolder = storing.loadFolderById(el.id);
		folders = storing.getFolders(parentFolder);
} else
	    {
	    final StoredMailFolder rootFolder = storing.getFoldersRoot();
	    folders = storing.getFolders(rootFolder);
	    }
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
				     return new Action[]{
					 new Action("add-mail-folder", strings.addMailFolder(), new KeyboardEvent(KeyboardEvent.Special.INSERT)),
					 new Action("delete-mail-folder", strings.deleteMailFolder(), new KeyboardEvent(KeyboardEvent.Special.DELETE)),
				     };
    }

public boolean onActionEvent(ControlPanel controlPanel, ActionEvent event, int id)
    {
	NullCheck.notNull(controlPanel, "controlPanel");
	NullCheck.notNull(event, "event");
	//adding
	if (ActionEvent.isAction(event, "add-mail-folder"))
	{
	    final String newFolderName = Popups.simple(luwrain, strings.newMailFolderPopupName(), strings.newMailFolderPopupPrefix(), "");
	    if (newFolderName == null || newFolderName.isEmpty())
		return true;
	    try {
	    final StoredMailFolder parentFolder;
	    if (id < 0)
		parentFolder = storing.getFoldersRoot(); else
		parentFolder = storing.loadFolderById(id);
	    if (parentFolder == null)
		throw new PimException("No parent folder");
	    final MailFolder newFolder = new MailFolder();
	    newFolder.title = newFolderName;
	    storing.saveFolder(parentFolder, newFolder);
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
}
