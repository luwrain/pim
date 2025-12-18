// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.pim.contacts;

import org.luwrain.pim.*;

public interface ContactsStoring
{
    ContactsFolders getFolders();
    Contacts getContacts();
}
