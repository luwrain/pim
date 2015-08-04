
package org.luwrain.pim.mail;

public interface StoredMailFolder
{
    String getTitle() throws Exception;
    void setTitle(String value) throws Exception;
    int getOrderIndex() throws Exception;
    void setOrderIndex(int value) throws Exception;
}
