/*
   Copyright 2012-2017 Michael Pozhidaev <michael.pozhidaev@gmail.com>

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

package org.luwrain.popups.pim;

import java.util.*;

import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.controls.*;
import org.luwrain.popups.*;
import org.luwrain.pim.*;
import org.luwrain.pim.contacts.*;

public class CcEditPopup extends EditableListPopup
{
protected final ContactsStoring storing;
protected final Strings strings;

    public CcEditPopup(Luwrain luwrain, Strings strings, ContactsStoring storing, String[] initialList) throws PimException
    {
	super(luwrain, 
	      makeParams(luwrain, strings.ccEditPopupName(), strings, storing, initialList),
Popups.DEFAULT_POPUP_FLAGS);
	NullCheck.notNull(storing, "storing");
	NullCheck.notNull(strings, "strings");
	this.storing = storing;
	this.strings = strings;
    }

    @Override public boolean onInputEvent(KeyboardEvent event)
    {
	NullCheck.notNull(event, "event");
	if (event.isSpecial() && !event.isModified())
	    switch(event.getSpecial())
	    {
		/*
	    case ENTER:
		if (openSubfolder())
		    return true;
		return super.onInputEvent(event);
		*/
	    }
	return super.onInputEvent(event);
    }

    /*
protected boolean openSubfolder()
    {
	final Object sel = selected();
	if (sel == null)
	    return false;
	if (sel instanceof StoredContact)
	{
	    onContactEntry((StoredContact)sel);
	    return true;
	}
	if (!(sel instanceof StoredContactsFolder))
	    return false;
	final StoredContactsFolder folder = (StoredContactsFolder)sel;
	try {
	    final ChooseMailPopup popup = new ChooseMailPopup(luwrain, strings, storing, folder);
	    luwrain.popup(popup);
	    if (popup.closing.cancelled())
		return true;
	    result = popup.result();
	    closing.doOk();
	}
	catch (PimException e)
	{
	    luwrain.crash(e);
	    return false;
	}
	return true;
    }
    */

    @Override public boolean onOk()
    {
	return true;
    }

@Override public String[] result()
    {
	final int count = editableListModel.getItemCount();
	if (count < 1)
	    return new String[0];
	final List<String> res = new LinkedList<String>();
	for(int i = 0;i < count;++i)
	    res.add(editableListModel.getItem(i).toString());
	return res.toArray(new String[res.size()]);
    }

    static protected EditableListArea.Params makeParams(Luwrain luwrain, String name, Strings strings,
						     ContactsStoring storing, String[] initialList)
    {
	NullCheck.notNull(luwrain, "luwrain");
	NullCheck.notNull(name, "name");
	NullCheck.notNull(strings, "strings");
	NullCheck.notNull(storing, "storing");
	NullCheck.notNullItems(initialList, "initialList");
	final EditableListArea.Params params = new EditableListArea.Params();
	params.context = new DefaultControlEnvironment(luwrain);
	params.name = name;
	params.model = new ListUtils.DefaultEditableModel();
	params.appearance = new ListUtils.DefaultAppearance(params.context);
	return params;
    }
}
