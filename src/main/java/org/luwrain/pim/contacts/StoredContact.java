
package org.luwrain.pim.contacts;

import org.luwrain.pim.*;

public interface StoredContact extends StoredCommonAttr
{
    String getTitle() throws PimException;
    void setTitle(String value) throws PimException;
    ContactValue[] getValues() throws PimException;
    void setValues(ContactValue[] values) throws PimException;
}
