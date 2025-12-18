// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.pim;

import java.util.*;

/** Common operations for any data access object (DAO).*/
public interface BasicDAO<E>
{
    void add(E e);
    List<E> getAll();
    void update(E e);
}
