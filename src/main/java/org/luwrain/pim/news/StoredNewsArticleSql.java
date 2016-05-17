
package org.luwrain.pim.news;

import java.util.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;

class StoredNewsArticleSql implements StoredNewsArticle, Comparable
{
    Connection con;
    public long id = 0;
    public long groupId = 0;

    public int state = 0;
    public String sourceUrl = "";
    public String sourceTitle = "";
    public String uri = "";
    public String title = "";
    public String extTitle = "";
    public String url = "";
    public String descr = "";
    public String author = "";
    public String categories = "";
    public java.util.Date publishedDate = new java.util.Date();
    public java.util.Date updatedDate = new java.util.Date();
    public String content = "";

    public StoredNewsArticleSql(Connection con)
    {
	this.con = con;
	if (con == null)
	    throw new NullPointerException("con may not be null");
    }

  @Override public   int getState()
    {
	return state;
    }

    @Override public   void setState(int state) throws SQLException
    {
	PreparedStatement st = con.prepareStatement("UPDATE news_article SET state = ? WHERE id = ?;");
	st.setInt(1, state);
	st.setLong(2, id);
	st.executeUpdate();
	this.state = state;
    }

    @Override public   String getSourceUrl()
    {
	return sourceUrl;
    }

    @Override public   void setSourceUrl(String sourceUrl) throws SQLException
    {
	//FIXME:
    }

    @Override public   String getSourceTitle()
    {
	return sourceTitle;
    }

    @Override public   void setSourceTitle(String sourceTitle) throws SQLException
    {
	//FIXME:
    }

    @Override public   String getUri()
    {
	return uri;
    }

    @Override public   void setUri(String uri) throws SQLException
    {
	//FIXME:
    }

    @Override public   String getTitle()
    {
	return title;
    }

    @Override public   void setTitle(String title) throws SQLException
    {
	//FIXME:
    }

    @Override public   String getExtTitle()
    {
	return extTitle;
    }

    @Override public   void setExtTitle(String extTitle) throws SQLException
    {
	//FIXME:
    }

    @Override public   String getUrl()
    {
	return url;
    }

    @Override public   void setUrl(String url) throws SQLException
    {
	//FIXME:
    }

    @Override public   String getDescr()
    {
	return descr;
    }

    @Override public   void setDescr(String descr) throws SQLException
    {
	//FIXME:
    }

    @Override public   String getAuthor()
    {
	return author;
    }

    @Override public   void setAuthor(String authro) throws SQLException
    {
	//FIXME:
    }

    @Override public   String getCategories()
    {
	return categories;
    }

    @Override public   void setCategories(String categories) throws SQLException
    {
	//FIXME:
    }

    @Override public   Date getPublishedDate()
    {
	return publishedDate;
    }

    @Override public   void setPublishedDate(Date publishedDate) throws SQLException
    {
	//FIXME:
    }

    @Override public   Date getUpdatedDate()
    {
	return updatedDate;
    }

    @Override public   void setUpdatedDate(Date updatedDate) throws SQLException
    {
	//FIXME:
    }

    @Override public   String getContent()
    {
	return content;
    }

    @Override public   void setContent(String content) throws SQLException
    {
	//FIXME:
    }

    @Override public int compareTo(Object o)
    {
	if (o == null || !(o instanceof StoredNewsArticleSql))
	    return 0;
	StoredNewsArticleSql article = (StoredNewsArticleSql)o;
	if (state != article.state)
	{
	    if (state > article.state)
		return -1;
	    if (state < article.state)
		return 1;
	    return 0;
	}
	if (publishedDate == null || article.publishedDate == null)
	    return 0;
	return -1 * publishedDate.compareTo(article.publishedDate);
    }
}
