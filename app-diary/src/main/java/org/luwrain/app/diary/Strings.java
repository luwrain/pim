// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.diary;

import org.luwrain.core.annotations.*;

@ResourceStrings(langs = { "en", "ru" })
public interface Strings
{
    String appName();
    String eventsAreaName();
    String notesAreaName();
    String calendarAreaName();
    String regularEventsAreaName();
    String create();
    String delete();
    String eventListSuffix();
    String noteHintEmpty();
    String createEventPopupName();
    String createEventPopupPrefix();
    String deleteEventPopupName();
    String deleteEventPopupText();

    // Event properties form
    String eventPropertiesAreaName();
    String titleEdit();
    String commentEdit();
    String locationEdit();
    String dateEdit();
    String timeEdit();
    String rruleEdit();
}
