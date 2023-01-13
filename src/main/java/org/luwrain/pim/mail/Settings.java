/*
   Copyright 2012-2023 Michael Pozhidaev <msp@luwrain.org>
   Copyright 2015 Roman Volovodov <gr.rPman@gmail.com>

   This file is part of LUWRAIN.

   LUWRAIN is free software; you can redistribute it and/or
   modify it under the terms of the GNU General Public
   License as published by the Free Software Foundation; either
   version 3 of the License, or (at your option) any later version.

   LUWRAIN is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
   General Public License for more details.
*/

//LWR_API 1.0

package org.luwrain.pim.mail;

import org.luwrain.core.*;

public interface Settings
{
    static public final String
	PATH = "/org/luwrain/pim/mail";

    static public final int
	DEFAULT_MESSAGE_LINE_LEN = 60;

    String getAccounts(String defValue);
    void setAccounts(String value);
    String getFolders(String defValue);
    void setFolders(String value);
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
