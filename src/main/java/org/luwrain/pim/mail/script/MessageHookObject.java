
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

    @Override public Object getMember(String name)
    {
	NullCheck.notNull(name, "name");
	try {
	    switch(name)
	    {
	    case "subject":
		return message.getSubject ();
	    case "from":
		return new AddressHookObject(message.getFrom());
	    case "cc":
		{
		    final List<HookObject> res = new LinkedList();
		    for(String s: message.getCc())
			if (s != null)
			    res.add(new AddressHookObject(s));
		    return ScriptUtils.createReadOnlyArray(res.toArray(new HookObject[res.size()]));
		}
	    case "headers":
		if (headers == null)
		    headers = extractHeaders(message.getRawMessage());
		return ScriptUtils.createReadOnlyArray(headers);
	    case "list":
		if (this.listHookObj == null)
		{
		    if (headers == null)
			headers = extractHeaders(message.getRawMessage());
		    this.listHookObj = new ListHookObject(headers);
		}
		return listHookObj;
	    default:
		return super.getMember(name);
	    }
	}
	catch(PimException e)
	{
	    Log.warning(LOG_COMPONENT, "unable to get the \'" + name + "\' member of the message hook object:" + e.getClass().getName() + ":" + e.getMessage());
	    return null;
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
