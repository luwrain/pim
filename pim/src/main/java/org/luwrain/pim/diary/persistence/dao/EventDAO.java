// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.pim.diary.persistence.dao;

import java.util.*;

import org.luwrain.pim.diary.persistence.model.*;

public interface EventDAO
{
    void add(Event event);
    List<Event> getAll();
}
