
package org.luwrain.pim.email;

public interface StoredEmailFolder
{
    String getTitle() throws Exception;
    void setTitle(String value) throws Exception;
    int getOrderIndex() throws Exception;
    void setOrderIndex(int value) throws Exception;
}
