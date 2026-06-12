// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.pim.contacts.persistence;

import java.io.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactsFolder implements Serializable
{
    private long id = -1;
    private long parentFolderId = -1;
    private String name = "";

    @Override public boolean equals(Object o)
    {
	if (o != null && o instanceof ContactsFolder f)
	    return id == f.id;
	return false;
    }

    @Override public int hashCode()
    {
	return Long.hashCode(id);
    }

    @Override public String toString()
    {
	return name != null ? name : "";
    }
}
