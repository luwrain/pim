// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.pim.diary.persistence;

import java.util.*;

//import org.luwrain.pim.diary.persistence.model.*;

public interface EventDAO
{
        long add(Event event);
    void delete(Event event);
    List<Event> getAll();
    void update(Event event);
}
