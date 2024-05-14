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

//LWR_API 1.0

package org.luwrain.pim;

import org.luwrain.core.*;

public class Connections
{
    static private org.luwrain.pim.mail.Factory mailFactory = null;
    static private org.luwrain.pim.news.Factory newsFactory = null;
    static private org.luwrain.pim.contacts.Factory contactsFactory = null;
    static private org.luwrain.pim.binder.Factory binderFactory = null;

    static void init(Luwrain luwrain)
    {
	NullCheck.notNull(luwrain, "luwrain");
	mailFactory = new org.luwrain.pim.mail.Factory(luwrain);
	newsFactory = new org.luwrain.pim.news.Factory(luwrain);
	contactsFactory = new org.luwrain.pim.contacts.Factory(luwrain);
	binderFactory = new org.luwrain.pim.binder.Factory(luwrain.getRegistry());
    }

    static void close()
    {
			mailFactory.close();
	newsFactory.close();
	contactsFactory.close();
	mailFactory = null;
		newsFactory = null;
		contactsFactory = null;
    }

    static public org.luwrain.pim.news.NewsStoring getNewsStoring(Luwrain luwrain, boolean highPriority)
    {
	NullCheck.notNull(luwrain, "luwrain");
	if (newsFactory == null)
	    return null;
	return newsFactory.newNewsStoring(highPriority);
    }

    /*
    static public org.luwrain.pim.mail.MailStoring getMailStoring(Luwrain luwrain, boolean highPriority)
    {
	NullCheck.notNull(luwrain, "luwrain");
	if (mailFactory == null)
	    return null;
	return mailFactory.newMailStoring(highPriority);
    }
    */

        static public org.luwrain.pim.contacts.ContactsStoring getContactsStoring(Luwrain luwrain, boolean highPriority)
    {
	NullCheck.notNull(luwrain, "luwrain");
	if (contactsFactory == null)
	    return null;
	return contactsFactory.newContactsStoring(highPriority);
    }
}
