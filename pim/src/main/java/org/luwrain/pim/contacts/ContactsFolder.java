// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.pim.contacts;

import lombok.*;

import org.luwrain.core.*;
import org.luwrain.pim.*;

@Data
@NoArgsConstructor
public class ContactsFolder implements Comparable
{
    private String title = "";
    private int orderIndex = 0;

    public boolean isRoot()
    {
	return false;
    }

    @Override public String toString()
    {
	return title;
    }

    @Override public int compareTo(Object o)
    {
	if (o == null || !(o instanceof ContactsFolder))
	    return 0;
	final ContactsFolder folder = (ContactsFolder)o;
	if (orderIndex < folder.orderIndex)
	    return -1;
	if (orderIndex > folder.orderIndex)
	    return 1;
	return 0;
    }

    @Override public boolean equals(Object o)
    {
	if (o == null || !(o instanceof ContactsFolder))
	    return false;
	final ContactsFolder folder = (ContactsFolder)o;
	return title.equals(folder.title);
    }
}
