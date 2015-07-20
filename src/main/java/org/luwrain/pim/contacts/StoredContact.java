
package org.luwrain.pim.contacts;

import org.luwrain.pim.StoredCommonAttr;

public interface StoredContact extends StoredCommonAttr
{
    ContactValue[] getValues() throws Exception;
    void setValues(ContactValue[] values) throws Exception;
}
