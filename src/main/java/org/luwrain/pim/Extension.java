/*
   Copyright 2012-2015 Michael Pozhidaev <michael.pozhidaev@gmail.com>
   Copyright 2015 Roman Volovodov <gr.rPman@gmail.com>

   This file is part of the LUWRAIN.

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

import org.luwrain.core.*;

public class Extension extends org.luwrain.core.extensions.EmptyExtension
{
    private Registry registry;

    private org.luwrain.pim.mail.Factory mailFactory;
    private org.luwrain.pim.news.Factory newsFactory;
    private org.luwrain.pim.contacts.Factory contactsFactory;
    private org.luwrain.pim.binder.Factory binderFactory;

    @Override public String init(Luwrain luwrain)
    {
	this.registry = luwrain.getRegistry();
	mailFactory = new org.luwrain.pim.mail.Factory(registry);
	newsFactory = new org.luwrain.pim.news.Factory(registry);
	contactsFactory = new org.luwrain.pim.contacts.Factory(registry);
	binderFactory = new org.luwrain.pim.binder.Factory(registry);
	return null;
    }

    @Override public SharedObject[] getSharedObjects(Luwrain luwrain)
    {
	final org.luwrain.pim.mail.Factory m = mailFactory;
	final org.luwrain.pim.news.Factory n = newsFactory;
	final org.luwrain.pim.contacts.Factory c = contactsFactory;
	final org.luwrain.pim.binder.Factory b = binderFactory;

	return new SharedObject[]{

	    new SharedObject(){
		@Override public String getName()
		{
		    return "luwrain.pim.mail";
		}
		@Override public Object getSharedObject()
		{
		    return mailFactory;
		}
	    },

	    new SharedObject(){
		@Override public String getName()
		{
		    return "luwrain.pim.news";
		}
		@Override public Object getSharedObject()
		{
		    return n;
		}
	    },

	    new SharedObject(){
		@Override public String getName()
		{
		    return "luwrain.pim.contacts";
		}
		@Override public Object getSharedObject()
		{
		    return c;
		}
	    },

	    new SharedObject(){
		@Override public String getName()
		{
		    return "luwrain.pim.binder";
		}
		@Override public Object getSharedObject()
		{
		    return b;
		}
	    }};
    }
}
