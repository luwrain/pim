// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>
// Copyright 2015 Roman Volovodov <gr.rPman@gmail.com>

package org.luwrain.pim.mail;

import org.luwrain.core.*;

public interface Settings
{
    static public final String
	PATH = "/org/luwrain/pim/mail";

    static public final int
	DEFAULT_MESSAGE_LINE_LEN = 60;

    String getUserAgent(String defValue);
    void setUserAgent(String value);
    int getMessageLineLen(int defValue);
    void setMessageLineLen(int value);

    static public Settings create(Registry registry)
    {
	registry.addDirectory(PATH);
	return RegistryProxy.create(registry, PATH, Settings.class);
    }
}
