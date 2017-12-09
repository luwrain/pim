
package org.luwrain.pim.news;

import java.util.*;

import org.luwrain.pim.*;
import org.luwrain.pim.news.*;

public interface NewsArticles
{
    void save(StoredNewsGroup newsGroup, NewsArticle article) throws PimException;
    StoredNewsArticle[] load(StoredNewsGroup newsGroup) throws PimException;
    StoredNewsArticle[] loadWithoutRead(StoredNewsGroup newsGroup) throws PimException;
    Set<String> loadUrisInGroup(StoredNewsGroup group) throws PimException;
    int countByUriInGroup(StoredNewsGroup newsGroup, String uri) throws PimException;
    int countNewInGroup(StoredNewsGroup group) throws PimException;
    int[] countNewInGroups(StoredNewsGroup[] groups) throws PimException;
    int[] countMarkedInGroups(StoredNewsGroup[] groups) throws PimException;
}
