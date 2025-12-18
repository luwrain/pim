// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.pim.contacts;

import org.luwrain.core.*;
import org.luwrain.pim.*;

public interface ContactsFolders
{
    ContactsFolder getRoot();
    ContactsFolder[] load(ContactsFolder folder);
    void save(ContactsFolder addTo, ContactsFolder folder);
    void delete(ContactsFolder folder);
}
