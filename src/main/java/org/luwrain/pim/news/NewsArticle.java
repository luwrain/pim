
package org.luwrain.pim.news;

import java.util.*;

public class NewsArticle
{
    public static final int NEW = 0;
    public static final int READ = 1;
    public static final int MARKED = 2;

    public String sourceUrl = "";
    public String sourceTitle = "";
    public String uri = "";
    public String title = "";
    public String extTitle = "";
    public String url = "";
    public String descr = "";
    public String author = "";
    public String categories = "";
    public Date publishedDate = new Date();
    public Date updatedDate = new Date();
    public String content = "";
}
