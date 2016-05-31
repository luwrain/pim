
package org.luwrain.pim.news;

import org.luwrain.pim.*;

public interface StoredNewsGroup
{
    long getId();
    String getName();
    void setName(String name) throws PimException;
    String[] getUrls();
    void setUrls(String[] urls) throws PimException;
    String getMediaContentType();
    void setMediaContentType(String value) throws PimException;
    int getOrderIndex();
    void setOrderIndex(int index) throws PimException;
    int getExpireAfterDays();
    void setExpireAfterDays(int count) throws PimException;
}
