/*
   Copyright 2012-2019 Michael Pozhidaev <michael.pozhidaev@gmail.com>
   Copyright 2015 Roman Volovodov <gr.rPman@gmail.com>

   This file is part of LUWRAIN.

   LUWRAIN is free software; you can redistribute it and/or
   modify it under the terms of the GNU General Public
   License as published by the Free Software Foundation; either
   version 3 of the License, or (at your option) any later version.

   LUWRAIN is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
   General Public License for more details.
*/

//LWR_API 1.0

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
