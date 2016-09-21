
package org.luwrain.pim.news;

import java.sql.*;
import java.util.*;

import org.luwrain.core.*;
import org.luwrain.pim.*;

class NewsStoringSql extends NewsStoringRegistry
{
    private Connection con;

    public NewsStoringSql(Registry registry, Connection con)
    {
	super(registry);
	this.con = con;
	NullCheck.notNull(con, "con");
    }

    @Override public void saveArticle(StoredNewsGroup newsGroup, NewsArticle article) throws PimException
    {
	try {
	if (newsGroup == null)
	    throw new NullPointerException("newsGrroup may not be null");
	if (!(newsGroup instanceof StoredNewsGroupRegistry))
	    throw new IllegalArgumentException("newsGroup is not an instance of StoredNewsGroupRegistry");
	StoredNewsGroupRegistry g = (StoredNewsGroupRegistry)newsGroup;
	PreparedStatement st = con.prepareStatement("INSERT INTO news_article (news_group_id,state,source_url,source_title,uri,title,ext_title,url,descr,author,categories,published_date,updated_date,content) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?);");
	st.setLong(1, g.id);
	st.setInt(2, NewsArticle.NEW);
	st.setString(3, article.sourceUrl);
	st.setString(4, article.sourceTitle);
	st.setString(5, article.uri);
	st.setString(6, article.title);
	st.setString(7, article.extTitle);
	st.setString(8, article.url);
	st.setString(9, article.descr);
	st.setString(10, article.author);
	st.setString(11, article.categories);
	st.setDate(12, new java.sql.Date(article.publishedDate.getTime()));
	st.setDate(13, new java.sql.Date(article.updatedDate.getTime()));
	st.setString(14, article.content);
	st.executeUpdate();
	}
	catch(Exception e)
	{
	    throw new PimException(e);
	}
    }

    @Override public     StoredNewsArticle[] loadArticlesInGroup(StoredNewsGroup newsGroup) throws PimException
    {
	try {
	StoredNewsGroupRegistry g = (StoredNewsGroupRegistry)newsGroup;
	PreparedStatement st = con.prepareStatement("SELECT id,news_group_id,state,source_url,source_title,uri,title,ext_title,url,descr,author,categories,published_date,updated_date,content FROM news_article WHERE news_group_id = ?;");
	st.setLong(1, g.id);
	ResultSet rs = st.executeQuery();
	Vector<StoredNewsArticleSql> articles = new Vector<StoredNewsArticleSql>();
	while (rs.next())
	{
	    StoredNewsArticleSql a = new StoredNewsArticleSql(con);
	    a.id = rs.getLong(1);
	    a.groupId = rs.getLong(2);
	    a.state = rs.getInt(3);
	    a.sourceUrl = rs.getString(4).trim();
	    a.sourceTitle = rs.getString(5).trim();
	    a.uri = rs.getString(6).trim();
	    a.title = rs.getString(7).trim();
	    a.extTitle = rs.getString(8).trim();
	    a.url = rs.getString(9).trim();
	    a.descr = rs.getString(10).trim();
	    a.author = rs.getString(11).trim();
	    a.categories = rs.getString(12).trim();
	    a.publishedDate = new java.util.Date(rs.getTimestamp(13).getTime());
	    a.updatedDate = new java.util.Date(rs.getTimestamp(14).getTime());
	    a.content = rs.getString(15).trim();
	    articles.add(a);
	}
	return articles.toArray(new StoredNewsArticle[articles.size()]);
	}
	catch(Exception e)
	{
	    throw new PimException(e);
	}
    }

    @Override public     StoredNewsArticle[] loadArticlesInGroupWithoutRead(StoredNewsGroup newsGroup) throws PimException
    {
	try {
	StoredNewsGroupRegistry g = (StoredNewsGroupRegistry)newsGroup;
	PreparedStatement st = con.prepareStatement("SELECT id,news_group_id,state,source_url,source_title,uri,title,ext_title,url,descr,author,categories,published_date,updated_date,content FROM news_article WHERE news_group_id = ? AND state <> 1;");
	st.setLong(1, g.id);
	ResultSet rs = st.executeQuery();
	Vector<StoredNewsArticleSql> articles = new Vector<StoredNewsArticleSql>();
	while (rs.next())
	{
	    StoredNewsArticleSql a = new StoredNewsArticleSql(con);
	    a.id = rs.getLong(1);
	    a.groupId = rs.getLong(2);
	    a.state = rs.getInt(3);
	    a.sourceUrl = rs.getString(4).trim();
	    a.sourceTitle = rs.getString(5).trim();
	    a.uri = rs.getString(6).trim();
	    a.title = rs.getString(7).trim();
	    a.extTitle = rs.getString(8).trim();
	    a.url = rs.getString(9).trim();
	    a.descr = rs.getString(10).trim();
	    a.author = rs.getString(11).trim();
	    a.categories = rs.getString(12).trim();
	    a.publishedDate = new java.util.Date(rs.getTimestamp(13).getTime());
	    a.updatedDate = new java.util.Date(rs.getTimestamp(14).getTime());
	    a.content = rs.getString(15).trim();
	    articles.add(a);
	}
	return articles.toArray(new StoredNewsArticle[articles.size()]);
	}
	catch(Exception e)
	{
	    throw new PimException(e);
	}
    }

    @Override public int countArticlesByUriInGroup(StoredNewsGroup newsGroup, String uri) throws PimException
    {
	try {
	StoredNewsGroupRegistry g = (StoredNewsGroupRegistry)newsGroup;
	PreparedStatement st = con.prepareStatement("SELECT count(*) FROM news_article WHERE news_group_id = ? AND uri = ?;");
	st.setLong(1, g.id);
	st.setString(2, uri);
	ResultSet rs = st.executeQuery();
	if (!rs.next())
	    return 0;
	return rs.getInt(1);
	}
	catch(Exception e)
	{
	    throw new PimException(e);
	}
    }

    @Override public int countNewArticleInGroup(StoredNewsGroup group) throws PimException
    {
	try {
	if (group == null)
	    return 0;
	if (!(group instanceof StoredNewsGroupRegistry))
	    return 0;
	StoredNewsGroupRegistry g = (StoredNewsGroupRegistry)group;
	PreparedStatement st = con.prepareStatement("SELECT count(*) FROM news_article WHERE news_group_id=? AND state=?;");
	st.setLong(1, g.id);
	st.setLong(2, NewsArticle.NEW);
	ResultSet rs = st.executeQuery();
	if (!rs.next())
	    return 0;
	return rs.getInt(1);
	}
	catch(Exception e)
	{
	    throw new PimException(e);
	}
    }

    @Override public int[] countNewArticlesInGroups(StoredNewsGroup[] groups) throws PimException
    {
	try {
	if (groups == null)
	    throw new NullPointerException("groups may not be null");
	StoredNewsGroupRegistry[] g = new StoredNewsGroupRegistry[groups.length];
	for(int i =- 0;i < groups.length;++i)
	{
	    if (groups[i] == null)
		throw new NullPointerException("groups[" + i + "] may not be null");
	    if (!(groups[i] instanceof StoredNewsGroupRegistry))
		throw new IllegalArgumentException("groups[" + i + "] must be an instance of StoredNewsGroupRegistry");
	    g[i] = (StoredNewsGroupRegistry)groups[i];
	}
	int[] res = new int[g.length];
	for(int i = 0;i < res.length;++i)
	    res[i] = 0;
	Statement st = con.createStatement();
	ResultSet rs = st.executeQuery("select news_group_id,count(*) from news_article where state=0 group by news_group_id;");
	while (rs.next())
	{
	    final long id = rs.getLong(1);
	    final int count = rs.getInt(2);
	    int k = 0;
	    while (k < g.length && g[k].id != id)
		++k;
	    if (k < g.length)
		res[k] = count;
	}
	return res;
	}
	catch(Exception e)
	{
	    throw new PimException(e);
	}
    }

    @Override public int[] countMarkedArticlesInGroups(StoredNewsGroup[] groups) throws PimException
    {
	try {
	if (groups == null)
	    throw new NullPointerException("groups may not be null");
	StoredNewsGroupRegistry[] g = new StoredNewsGroupRegistry[groups.length];
	for(int i =- 0;i < groups.length;++i)
	{
	    if (groups[i] == null)
		throw new NullPointerException("groups[" + i + "] may not be null");
	    if (!(groups[i] instanceof StoredNewsGroupRegistry))
		throw new IllegalArgumentException("groups[" + i + "] must be an instance of StoredNewsGroupRegistry");
	    g[i] = (StoredNewsGroupRegistry)groups[i];
	}
	int[] res = new int[g.length];
	for(int i = 0;i < res.length;++i)
	    res[i] = 0;
	Statement st = con.createStatement();
	ResultSet rs = st.executeQuery("select news_group_id,count(*) from news_article where state=2 group by news_group_id;");
	while (rs.next())
	{
	    final long id = rs.getLong(1);
	    final int count = rs.getInt(2);
	    int k = 0;
	    while (k < g.length && g[k].id != id)
		++k;
	    if (k < g.length)
		res[k] = count;
	}
	return res;
	}
	catch(Exception e)
	{
	    throw new PimException(e.getMessage(), e);
	}
    }

    @Override public Set<String> loadArticleUrisInGroup(StoredNewsGroup group) throws PimException
    {
	NullCheck.notNull(group, "group");
	return null;
    }
}
