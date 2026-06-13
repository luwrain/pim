// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.diary.layouts;

import java.util.*;
import java.text.*;

import org.luwrain.core.*;
import org.luwrain.app.base.*;
import org.luwrain.controls.*;
import org.luwrain.app.diary.*;
import org.luwrain.pim.diary.persistence.Event;

import static java.util.Objects.*;

public final class EventPropertiesLayout extends LayoutBase
{
    static private final String
	TITLE = "title",
	COMMENT = "comment",
	LOCATION = "location",
	DATE = "date",
	TIME = "time",
	RRULE = "rrule";

    static private final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");
    static private final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm");

    final App app;
    final FormArea form;
    final Event event;

    public EventPropertiesLayout(App app, Event event, ActionHandler close)
    {
	super(app);
	this.app = app;
	this.event = event;
	final var s = app.getStrings();

	form = new FormArea(getControlContext(), s.eventPropertiesAreaName());
	form.addEdit(TITLE, s.titleEdit(), requireNonNullElse(event.getTitle(), ""));
	form.addEdit(COMMENT, s.commentEdit(), requireNonNullElse(event.getComment(), ""));
	form.addEdit(LOCATION, s.locationEdit(), requireNonNullElse(event.getLocation(), ""));

	final var dt = new Date(event.getDtStart());
	form.addEdit(DATE, s.dateEdit(), DATE_FORMAT.format(dt));
	form.addEdit(TIME, s.timeEdit(), TIME_FORMAT.format(dt));
	form.addEdit(RRULE, s.rruleEdit(), requireNonNullElse(event.getRrule(), ""));

	setAreaLayout(form, null);
	setOkHandler(() -> {
		event.setTitle(form.getEnteredText(TITLE).trim());
		event.setComment(form.getEnteredText(COMMENT).trim());
		event.setLocation(form.getEnteredText(LOCATION).trim());
		event.setRrule(form.getEnteredText(RRULE).trim());
		try {
		    final var datePart = DATE_FORMAT.parse(form.getEnteredText(DATE).trim());
		    final var timePart = TIME_FORMAT.parse(form.getEnteredText(TIME).trim());
		    final var cal = Calendar.getInstance();
		    cal.setTime(datePart);
		    final var timeCal = Calendar.getInstance();
		    timeCal.setTime(timePart);
		    cal.set(Calendar.HOUR_OF_DAY, timeCal.get(Calendar.HOUR_OF_DAY));
		    cal.set(Calendar.MINUTE, timeCal.get(Calendar.MINUTE));
		    event.setDtStart(cal.getTime().getTime());
		}
		catch (ParseException e)
		{
		}
		app.persist.getEventDAO().update(event);
		app.getLuwrain().playSound(Sounds.DONE);
		close.onAction();
		return true;
	    });
	setCloseHandler(close);
    }
}
