// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>
// Copyright 2015 Roman Volovodov <gr.rPman@gmail.com>

package org.luwrain.pim.news.persist;

import java.util.*;

public interface GroupDAO
{
    int add(Group group);
    void delete(Group group);
    List<Group> load();
    void update(Group group);
}
