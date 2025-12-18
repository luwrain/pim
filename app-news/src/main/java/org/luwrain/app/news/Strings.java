// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.news;

import java.util.*;
import org.luwrain.core.annotations.*;

@ResourceStrings(langs = { "en", "ru" })
public interface Strings
{
    String appName();
        String groupsAreaName();
    String actionAddGroup();
    String actionDeleteGroup();
    //    String actionFetch();
    String actionHideWithReadOnly();
        String actionShowWithReadOnly();
    
    
    String actionMarkAllAsRead();
    String actionMarkArticle();
    String actionReadArticle();

    String actionUnmarkArticle();

    String articleTitle(String title);
    String articleUrl(String url);
    String groupAddingNameMayNotBeEmpty();
    String groupAddingPopupName();
    String groupAddingPopupPrefix();
    String groupDeletingPopupName();
    String groupDeletingPopupText(String groupName);
    String groupPropertiesAreaName(String groupName);
    String groupPropertiesInvalidOrderIndex();
    String groupPropertiesName();
    String groupPropertiesNameMayNotBeEmpty();
    String groupPropertiesOrderIndex();
    String groupPropertiesUrls();

    String markedPrefix();
    String readPrefix();
    String summaryAreaName();
    String viewAreaName();
}
