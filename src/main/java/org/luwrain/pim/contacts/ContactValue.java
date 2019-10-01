/*
   Copyright 2012-2019 Michael Pozhidaev <msp@luwrain.org>
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

package org.luwrain.pim.contacts;

import org.luwrain.core.NullCheck;

public final class ContactValue
{
    static public final int MAIL = 1;
    static public final int ADDRESS = 2;
    static public final int MOBILE_PHONE = 3;
    static public final int GROUND_PHONE = 4;
    static public final int BIRTHDAY = 5;
    static public final int URL = 6;
    static public final int SKYPE = 500;

    private final int type;
    private final String value;
    private final boolean preferable;

    public ContactValue(int type, String value, boolean preferable)
    {
	this.type = type;
	this.value = value;
	this.preferable = preferable;
	NullCheck.notNull(value, "value");
    }

    public int getType()
    {
	return this.type;
    }

    public String getValue()
    {
	return value;
    }

    public boolean isPreferable()
    {
	return preferable;
    }
}
