
package org.luwrain.pim.mail.script;


import org.luwrain.core.*;
import org.luwrain.script.*;
import org.luwrain.pim.*;
import org.luwrain.pim.mail.*;

public final class MessageHookObject extends EmptyHookObject
{
    private final MailMessage message;

    public MessageHookObject(MailMessage message)
    {
	NullCheck.notNull(message, "message");
	this.message = message;
    }

    @Override public Object getMember(String name)
    {
	NullCheck.notNull(name, "name");
	switch(name)
	{
	case "subject":
	    return message.subject != null?message.subject:"";
	default:
	    return super.getMember(name);
	}
    }
}
