// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.tasks;

import org.luwrain.core.annotations.*;

@ResourceStrings(langs = { "en", "ru" })
public interface Strings
{
    String appName();
    String tasksAreaName();
    String create();
    String delete();
    String taskListSuffix();
    String createTaskPopupName();
    String createTaskPopupPrefix();
    String deleteTaskPopupName();
    String deleteTaskPopupText();
    String defaultTaskListName();

    // Task properties form
    String taskPropertiesAreaName(String title);
    String taskPropertiesTitle();
    String taskPropertiesDescr();
    String taskPropertiesDueDate();
    String taskPropertiesStatus();
    String taskPropertiesPriority();
    String taskPropertiesTags();
    String taskPropertiesContent();
    String taskPropertiesTitleCannotBeEmpty();
    String taskPropertiesInvalidDueDate();
}

