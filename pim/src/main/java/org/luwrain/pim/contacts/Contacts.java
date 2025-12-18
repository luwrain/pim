// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.pim.contacts;

import org.luwrain.pim.*;

public interface Contacts
{
    Contact[] load(ContactsFolder folder);
    void save(ContactsFolder folder, org.luwrain.pim.contacts.Contact contact);
    void delete(org.luwrain.pim.contacts.Contact contact);
}
