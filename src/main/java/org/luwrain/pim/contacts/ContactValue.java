
package org.luwrain.pim.contacts;

import org.luwrain.core.NullCheck;

public class ContactValue
{
    static public final int MAIL = 1;
    static public final int ADDRESS = 2;
    static public final int MOBILE_PHONE = 3;
    static public final int GROUND_PHONE = 4;
    static public final int BIRTHDAY = 5;
    static public final int URL = 6;

    static public final int SKYPE = 500;

    public int type;
    public String value = "";
    public boolean preferable = false;

    public ContactValue(int type, String value,
			boolean preferable)
    {
	this.type = type;
	this.value = value;
	this.preferable = preferable;
	NullCheck.notNull(value, "value");
    }
}
