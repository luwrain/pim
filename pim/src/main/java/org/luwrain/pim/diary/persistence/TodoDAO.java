// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.pim.diary.persistence;

import java.util.*;

public interface TodoDAO
{
    long add(Todo todo);
    void delete(Todo todo);
    List<Todo> getAll();
    void update(Todo todo);
}
