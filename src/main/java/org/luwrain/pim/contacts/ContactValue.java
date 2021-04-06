/*
   Copyright 2012-2021 Michael Pozhidaev <msp@luwrain.org>
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
    public enum Type { MAIL, ADDRESS, PHONE, BIRTHDAY, URL, SKYPE };

    private final Type type;
    private final String value;
    private final boolean preferable;

    public ContactValue(Type type, String value, boolean preferable)
    {
	NullCheck.notNull(type, "type");
		NullCheck.notNull(value, "value");
	this.type = type;
	this.value = value;
	this.preferable = preferable;
    }

    public Type getType()
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
