
package org.luwrain.pim.news;

import org.luwrain.pim.*;

public interface NewsStoring extends Cloneable
{
    StoredNewsGroup[] loadGroups() throws PimException;
    StoredNewsGroup loadGroupById(long id) throws PimException;
    void saveGroup(NewsGroup group) throws PimException;
    void deleteGroup(StoredNewsGroup group) throws PimException;
    //    long getGroupId(StoredNewsGroup group) throws PimException;

    void saveNewsArticle(StoredNewsGroup newsGroup, NewsArticle article) throws PimException;
    StoredNewsArticle[] loadNewsArticlesOfGroup(StoredNewsGroup newsGroup) throws PimException;
    StoredNewsArticle[] loadNewsArticlesInGroupWithoutRead(StoredNewsGroup newsGroup) throws PimException;
    int countArticlesByUriInGroup(StoredNewsGroup newsGroup, String uri) throws PimException;
    int countNewArticleInGroup(StoredNewsGroup group) throws PimException;
    int[] countNewArticlesInGroups(StoredNewsGroup[] groups) throws PimException;
    int[] countMarkedArticlesInGroups(StoredNewsGroup[] groups) throws PimException;

    Object clone();
}
