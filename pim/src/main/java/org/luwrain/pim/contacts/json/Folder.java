// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.pim.contacts.json;

import java.util.*;

import org.luwrain.core.*;
import org.luwrain.pim.contacts.*;

final class Folder extends ContactsFolder
{
    List<Folder> subfolders = new ArrayList<Folder>();
    List<Contact> contacts = new ArrayList<Contact>();
    private transient Storing storing = null;

    void setStoring(Storing storing)
    {
	NullCheck.notNull(storing, "storing");
	if (subfolders == null)
	    subfolders = new ArrayList<>();
	if (contacts == null)
	    contacts = new ArrayList<>();
	for(Folder f: subfolders)
	    f.setStoring(storing);
	for(Contact c: contacts)
	    c.setStoring(storing);
    }
}
