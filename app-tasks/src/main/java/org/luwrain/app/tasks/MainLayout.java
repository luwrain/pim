// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.tasks;

import java.util.*;

import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.app.base.*;
import org.luwrain.controls.*;
import org.luwrain.controls.edit.*;
import org.luwrain.controls.list.*;
import org.luwrain.pim.tasks.persist.*;
import org.luwrain.app.tasks.layouts.*;

import static java.util.Objects.*;
import static org.luwrain.core.DefaultEventResponse.*;
import static org.luwrain.core.events.InputEvent.*;

public class MainLayout extends LayoutBase implements ListArea.ClickHandler<Task>
{
    public final List<Task> tasks = new ArrayList<>();
    public final ListArea<Task> tasksArea;
    public final EditArea contentEdit;
    final App app;

    private final TaskList defaultTaskList;
    private Task currentTask = null;

    private final EditArea.ChangeListener contentChangeListener;

    
    MainLayout(App app, TaskList defaultTaskList)
    {
	super(app);
	this.app = app;
	this.defaultTaskList = defaultTaskList;
	final var s = app.getStrings();

	contentChangeListener = (area, lines, hotPoint) -> {
	if (currentTask == null)
	    return;
	currentTask.setContent(String.join("\n", area.getTextAsList()));
	app.persist.getTaskDAO().update(currentTask);
    };


	tasksArea = new ListArea<Task>(listParams(p -> {
		    p.name = s.tasksAreaName();
		    p.model = new ListModel<>(tasks);
		    p.appearance = new TaskListAppearance(getControlContext());
		    p.clickHandler = this;
		}));

	contentEdit = new EditArea(editParams(p -> {
		    p.name = s.taskContentAreaName();
		    p.changeListeners = List.of(contentChangeListener);
		}));

	setPropertiesHandler(tasksArea, a -> {
	final var t = tasksArea.selected();
	if (t == null)
	    return null;
	return new TaskPropertiesLayout(app, t, getReturnAction());
    });

	final var tasksActions = actions(
					 actCreateTask(), actDeleteTask()
	    );

	setAreaLayout(AreaLayout.LEFT_RIGHT, tasksArea, tasksActions, contentEdit, null);
	loadTasks();
    }

    void onActivateTask(Task task)
    {
		currentTask = task;
	loadTaskContent(task);

    }

    void onDeactivateTask()
    {
    }

    @Override public boolean onListClick(ListArea<Task> area, int index, Task task)
    {
	if (task == null)
	    return false;
	onActivateTask(task);
	setActiveArea(contentEdit);
	return true;
    }

    ActionInfo actCreateTask()
    {
	return action("create", app.getStrings().create(), new InputEvent(Special.INSERT), () -> {
	final var title = app.conv.newTaskTitle();
	if (title == null || title.isEmpty())
	    return true;
	final var task = new Task();
	task.setTitle(title);
	task.setStatus(Task.Status.NEW);
	task.setPriority(Task.Priority.NORMAL);
	task.setCreatedTimestamp(System.currentTimeMillis());
	app.persist.getTaskDAO().add(defaultTaskList, task);
	loadTasks();
	app.getLuwrain().playSound(Sounds.DONE);
	return true;
	    });
    }

    ActionInfo actDeleteTask()
    {
	return action("delete", app.getStrings().delete(), new InputEvent(Special.DELETE), () -> {
	final var selected = tasksArea.selected();
	if (selected == null)
	    return false;
	final var title = requireNonNullElse(selected.getTitle(), "");
	if (!app.conv.confirmDeleteTask(title))
	    return true;
	app.persist.getTaskDAO().delete(defaultTaskList, selected);
	if (currentTask == selected)
	{
	    currentTask = null;
	    clearTaskContent();
	}
	loadTasks();
	app.getLuwrain().playSound(Sounds.DONE);
	return true;
	    });
    }

    void loadTasks()
    {
	tasks.clear();
	final var loaded = app.persist.getTaskDAO().load(defaultTaskList);
	if (loaded != null)
	    tasks.addAll(loaded);
	Collections.sort(tasks, (v1, v2) -> {
		if (v1.getPriority() == Task.Priority.URGENT && v2.getPriority() != Task.Priority.URGENT)
		    return -1;
		if (v2.getPriority() == Task.Priority.URGENT && v1.getPriority() != Task.Priority.URGENT)
		    return 1;
				if (v1.getPriority() == Task.Priority.HIGH && v2.getPriority() != Task.Priority.HIGH)
		    return -1;
		if (v2.getPriority() == Task.Priority.HIGH && v1.getPriority() != Task.Priority.HIGH)
		    return 1;
		return 0;
	    });
	tasksArea.refresh();
    }

    private void loadTaskContent(Task task)
    {
	final String content = task.getContent();
	final String[] lines = content != null?content.split("\n", -1):new String[]{""};
	contentEdit.update((mutableLines, hotPoint) -> {
		mutableLines.setLines(lines);
		hotPoint.setHotPointX(0);
		hotPoint.setHotPointY(0);
		return false;
	    });
	contentEdit.refresh();
    }

    private void clearTaskContent()
    {
	contentEdit.update((mutableLines, hotPoint) -> {
		mutableLines.setLines(new String[]{""});
		hotPoint.setHotPointX(0);
		hotPoint.setHotPointY(0);
		return false;
	    });
	contentEdit.refresh();
    }

    final class TaskListAppearance extends AbstractAppearance<Task>
    {
	TaskListAppearance(ControlContext context)
	{
	    super();
	}

	@Override public void announceItem(Task task, Set<Flags> flags)
	{
	    if (task == null)
	    {
		app.setEventResponse(hint(Hint.EMPTY_LINE));
		return;
	    }
	    app.setEventResponse(listItem(requireNonNullElse(task.getTitle(), "") + " " + app.getStrings().taskListSuffix()));
	}

	@Override public String getScreenAppearance(Task task, Set<Flags> flags)
	{
	    if (task == null)
		return "";
	    return requireNonNullElse(task.getTitle(), "");
	}
    }
}
