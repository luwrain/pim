/*
   Copyright 2012-2019 Michael Pozhidaev <michael.pozhidaev@gmail.com>
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

import org.luwrain.app.fetch.Base.Type;

public final class Extension extends org.luwrain.core.extensions.EmptyExtension
{
    private org.luwrain.pim.workers.News newsWorker = null;
        private org.luwrain.pim.workers.Smtp smtpWorker = null;
            private org.luwrain.pim.workers.Pop3 pop3Worker = null;

    private org.luwrain.pim.mail.sql.FolderUniRefProc mailFolderUniRefProc;

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

	@Override public UniRefProc[] getUniRefProcs(Luwrain luwrain)
	{
	    if (mailFolderUniRefProc == null)
		mailFolderUniRefProc = new org.luwrain.pim.mail.sql.FolderUniRefProc(luwrain);
	    return new UniRefProc[]{
		mailFolderUniRefProc,
	    };
    }

    @Override public Command[] getCommands(Luwrain luwrain)
    {
	return new Command[]{
	    new SimpleShortcutCommand("fetch"),

	    	    new Command(){
		@Override public String getName()
		{
		    return "wizard-mail";
		}
		@Override public void onCommand(Luwrain luwrain)
		{
		    NullCheck.notNull(luwrain, "luwrain");
		    new org.luwrain.pim.wizards.Mail(luwrain).start();
		}
	    },

	};
    }

    @Override public ExtensionObject[] getExtObjects(Luwrain luwrain)
    {
	return new ExtensionObject[]{

	    newsWorker, smtpWorker, pop3Worker,

	    new Shortcut() {
		@Override public String getExtObjName()
		{
		    return "fetch";
		}
		@Override public Application[] prepareApp(String[] args)
		{
		    NullCheck.notNull(args, "args");
		    if (args.length == 0)
			return new Application[]{new org.luwrain.app.fetch.App(EnumSet.of(Type.NEWS, Type.INCOMING_MAIL, Type.OUTGOING_MAIL))};
		    if (args.length > 1)
			return null;
		    final String arg = args[0];
		    Set<Type> type;
		    switch(arg)
		    {
		    case "--ALL":
			type = EnumSet.of(Type.NEWS, Type.INCOMING_MAIL, Type.OUTGOING_MAIL);
			break;
		    case "--NEWS":
			type = EnumSet.of(Type.NEWS);
			break;
		    case "--MAIL":
			type = EnumSet.of(Type.INCOMING_MAIL, Type.OUTGOING_MAIL);
			break;
		    case "--INCOMING-MAIL":
			type = EnumSet.of(Type.INCOMING_MAIL);
			break;
		    case "--OUTGOING-MAIL":
			type = EnumSet.of(Type.OUTGOING_MAIL);
			break;
		    default:
			return new Application[0];
		    };
		    return new Application[]{new org.luwrain.app.fetch.App(type)};
		}
	    },

	};
    }

@Override public void close()
    {
	Connections.close();
    }
}
