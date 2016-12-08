
package org.luwrain.pim;

import org.luwrain.core.*;

public class Extension extends org.luwrain.core.extensions.EmptyExtension
{
    static public final String CONTACTS_SHARED_OBJECT = "luwrain.pim.contacts";
    static public final String MAIL_SHARED_OBJECT = "luwrain.pim.mail";

    private org.luwrain.pim.mail.Factory mailFactory;
    private org.luwrain.pim.news.Factory newsFactory;
    private org.luwrain.pim.contacts.Factory contactsFactory;
    private org.luwrain.pim.binder.Factory binderFactory;

    private org.luwrain.pim.mail.FolderUniRefProc mailFolderUniRefProc;

    @Override public String init(Luwrain luwrain)
    {
	NullCheck.notNull(luwrain, "luwrain");
	mailFactory = new org.luwrain.pim.mail.Factory(luwrain);
	newsFactory = new org.luwrain.pim.news.Factory(luwrain);
	contactsFactory = new org.luwrain.pim.contacts.Factory(luwrain.getRegistry());
	binderFactory = new org.luwrain.pim.binder.Factory(luwrain.getRegistry());
	return null;
    }

    @Override public SharedObject[] getSharedObjects(Luwrain luwrain)
    {
	return new SharedObject[]{

	    new SharedObject(){
		@Override public String getName()
		{
		    return MAIL_SHARED_OBJECT;
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
		    return newsFactory;
		}
	    },

	    new SharedObject(){
		@Override public String getName()
		{
		    return "luwrain.pim.contacts";
		}
		@Override public Object getSharedObject()
		{
		    return contactsFactory;
		}
	    },

	    new SharedObject(){
		@Override public String getName()
		{
		    return "luwrain.pim.binder";
		}
		@Override public Object getSharedObject()
		{
		    return binderFactory;
		}
	    }};
    }

    @Override public org.luwrain.cpanel.Factory[] getControlPanelFactories(Luwrain luwrain)
    {
	NullCheck.notNull(luwrain, "luwrain");
	return new org.luwrain.cpanel.Factory[]{
	    new org.luwrain.settings.news.Factory(luwrain),
	    new org.luwrain.settings.mail.Factory(luwrain),
	};
    }

	@Override public UniRefProc[] getUniRefProcs(Luwrain luwrain)
	{
	    if (mailFolderUniRefProc == null)
		mailFolderUniRefProc = new org.luwrain.pim.mail.FolderUniRefProc(luwrain);
	    return new UniRefProc[]{
		mailFolderUniRefProc,
	    };
    }

    @Override public void close()
    {
	mailFactory.close();
	newsFactory.close();
	contactsFactory.close();
    }
}
