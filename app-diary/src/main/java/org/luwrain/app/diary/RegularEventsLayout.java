// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.diary;

import java.util.*;
import org.apache.logging.log4j.*;

import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.app.base.*;
import org.luwrain.controls.*;
import org.luwrain.controls.list.*;

import org.luwrain.pim.diary.persistence.Event;

import static org.luwrain.core.DefaultEventResponse.*;
import static org.luwrain.core.events.InputEvent.*;

public class RegularEventsLayout extends LayoutBase implements ListArea.ClickHandler<Event>
{
    static private final Logger log = LogManager.getLogger();

    public final List<Event> regularEvents = new ArrayList<>();
    public final ListArea<Event> regularEventsArea;
    final Actions regularEventsActions;
    final App app;

    RegularEventsLayout(App app)
    {
	super(app);
	this.app = app;
	final var s = app.getStrings();

	regularEventsArea = new ListArea<Event>(listParams(p -> {
		    p.name = s.regularEventsAreaName();
		    p.model = new ListModel<Event>(regularEvents);
		    p.appearance = new RegularEventListAppearance(getControlContext());
		    p.clickHandler = this;
		}));

	regularEventsActions = actions(
	    action("create", s.create(), new InputEvent(Special.INSERT),
		   () -> { onCreateRegularEvent(); return true; }),
	    action("delete", s.delete(), new InputEvent(Special.DELETE),
		   () -> { onDeleteRegularEvent(); return true; })
	    );

	setAreaLayout(regularEventsArea, regularEventsActions);

	loadRegularEvents();
    }

    @Override public boolean onListClick(ListArea<Event> area, int index, Event event)
    {
	if (event == null)
	    return false;
	app.setEventResponse(listItem(requireNonNullElse(event.getTitle(), "")));
	return true;
    }

    boolean onCreateRegularEvent()
    {
	final var title = app.conv.newEventTitle();
	if (title == null || title.isEmpty())
	    return true;
	final var event = new Event();
	event.setTitle(title);
	// A regular event has no specific date; use rrule or leave dtStart as now
	event.setDtStart(System.currentTimeMillis());
	app.persist.getEventDAO().add(event);
	loadRegularEvents();
	app.getLuwrain().playSound(Sounds.DONE);
	return true;
    }

    boolean onDeleteRegularEvent()
    {
	final var selected = regularEventsArea.selected();
	if (selected == null)
	    return false;
	final var title = requireNonNullElse(selected.getTitle(), "");
	if (!app.conv.confirmDeleteEvent(title))
	    return true;
	app.persist.getEventDAO().delete(selected);
	loadRegularEvents();
	app.getLuwrain().playSound(Sounds.DONE);
	return true;
    }

    void loadRegularEvents()
    {
	regularEvents.clear();
	final var allEvents = app.persist.getEventDAO().getAll();
	if (allEvents == null || allEvents.isEmpty())
	{
	    regularEventsArea.refresh();
	    return;
	}
	// Regular events are those with an rrule set (recurring events)
	for (var ev : allEvents)
	{
	    if (ev.getRrule() != null && !ev.getRrule().isEmpty())
		regularEvents.add(ev);
	}
	regularEventsArea.refresh();
    }

    final class RegularEventListAppearance extends AbstractAppearance<Event>
    {
	RegularEventListAppearance(ControlContext context)
	{
	    super();
	}

	@Override public void announceItem(Event event, Set<Flags> flags)
	{
	    if (event == null)
	    {
		app.setEventResponse(hint(Hint.EMPTY_LINE));
		return;
	    }
	    final var sb = new StringBuilder();
	    sb.append(requireNonNullElse(event.getTitle(), ""));
	    if (event.getComment() != null && !event.getComment().isEmpty())
		sb.append(": ").append(event.getComment());
	    app.setEventResponse(listItem(sb.toString()));
	}

	@Override public String getScreenAppearance(Event event, Set<Flags> flags)
	{
	    if (event == null)
		return "";
	    return requireNonNullElse(event.getTitle(), "");
	}
    }

    static private String requireNonNullElse(String s, String def)
    {
	return s != null ? s : def;
    }
}
