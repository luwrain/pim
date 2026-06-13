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
import org.luwrain.popups.*;
import org.luwrain.app.diary.layouts.*;

import static java.util.Objects.*;
import static org.luwrain.core.DefaultEventResponse.*;
import static org.luwrain.core.events.InputEvent.*;

import org.luwrain.pim.diary.persistence.Event;

public class MainLayout extends LayoutBase implements ListArea.ClickHandler<Event>, CalendarArea.ChangeListener
{
    static private final Logger log = LogManager.getLogger();

    public final List<Event> events = new ArrayList<>();
    public final ListArea<Event> eventsArea;
    public final EditArea notesArea;
    public final CalendarArea calendarArea;
    final App app;

    MainLayout(App app) 
    {
	super(app);
	this.app = app;
	final var s = app.getStrings();

	eventsArea = new ListArea<Event>(listParams(p -> {
		    p.name = s.eventsAreaName();
		    p.model = new ListModel<>(events);
		    p.appearance = new EventListAppearance(getControlContext());
		    p.clickHandler = this;
		}));

	setPropertiesHandler(eventsArea, a -> {
		final var ss = eventsArea.selected();
		if (ss == null)
		    return null;
		return new EventPropertiesLayout(app, ss, getReturnAction());
	    });

	final var eventsActions = actions(
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

	final var calendarParams = new CalendarArea.Params();
	calendarParams.context = getControlContext();
	calendarParams.changeListener = this;
	calendarParams.calendar = Calendar.getInstance();

	calendarArea = new CalendarArea(calendarParams);

	setAreaLayout(AreaLayout.LEFT_TOP_BOTTOM,
		      eventsArea, eventsActions,
		      notesArea, null,
		      calendarArea, null);

	// Load initial events for today
	loadEventsForDate(calendarParams.calendar.getTime());
    }

    @Override public boolean onListClick(ListArea<Event> area, int index, org.luwrain.pim.diary.persistence.Event event)
    {
	if (event == null)
	    return false;
	app.setEventResponse(listItem(event.getTitle() + " " + app.getStrings().eventListSuffix()));
	notesArea.setText(new String[]{ requireNonNullElse(event.getComment(), "") });
	return true;
    }

    boolean onCreateEvent()
    {
	final var s = app.getStrings();
	final var popup = new SimpleEditPopup(
	    app.getLuwrain(),
	    s.createEventPopupName(),
	    s.createEventPopupPrefix(),
	    "",
	    EnumSet.noneOf(Popup.Flags.class)
	);
	app.getLuwrain().popup(popup);
	if (popup.wasCancelled())
	    return true;
	final var title = popup.text();
	if (title == null || title.trim().isEmpty())
	    return true;
	final var event = new Event();
	event.setTitle(title.trim());
	event.setDtStart(calendarArea.getCalendar().getTime().getTime());
	app.persist.getEventDAO().add(event);
	loadEventsForDate(calendarArea.getCalendar().getTime());
	app.getLuwrain().playSound(Sounds.DONE);
	return true;
    }

    boolean onDeleteEvent()
    {
	final var selected = eventsArea.selected();
	if (selected == null)
	    return false;
	final var s = app.getStrings();
	final var title = requireNonNullElse(selected.getTitle(), "");
	final var popup = new YesNoPopup(
	    app.getLuwrain(),
	    s.deleteEventPopupName(),
	    s.deleteEventPopupText() + " " + title,
	    false,
	    EnumSet.noneOf(Popup.Flags.class)
	);
	app.getLuwrain().popup(popup);
	if (!popup.result() || popup.wasCancelled())
	    return true;
	app.persist.getEventDAO().delete(selected);
	loadEventsForDate(calendarArea.getCalendar().getTime());
	app.getLuwrain().playSound(Sounds.DONE);
	return true;
    }

    @Override public void onCalendarChange(Date date)
    {
	loadEventsForDate(date);
    }

    void loadEventsForDate(Date date)
    {
	requireNonNull(date, "date can't be null");
	final var allEvents = app.persist.getEventDAO().getAll();
	events.clear();
	if (allEvents == null || allEvents.isEmpty())
	{
	    eventsArea.refresh();
	    return;
	}
	final var cal = Calendar.getInstance();
	cal.setTime(date);
	final int targetDay = cal.get(Calendar.DAY_OF_MONTH);
	final int targetMonth = cal.get(Calendar.MONTH);
	final int targetYear = cal.get(Calendar.YEAR);
	final var tmpCal = Calendar.getInstance();
	for (var ev : allEvents)
	{
	    tmpCal.setTime(new Date(ev.getDtStart()));
	    if (tmpCal.get(Calendar.DAY_OF_MONTH) == targetDay
		&& tmpCal.get(Calendar.MONTH) == targetMonth
		&& tmpCal.get(Calendar.YEAR) == targetYear)
	    {
		events.add(ev);
	    }
	}
	eventsArea.refresh();
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
	    final var dt = new Date(event.getDtStart());
	    if (dt != null)
		sb.append(String.format("%02d:%02d ", dt.getHours(), dt.getMinutes()));
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
	    final var dt = new Date(event.getDtStart());
		sb.append(String.format("%02d:%02d ", dt.getHours(), dt.getMinutes()));
	    sb.append(requireNonNullElse(event.getTitle(), ""));
	    return sb.toString();
	}
    }
}
