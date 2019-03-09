
package org.luwrain.pim.mail.script;

import org.luwrain.core.*;
import org.luwrain.script.*;
import org.luwrain.pim.*;
import org.luwrain.pim.mail.*;

public final class ListHookObject extends EmptyHookObject
{
    static private final String LOG_COMPONENT = MessageHookObject.LOG_COMPONENT;
    static private final String HEADER_ID = "list-id:";

    private final String id;
    private final String name;

    ListHookObject(String[] headers)
    {
	NullCheck.notNullItems(headers, "headers");
	String idValue = null;
	for(String s: headers)
	    if (s.toLowerCase().startsWith(HEADER_ID))
	{
	    idValue = s.substring(HEADER_ID.length());
	    break;
	}
	if (idValue == null || idValue.trim().isEmpty())
	{
	    this.id = "";
	    this.name = "";
	    return;
	}
	final String idStr = AddressUtils.getAddress(idValue).trim();
	if (!idStr.isEmpty())
	    this.id = idStr; else
	    this.id = idValue.trim();
	this.name = AddressUtils.getPersonal(idValue);
    }

        @Override public Object getMember(String name)
    {
	NullCheck.notNull(name, "name");
	switch(name)
	{
	case "id":
	    return this.id;
	case "name":
	    return this.name;
	    	default:
	    return super.getMember(name);
	}
    }
}
