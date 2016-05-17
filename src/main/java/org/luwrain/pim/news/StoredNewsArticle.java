
package org.luwrain.pim.news;

import java.util.*;
import java.sql.SQLException;

public interface StoredNewsArticle
{
    int getState();
    void setState(int state) throws SQLException;
    String getSourceUrl();
    void setSourceUrl(String sourceUrl) throws SQLException;
    String getSourceTitle();
    void setSourceTitle(String sourceTitle) throws SQLException;
    String getUri();
    void setUri(String uri) throws SQLException;
    String getTitle();
    void setTitle(String title) throws SQLException;
    String getExtTitle();
    void setExtTitle(String extTitle) throws SQLException;
    String getUrl();
    void setUrl(String url) throws SQLException;
    String getDescr();
    void setDescr(String descr) throws SQLException;
    String getAuthor();
    void setAuthor(String authro) throws SQLException;
    String getCategories();
    void setCategories(String categories) throws SQLException;
    Date getPublishedDate();
    void setPublishedDate(Date publishedDate) throws SQLException;
    Date getUpdatedDate();
    void setUpdatedDate(Date updatedDate) throws SQLException;
    String getContent();
    void setContent(String content) throws SQLException;
}
