/*
   Copyright 2012-2018 Michael Pozhidaev <michael.pozhidaev@gmail.com>

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

package org.luwrain.pim.fetching;

import org.luwrain.core.*;

public class Base
{
    protected final Control control;
    protected final Luwrain luwrain;
    protected final Registry registry;
    protected final Strings strings;

    public Base(Control control, Strings strings)
    {
	NullCheck.notNull(control, "control");
	NullCheck.notNull(strings, "strings");
	this.control = control;
	this.strings = strings;
	this.luwrain = control.luwrain();
	this.registry = luwrain.getRegistry();
    }

    protected void message(String text)
    {
	NullCheck.notNull(text, "text");
	control.message(text);
    }

    protected void checkInterrupted() throws InterruptedException
    {
	control.checkInterrupted();
    }

    protected void crash(Exception e)
    {
	NullCheck.notNull(e, "e");
	luwrain.crash(e);
    }
}
