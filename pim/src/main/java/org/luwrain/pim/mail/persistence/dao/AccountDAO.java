// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>
// Copyright 2015 Roman Volovodov <gr.rPman@gmail.com>

package org.luwrain.pim.mail.persistence.dao;

import java.util.*;
import org.luwrain.pim.mail.persistence.model.*;

public interface AccountDAO
{
    int add(Account account);
    void delete(Account account);
    Account getById(int id);
    List<Account> getAll();
    void update(Account account);
}
