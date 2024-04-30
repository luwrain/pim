/*
   Copyright 2012-2024 Michael Pozhidaev <msp@luwrain.org>

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

package org.luwrain.app.diary;

import java.util.*;
import java.util.concurrent.atomic.*;
import java.io.*;

import org.luwrain.core.*;
//import org.luwrain.core.events.*;
//import org.luwrain.core.queries.*;
import org.luwrain.controls.*;
import org.luwrain.controls.edit.*;
import org.luwrain.app.base.*;

import org.luwrain.pim.diary.persistence.model.*;
import org.luwrain.pim.diary.persistence.model.Event;

import org.luwrain.controls.ListUtils.*;

import static org.luwrain.core.DefaultEventResponse.*;

final class MainLayout extends LayoutBase
{
    private final App app;
    final ListArea<Event> eventsArea;

    MainLayout(App app)
    {
	super(app);
	this.app = app;
	this.eventsArea = new ListArea<Event>(listParams(params -> {
		    params.name = app.getStrings().eventsAreaName();
		    params.appearance  = new EventAppearance();
		}));
		setAreaLayout(eventsArea, null);
    }

    private final class EventAppearance extends AbstractAppearance<Event>
    {
	@Override public void announceItem(Event event, Set<Flags> flags)
	{
	    app.setEventResponse(listItem(event.getTitle(), Suggestions.LIST_ITEM));
	}
	@Override public String getScreenAppearance(Event event, Set<Flags> flags)
	{
	    return event.getTitle();
	}
    }
}
