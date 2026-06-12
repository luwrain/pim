// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.pim.contacts.persistence;

import java.util.*;

public interface ContactDAO
{
    long add(Contact contact);
    void delete(Contact contact);
    List<Contact> getAll();
    void update(Contact contact);
}
