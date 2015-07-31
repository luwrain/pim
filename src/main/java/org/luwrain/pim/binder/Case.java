
package org.luwrain.pim.binder;

import org.luwrain.pim.CommonAttr;

public class Case extends CommonAttr
{
    public static final int ACTUAL = 0;

    public String title;
    public int status = ACTUAL;
    public int priority = 0;
}
