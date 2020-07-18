/*
   Copyright 2012-2020 Michael Pozhidaev <msp@luwrain.org>

   This file is part of LUWRAIN.

   LUWRAIN is free software; you can redistribute it and/or
   modify it under the terms of the GNU General Public
   License as published by the Free Software Foundation; either
   version 3 of the License, or (at your option) any later version.

   LUWRAIN is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
   General Public License for more details.
*/

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
	    final MailFolder[] folders;
	    if (parent instanceof Element)
	    {
		final Element el = (Element)parent;
		final MailFolder parentFolder = storing.getFolders().loadById(el.id);
		folders = storing.getFolders().load(parentFolder);
	    } else
	    {
		final MailFolder rootFolder = storing.getFolders().getRoot();
		folders = storing.getFolders().load(rootFolder);
	    }
	    final Element[] res = new Element[folders.length];
	    for(int i = 0;i < folders.length;++i)
		res[i] = new Element(parent, storing.getFolders().getId(folders[i]), folders[i].getTitle());
	    return res;
	}
	catch(PimException e)
	{
	    luwrain.crash(e);
	    return new org.luwrain.cpanel.Element[0];
	}
    }

    public SectionArea createArea(ControlPanel controlPanel, int id)
    {
	NullCheck.notNull(controlPanel, "controlPanel");
	try {
	    return new Area(controlPanel, strings, storing, storing.getFolders().loadById(id));
	}
	catch(PimException e)
	{
	    luwrain.crash(e);
	    return null;
	}
    }

    public Action[] getActions(boolean withDeleting)
    {
	if (withDeleting)
	    return new Action[]{
		new Action("add-mail-folder", strings.addMailFolder(), new InputEvent(InputEvent.Special.INSERT)),
		new Action("delete-mail-folder", strings.deleteMailFolder(), new InputEvent(InputEvent.Special.DELETE)),
	    };
	return new Action[]{
	    new Action("add-mail-folder", strings.addMailFolder(), new InputEvent(InputEvent.Special.INSERT)),
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
		final MailFolder parentFolder;
		if (id < 0)
		    parentFolder = storing.getFolders().getRoot(); else
		    parentFolder = storing.getFolders().loadById(id);
		if (parentFolder == null)
		    throw new PimException("No parent folder");
		final MailFolder newFolder = new MailFolder();
		newFolder.setTitle(newFolderName);
		storing.getFolders().save(parentFolder, newFolder);
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
