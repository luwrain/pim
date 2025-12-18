// Copyright 2015 Roman Volovodov <gr.rPman@gmail.com>

package org.luwrain.pim.storage;

import java.util.*;

public interface AbstractStorage<E>
{
    List<? extends E>     getAll();
    void add(E e);
    void remove(E e);
}
