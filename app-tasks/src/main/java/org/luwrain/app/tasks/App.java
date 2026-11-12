// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.tasks;

import org.luwrain.core.*;
import org.luwrain.app.base.*;
import org.luwrain.core.annotations.*;
import org.luwrain.pim.tasks.persist.*;

@AppNoArgs(
	   name = "tasks",
	   title = { "en=Tasks", "ru=Задачи" },
	   category = StarterCategory.PIM)
public final class App extends AppBase<Strings>
{
    private MainLayout mainLayout;
    Conv conv;
    public TasksPersistence persist;

    public App() { super(Strings.class, "luwrain.tasks"); }

    @Override public AreaLayout onAppInit()
    {
	persist = getLuwrain().createInstance(TasksPersistence.class);
	if (persist == null)
	    throw new IllegalStateException("No tasks persistence");
	this.conv = new Conv(this);
	mainLayout = new MainLayout(this, getDefaultTaskList());
	setAppName(getStrings().appName());
	return mainLayout.getAreaLayout();
    }

    @Override public boolean onEscape()
    {
	mainLayout.onDeactivateTask();
	closeApp();
	return true;
    }

    private TaskList getDefaultTaskList()
    {
var t = persist.getTaskListDAO().load().stream()
	.filter(e -> e.isDefaultList()).findFirst();
if (t.isPresent())
return t.get();
var l = new TaskList();
l.setName(getStrings().defaultTaskListName());
l.setDefaultList(true);
persist.getTaskListDAO().add(l);
return l;
    }
}
