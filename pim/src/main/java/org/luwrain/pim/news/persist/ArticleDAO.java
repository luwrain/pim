// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>
// Copyright 2015 Roman Volovodov <gr.rPman@gmail.com>

package org.luwrain.pim.news.persist;

import java.util.*;

import org.luwrain.pim.*;
import org.luwrain.pim.news.*;

public interface ArticleDAO
{
    List<Article> load(Group group);
    List<Article> loadWithoutRead(Group group);
    void update(Article article);
    void add(Group group, Article article);
    void delete(Group group, Article article);
        Set<String> loadUrisInGroup(Group group);
    List<Integer> countMarkedInGroups(List<Group> groups);
    List<Integer> countNewInGroups(List<Group> groups);
    int countNewInGroup(Group group);
    int countByUriInGroup(Group group, String uri);
}
