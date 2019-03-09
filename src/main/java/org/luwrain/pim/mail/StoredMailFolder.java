
package org.luwrain.pim.mail;

import org.luwrain.pim.*;

public interface StoredMailFolder
{
    String getTitle() throws PimException;
    void setTitle(String value) throws PimException;
    int getOrderIndex() throws PimException;
    void setOrderIndex(int value) throws PimException;
}
