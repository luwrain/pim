
package org.luwrain.pim.news;

import java.util.*;

import org.luwrain.pim.*;

public interface StoredNewsArticle
{
    int getState();
    void setState(int state) throws PimException;
    String getSourceUrl();
    void setSourceUrl(String sourceUrl) throws PimException;
    String getSourceTitle();
    void setSourceTitle(String sourceTitle) throws PimException;
    String getUri();
    void setUri(String uri) throws PimException;
    String getTitle();
    void setTitle(String title) throws PimException;
    String getExtTitle();
    void setExtTitle(String extTitle) throws PimException;
    String getUrl();
    void setUrl(String url) throws PimException;
    String getDescr();
    void setDescr(String descr) throws PimException;
    String getAuthor();
    void setAuthor(String authro) throws PimException;
    String getCategories();
    void setCategories(String categories) throws PimException;
    Date getPublishedDate();
    void setPublishedDate(Date publishedDate) throws PimException;
    Date getUpdatedDate();
    void setUpdatedDate(Date updatedDate) throws PimException;
    String getContent();
    void setContent(String content) throws PimException;
}
