// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.pim.contacts.persistence;

import java.util.*;

public interface ContactsFolderDAO
{
    long add(ContactsFolder folder);
    void delete(ContactsFolder folder);
    List<ContactsFolder> getAll();
    List<ContactsFolder> getChildFolders(ContactsFolder folder);
    void update(ContactsFolder folder);
    ContactsFolder getRoot();
}
