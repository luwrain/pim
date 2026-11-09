// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.tasks;

import java.util.*;

import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.app.base.*;
import org.luwrain.controls.*;
import org.luwrain.controls.list.*;

import org.luwrain.pim.tasks.persist.*;

import static java.util.Objects.*;
import static org.luwrain.core.DefaultEventResponse.*;
import static org.luwrain.core.events.InputEvent.*;

public class MainLayout extends LayoutBase implements ListArea.ClickHandler<Task>
{
    public final List<Task> tasks = new ArrayList<>();
    public final ListArea<Task> tasksArea;
    final App app;

    private TaskList currentList = null;

    MainLayout(App app)
    {
	super(app);
	this.app = app;
	final var s = app.getStrings();

	tasksArea = new ListArea<Task>(listParams(p -> {
		    p.name = s.tasksAreaName();
		    p.model = new ListModel<>(tasks);
		    p.appearance = new TaskListAppearance(getControlContext());
		    p.clickHandler = this;
		}));

	final var tasksActions = actions(
	    action("create", s.create(), new InputEvent(Special.INSERT),
		   () -> { onCreateTask(); return true; }),
	    action("delete", s.delete(), new InputEvent(Special.DELETE),
		   () -> { onDeleteTask(); return true; })
	    );

	setAreaLayout(tasksArea, tasksActions);

	ensureCurrentList();
	loadTasks();
    }

    @Override public boolean onListClick(ListArea<Task> area, int index, Task task)
    {
	if (task == null)
	    return false;
	app.setEventResponse(listItem(requireNonNullElse(task.getTitle(), "") + " " + app.getStrings().taskListSuffix()));
	return true;
    }

    boolean onCreateTask()
    {
	final var title = app.conv.newTaskTitle();
	if (title == null || title.isEmpty())
	    return true;
	final var task = new Task();
	task.setTitle(title);
	task.setStatus(Task.Status.NEW);
	task.setPriority(Task.Priority.NORMAL);
	task.setCreatedTimestamp(System.currentTimeMillis());
	app.persist.getTaskDAO().add(currentList, task);
	loadTasks();
	app.getLuwrain().playSound(Sounds.DONE);
	return true;
    }

    boolean onDeleteTask()
    {
	final var selected = tasksArea.selected();
	if (selected == null)
	    return false;
	final var title = requireNonNullElse(selected.getTitle(), "");
	if (!app.conv.confirmDeleteTask(title))
	    return true;
	app.persist.getTaskDAO().delete(currentList, selected);
	loadTasks();
	app.getLuwrain().playSound(Sounds.DONE);
	return true;
    }

    private void ensureCurrentList()
    {
	final var lists = app.persist.getTaskListDAO().load();
	if (lists != null && !lists.isEmpty())
	{
	    currentList = lists.get(0);
	    return;
	}
	final var list = new TaskList();
	list.setName("Default");
	list.setOrderIndex(0);
	app.persist.getTaskListDAO().add(list);
	currentList = list;
    }

    void loadTasks()
    {
	tasks.clear();
	if (currentList == null)
	{
	    tasksArea.refresh();
	    return;
	}
	final var loaded = app.persist.getTaskDAO().load(currentList);
	if (loaded != null)
	    tasks.addAll(loaded);
	tasksArea.refresh();
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
