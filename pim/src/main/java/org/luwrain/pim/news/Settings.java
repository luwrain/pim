// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>
// Copyright 2015 Roman Volovodov <gr.rPman@gmail.com>

package org.luwrain.pim.news;

import org.luwrain.core.*;

public interface Settings
{
    static public final String
	PATH = "/org/luwrain/pim/news";

    String getGroups(String defValue);
    void setGroups(String value);
    int getNextGroupId(int defValue);
    void setNextGroupId(int value);

    static public Settings create(Registry registry)
    {
	NullCheck.notNull(registry, "registry");
	return RegistryProxy.create(registry, PATH, Settings.class);
    }
}
