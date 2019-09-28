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

public class ChooseMailPopup extends ListPopup2
{
protected final ContactsStoring storing;
protected final Strings strings;

    public ChooseMailPopup(Luwrain luwrain, Strings strings, ContactsStoring storing, StoredContactsFolder folder) throws PimException
    {
	super(luwrain, 
	      constructParams(luwrain, strings.chooseMailPopupName(folder.toString()), strings, storing, folder),Popups.DEFAULT_POPUP_FLAGS);
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
	    case ENTER:
		if (openSubfolder())
		    return true;
		return super.onInputEvent(event);
	    }
	return super.onInputEvent(event);
    }

protected boolean openSubfolder()
    {
	final Object sel = selected();
	if (sel == null)
	    return false;
	if (sel instanceof Contact)
	{
	    onContactEntry((Contact)sel);
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

    @Override public boolean onOk()
    {
	return result != null;
    }

@Override public String result()
    {
	return result.toString();
    }

protected void onContactEntry(Contact contact)
    {
	try {
	    final LinkedList<String> addrs = new LinkedList<String>();
	    final ContactValue values[] = contact.getValues();
	    if (values != null)
		for(ContactValue v: values)
		    if (v.type == ContactValue.MAIL && !v.value.trim().isEmpty())
			addrs.add(v.value.trim());
	    if (addrs.isEmpty())
	    {
		luwrain.message(strings.contactDoesntHaveMail(contact.getTitle()), Luwrain.MessageType.ERROR);
		return ;
	    }
	    if (addrs.size() == 1)
	    {
		result = contact.getTitle() + " <" + addrs.getFirst() + ">";
		closing.doOk();
		return;
	    }
	    final String[] toOffer = addrs.toArray(new String[addrs.size()]);
	    final String r = (String)Popups.fixedList(luwrain, strings.chooseMailForContactPopupName(contact.getTitle()), toOffer);
	    if (r == null)
		return;
	    result = contact.getTitle() + " <" + r + ">";
	    closing.doOk();
	    return;
	}
	catch(PimException e)
	{
	    luwrain.crash(e);
	    return;
	}
    }

    static protected ListArea.Params constructParams(Luwrain luwrain, String name, Strings strings,
						     ContactsStoring storing, StoredContactsFolder folder)
    {
	NullCheck.notNull(luwrain, "luwrain");
	NullCheck.notNull(name, "name");
	NullCheck.notNull(strings, "strings");
	NullCheck.notNull(storing, "storing");
	NullCheck.notNull(folder, "folder");
	final ListArea.Params params = new ListArea.Params();
	params.context = new DefaultControlContext(luwrain);
	params.name = name;
params.model = new Model(storing, folder);
params.appearance = new Appearance(luwrain, strings);
	return params;
    }

    static protected class Model implements ListArea.Model
    {
protected final ContactsStoring storing;
protected final StoredContactsFolder folder;
protected Object[] items;

	Model(ContactsStoring storing, StoredContactsFolder folder)
	{
	    NullCheck.notNull(storing, "storing");
	    NullCheck.notNull(folder, "folder");
	    this.storing = storing;
	    this.folder = folder;
	    refresh();
	}

	@Override public int getItemCount()
	{
	    return items != null?items.length:0;
	}

	@Override public Object getItem(int index)
	{
	    return items != null && index < items.length?items[index]:null;
	}

	@Override public void refresh()
	{
	    try {
		final StoredContactsFolder[] folders = storing.getFolders().load(folder);
		final Contact[] contacts = storing.getContacts().load(folder);
		final List res = new LinkedList();
		for(StoredContactsFolder f: folders)
		    res.add(f);
		for(Contact c: contacts)
		    res.add(c);
		items = res.toArray(new Object[res.size()]);
	    }
	    catch (Exception e)
	    {
		e.printStackTrace();
		items = new Object[0];
	    }
	}
    }

    static protected class Appearance implements ListArea.Appearance
    {
protected final Luwrain luwrain;
protected final Strings strings;

	Appearance(Luwrain luwrain, Strings strings)
	{
	    NullCheck.notNull(luwrain, "luwrain");
	    NullCheck.notNull(strings, "strings");
	    this.luwrain = luwrain;
	    this.strings = strings;
	}

	@Override public void announceItem(Object item, Set<Flags> flags)
	{
	    NullCheck.notNull(item, "item");
	    NullCheck.notNull(flags, "flags");
	    luwrain.playSound(Sounds.LIST_ITEM);
	    if (item instanceof StoredContactsFolder)
	    {
		final StoredContactsFolder folder = (StoredContactsFolder)item;
		try {
		    final String title = folder.getTitle();
		    luwrain.speak(title + " группа");
		    return;
		}
		catch(PimException e)
		{
		    luwrain.crash(e);
		    return;
		}
	    }
	    luwrain.speak(item.toString());
	}

	@Override public String getScreenAppearance(Object item, Set<Flags> flags)
	{
	    NullCheck.notNull(item, "item");
	    NullCheck.notNull(flags, "flags");
	    return item.toString();
	}

	@Override public int getObservableLeftBound(Object item)
	{
	    return 0;
	}

	@Override public int getObservableRightBound(Object item)
	{
	    return item != null?item.toString().length():0;
	}
    }
}
