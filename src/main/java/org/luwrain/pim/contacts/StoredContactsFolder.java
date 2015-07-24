
package org.luwrain.pim.contacts;

public interface StoredContactsFolder
{
    String getTitle() throws Exception;
    void setTitle(String value) throws Exception;
    int getOrderIndex() throws Exception;
    void setOrderIndex(int value) throws Exception;
}
