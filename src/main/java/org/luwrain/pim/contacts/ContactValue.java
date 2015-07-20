
package org.luwrain.pim.contacts;

public class ContactValue
{
    public int type;
    public String value = "";
    public boolean preferable = false;

    public ContactValue(int type,
			String value,
boolean preferable)
    {
	this.type = type;
	this.value = value;
	this.preferable = preferable;
	if (value == null)
	    throw new NullPointerException("value may not be null");
    }
}

