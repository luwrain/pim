
package org.luwrain.pim.news;

import java.util.*;

import org.luwrain.pim.*;

public interface NewsStoring extends Cloneable
{
    NewsGroups getGroups();
    NewsArticles getArticles();
    Object clone();
}
