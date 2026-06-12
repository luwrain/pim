// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.diary;

import java.util.*;
import java.time.*;
import org.apache.logging.log4j.*;

import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.app.base.*;
import org.luwrain.controls.*;
import org.luwrain.controls.list.*;
import org.luwrain.controls.edit.*;

import static java.util.Objects.*;
import static org.luwrain.core.DefaultEventResponse.*;
import static org.luwrain.core.events.InputEvent.*;

public class MainLayout extends LayoutBase implements ListArea.ClickHandler<Event>
{
    static private final Logger log = LogManager.getLogger();

    public final List<Event> events = new ArrayList<>();
    public final ListArea<Event> eventsArea;
    public final EditArea notesArea;
    public final CalendarArea calendarArea;
    final Actions eventsActions;
    final App app;

    MainLayout(App app) 
    {
	super(app);
	this.app = app;
	final var s = app.getStrings();

	eventsArea = new ListArea<Event>(listParams(p -> {
		    p.name = s.eventsAreaName();
		    p.model = new ListModel<Event>(events);
		    p.appearance = new EventListAppearance(getControlContext());
		    p.clickHandler = this;
		}));

	eventsActions = actions(
	    action("create", s.create(), new InputEvent(Special.INSERT),
		   () -> { onCreateEvent(); return true; }),
	    action("delete", s.delete(), new InputEvent(Special.DELETE),
		   () -> { onDeleteEvent(); return true; })
	    );

	notesArea = new EditArea(editParams(p -> {
		    p.name = s.notesAreaName();
		    p.appearance = new DefaultEditAreaAppearance(p.context) {
			    @Override public void announceLine(int index, String line)
			    {
				if (line.trim().isEmpty())
				{
				    app.setEventResponse(hint(Hint.EMPTY_LINE));
				    return;
				}
				app.setEventResponse(text(line));
			    }
			};
		}));

	calendarArea = new CalendarArea(getControlContext(), Calendar.getInstance());

	setAreaLayout(AreaLayout.LEFT_RIGHT_BOTTOM,
		      eventsArea, eventsActions,
		      notesArea, null,
		      calendarArea, null);
    }

    @Override public boolean onListClick(ListArea<Event> area, int index, Event event)
    {
	if (event == null)
	    return false;
	app.setEventResponse(listItem(event.getTitle() + " " + app.getStrings().eventListSuffix()));
	notesArea.setText(new String[]{ requireNonNullElse(event.getNotes(), "") });
	//calendarArea.setDate(event.getDateTime() != null
	//			     ? event.getDateTime().toLocalDate()
	//			     : LocalDate.now());
	return true;
    }

    boolean onCreateEvent()
    {
	// TODO: open event creation form
	return true;
    }

    boolean onDeleteEvent()
    {
	final var selected = eventsArea.selected();
	if (selected == null)
	    return false;
	// TODO: confirm and delete
	events.remove(selected);
	eventsArea.refresh();
	app.getLuwrain().playSound(Sounds.DONE);
	return true;
    }

    @Override public AreaLayout getAreaLayout()
    {
	return super.getAreaLayout();
    }

    final class EventListAppearance extends AbstractAppearance<Event>
    {
	EventListAppearance(ControlContext context)
	{
	    super();
	}

	@Override public void announceItem(Event event, Set<Flags> flags)
	{
	    final var s = app.getStrings();
	    if (event == null)
	    {
		app.setEventResponse(hint(Hint.EMPTY_LINE));
		return;
	    }
	    final var sb = new StringBuilder();
	    final var dt = event.getDateTime();
	    if (dt != null)
		sb.append(String.format("%02d:%02d ", dt.getHour(), dt.getMinute()));
	    sb.append(requireNonNullElse(event.getTitle(), ""));
	    if (event.getLocation() != null && !event.getLocation().isEmpty())
		sb.append(", ").append(event.getLocation());
	    app.setEventResponse(listItem(sb.toString() + " " + s.eventListSuffix()));
	}

	@Override public String getScreenAppearance(Event event, Set<Flags> flags)
	{
	    if (event == null)
		return "";
	    final var sb = new StringBuilder();
	    final var dt = event.getDateTime();
	    if (dt != null)
		sb.append(String.format("%02d:%02d ", dt.getHour(), dt.getMinute()));
	    sb.append(requireNonNullElse(event.getTitle(), ""));
	    if (event.isCompleted())
		sb.insert(0, "\u2713 ");
	    return sb.toString();
	}
    }
}
