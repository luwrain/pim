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
