
package org.luwrain.pim.mail.script;

import java.util.*;

import org.luwrain.core.*;
import org.luwrain.script.*;
import org.luwrain.pim.*;
import org.luwrain.pim.mail.*;

public final class MessageHookObject extends EmptyHookObject
{
    static final String LOG_COMPONENT = MailHookObject.LOG_COMPONENT;

    final MailMessage message;
    private String[] headers = null;
    private ListHookObject listHookObj = null;

    public MessageHookObject(MailMessage message)
    {
	NullCheck.notNull(message, "message");
	this.message = message;
    }

        public MessageHookObject(StoredMailMessage message)
    {
	NullCheck.notNull(message, "message");
	if (!(message instanceof MailMessage))
	    throw new IllegalArgumentException("Unsupported message instance: " + message.getClass().getName());
	this.message = (MailMessage)message;
    }


    @Override public Object getMember(String name)
    {
	NullCheck.notNull(name, "name");
	switch(name)
	{
	case "subject":
	    return message.subject != null?message.subject:"";
	    	case "from":
		    return message.from != null?new AddressHookObject(message.from):"";
	case "cc":
	    {
		final List<HookObject> res = new LinkedList();
		if (message.cc != null)
		    for(String s: message.cc)
			if (s != null)
			    res.add(new AddressHookObject(s));
		return ScriptUtils.createReadOnlyArray(res.toArray(new HookObject[res.size()]));
	    }
	case "headers":
	    try {
		if (headers == null)
		    headers = extractHeaders(message.getRawMessage());
	    }
	    catch(PimException e)
	    {
		return null;
	    }
	    return ScriptUtils.createReadOnlyArray(headers);
	case "list":
	    if (this.listHookObj == null)
	    {
		try {
	    if (headers == null)
		headers = extractHeaders(message.getRawMessage());
		}
		catch(PimException e)
		{
		    return null;
		}
	    this.listHookObj = new ListHookObject(headers);
	    }
	    return listHookObj;
	    
	default:
	    return super.getMember(name);
	}
    }

    static private String[] extractHeaders(byte[] bytes)
    {
	NullCheck.notNull(bytes, "bytes");
	final String str;
	try {
	    str = new String(bytes, "US-ASCII");
	}
	catch(java.io.UnsupportedEncodingException e)
	{
	    Log.debug(LOG_COMPONENT, "unable to decode a message to US-ASCII:" + e.getClass().getName() + ":" + e.getMessage());
	    return new String[0];
	}
	final String[] lines = str.split("\n", -1);
	final List<String> res = new LinkedList();
	for(String s: lines)
	{
	    if (s.trim().isEmpty())
		break;
	    res.add(s.replaceAll("\r", ""));
	}
	return res.toArray(new String[res.size()]);
    }
}
