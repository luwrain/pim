
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
