
package org.luwrain.pim.binder;

import org.luwrain.pim.StoredCommonAttr;

public interface StoredCase extends StoredCommonAttr
{
    String getTitle() throws Exception;
    void setTitle(String value) throws Exception;
    int getStatus() throws Exception;
    void setStatus(int value) throws Exception;
    int getPriority() throws Exception;
    void setPriority(int value) throws Exception;
}
