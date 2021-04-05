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


       

       
package org.luwrain.pim;

import java.util.*;

import org.luwrain.base.*;
import org.luwrain.core.*;
import org.luwrain.script.hooks.*;

import org.luwrain.pim.mail.*;
import org.luwrain.pim.mail.script.*;

public final class Extension extends org.luwrain.core.extensions.EmptyExtension
{
    private org.luwrain.pim.workers.News newsWorker = null;
    private org.luwrain.pim.workers.Smtp smtpWorker = null;
    private org.luwrain.pim.workers.Pop3 pop3Worker = null;

    @Override public String init(Luwrain luwrain)
    {
	NullCheck.notNull(luwrain, "luwrain");
	Connections.init(luwrain);
	this.newsWorker = new org.luwrain.pim.workers.News(luwrain);
	this.smtpWorker = new org.luwrain.pim.workers.Smtp(luwrain);
	this.pop3Worker = new org.luwrain.pim.workers.Pop3(luwrain);
	return null;
    }

    @Override public org.luwrain.cpanel.Factory[] getControlPanelFactories(Luwrain luwrain)
    {
	NullCheck.notNull(luwrain, "luwrain");
	return new org.luwrain.cpanel.Factory[]{
	    new org.luwrain.settings.mail.Factory(luwrain),
	};
    }

    @Override public ExtensionObject[] getExtObjects(Luwrain luwrain)
    {
	return new ExtensionObject[]{ newsWorker, smtpWorker, pop3Worker };
    }

    @Override public void close()
    {
	Connections.close();
    }
}
