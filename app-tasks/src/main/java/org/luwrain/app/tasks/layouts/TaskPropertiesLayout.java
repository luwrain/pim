// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.tasks.layouts;

import java.util.*;
import java.text.*;

import org.luwrain.core.*;
import org.luwrain.controls.*;
import org.luwrain.popups.*;
import org.luwrain.pim.tasks.persist.*;
import org.luwrain.app.base.*;
import org.luwrain.app.tasks.*;

import static java.util.Objects.*;

public final class TaskPropertiesLayout extends LayoutBase
{
    static private final String
	TITLE = "title",
	DESCR = "descr",
	DUE = "due",
	STATUS = "status",
	PRIORITY = "priority",
	TAGS = "tags";

    static private final SimpleDateFormat DUE_FORMAT = new SimpleDateFormat("dd.MM.yyyy HH:mm");

    private final App app;
    private final Task task;
    final FormArea formArea;

    public TaskPropertiesLayout(App app, Task task, ActionHandler closing)
    {
	super(app);
	this.app = app;
	this.task = task;
	final var s = app.getStrings();
	this.formArea = new FormArea(getControlContext(), s.taskPropertiesAreaName(requireNonNullElse(task.getTitle(), "")));
	formArea.addEdit(TITLE, s.taskPropertiesTitle(), requireNonNullElse(task.getTitle(), ""));
	formArea.addEdit(DESCR, s.taskPropertiesDescr(), requireNonNullElse(task.getDescr(), ""));
	formArea.addEdit(DUE, s.taskPropertiesDueDate(), formatDue(task.getDueTimestamp()));
	formArea.addList(STATUS, s.taskPropertiesStatus(),
			 requireNonNullElse(task.getStatus(), Task.Status.NEW),
			 (area, itemName, current) -> chooseStatus(),
			 null, true);
	formArea.addList(PRIORITY, s.taskPropertiesPriority(),
			 requireNonNullElse(task.getPriority(), Task.Priority.NORMAL),
			 (area, itemName, current) -> choosePriority(),
			 null, true);
	formArea.addEdit(TAGS, s.taskPropertiesTags(), joinTags(task.getTags()));
	setCloseHandler(closing);
	setOkHandler(() -> {
		if (!save())
		    return true;
		app.getLuwrain().playSound(Sounds.DONE);
		return closing.onAction();
	    });
	setAreaLayout(formArea, actions());
    }

    private boolean save()
    {
	final var s = app.getStrings();
	final String title = formArea.getEnteredText(TITLE).trim();
	if (title.isEmpty())
	{
	    app.message(s.taskPropertiesTitleCannotBeEmpty(), Luwrain.MessageType.ERROR);
	    return false;
	}
	final long due;
	try {
	    due = parseDue(formArea.getEnteredText(DUE));
	}
	catch(ParseException e)
	{
	    app.message(s.taskPropertiesInvalidDueDate(), Luwrain.MessageType.ERROR);
	    return false;
	}
	task.setTitle(title);
	task.setDescr(formArea.getEnteredText(DESCR).trim());
	task.setDueTimestamp(due);
	task.setStatus((Task.Status)formArea.getSelectedListItem(STATUS));
	task.setPriority((Task.Priority)formArea.getSelectedListItem(PRIORITY));
	task.setTags(parseTags(formArea.getEnteredText(TAGS)));
	app.persist.getTaskDAO().update(task);
	return true;
    }

    private Task.Status chooseStatus()
    {
	final var selected = Popups.fixedList(getLuwrain(),
					      app.getStrings().taskPropertiesStatus(),
					      Task.Status.values(),
					      Popups.DEFAULT_POPUP_FLAGS);
	return selected != null?(Task.Status)selected:requireNonNullElse(task.getStatus(), Task.Status.NEW);
    }

    private Task.Priority choosePriority()
    {
	final var selected = Popups.fixedList(getLuwrain(),
					      app.getStrings().taskPropertiesPriority(),
					      Task.Priority.values(),
					      Popups.DEFAULT_POPUP_FLAGS);
	return selected != null?(Task.Priority)selected:requireNonNullElse(task.getPriority(), Task.Priority.NORMAL);
    }

    private String formatDue(long timestamp)
    {
	return timestamp >= 0?DUE_FORMAT.format(new Date(timestamp)):"";
    }

    private long parseDue(String value) throws ParseException
    {
	final String v = value.trim();
	if (v.isEmpty())
	    return -1L;
	return DUE_FORMAT.parse(v).getTime();
    }

    private String joinTags(List<String> tags)
    {
	if (tags == null || tags.isEmpty())
	    return "";
	return String.join(", ", tags);
    }

    private List<String> parseTags(String value)
    {
	return Arrays.stream(value.split(","))
	.map(String::trim)
	.filter(e -> !e.isEmpty())
	.toList();
    }

    private List<String> splitContent(String content)
    {
	if (content == null)
	    return new ArrayList<>();
	return Arrays.asList(content.split("\n", -1));
    }
}

