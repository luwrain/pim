
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

        static public org.luwrain.pim.mail.MailStoring getMailStoring(Luwrain luwrain)
    {
	NullCheck.notNull(luwrain, "luwrain");
	if (mailFactory == null)
	    return null;
	return mailFactory.newMailStoring();
    }

    
}
