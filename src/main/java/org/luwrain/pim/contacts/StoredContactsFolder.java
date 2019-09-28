
package org.luwrain.pim.contacts;

import org.luwrain.pim.*;

public interface StoredContactsFolder
{
    String getTitle() throws PimException;
    void setTitle(String value) throws PimException;
    int getOrderIndex() throws PimException;
    void setOrderIndex(int value) throws PimException;
    boolean isRoot();
    }
