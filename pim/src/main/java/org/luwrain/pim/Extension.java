/*
   Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>
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

import java.nio.file.*;
import com.google.auto.service.*;

import org.luwrain.core.*;

import org.luwrain.pim.mail.*;

@AutoService(org.luwrain.core.Extension.class)
public final class Extension extends EmptyExtension
{
    private MailFactory mailFactory = null;
    private org.luwrain.pim.workers.News newsWorker = null;
    private org.luwrain.pim.workers.Smtp smtpWorker = null;
    private org.luwrain.pim.workers.Pop3 pop3Worker = null;

    @Override public String init(Luwrain luwrain)
    {
	mailFactory = new MailFactory(Paths.get(luwrain.getPath("var:luwrain.pim.mail")));
	this.newsWorker = new org.luwrain.pim.workers.News(luwrain);
	this.smtpWorker = new org.luwrain.pim.workers.Smtp(luwrain);
	this.pop3Worker = new org.luwrain.pim.workers.Pop3(luwrain);
	return null;
    }

    @Override public void close()
    {
	mailFactory.close();
    }

    @Override public ExtensionObject[] getExtObjects(Luwrain luwrain)
    {
	return new ExtensionObject[]{
mailFactory,
newsWorker, smtpWorker, pop3Worker,
	};
    }

    @Override public org.luwrain.cpanel.Factory[] getControlPanelFactories(Luwrain luwrain)
    {
	return new org.luwrain.cpanel.Factory[]{
	    new org.luwrain.settings.mail.Factory(luwrain),
	};
    }
}
