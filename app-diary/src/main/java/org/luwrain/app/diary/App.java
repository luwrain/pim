// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.diary;

import java.util.*;

import org.luwrain.core.*;
import org.luwrain.app.base.*;
import org.luwrain.core.annotations.*;
import org.luwrain.pim.diary.persistence.*;

@AppNoArgs(
	   name = "diary",
	   title = { "en=Diary", "ru=Дневник" },
	   category = StarterCategory.PIM)
public final class App extends AppBase<Strings>
{
    private MainLayout mainLayout;
    private RegularEventsLayout regularEventsLayout;
    DiaryPersistence persist;

    public App() { super(Strings.class, "luwrain.diary"); }

    @Override public AreaLayout onAppInit()
    {
	persist = getLuwrain().createInstance(DiaryPersistence.class);
	if (persist == null)
	    throw new IllegalStateException("No diary persistence");
	mainLayout = new MainLayout(this);
	regularEventsLayout = new RegularEventsLayout(this);
	setAppName(getStrings().appName());
	return mainLayout.getAreaLayout();
    }

    @Override public boolean onEscape()
    {
	closeApp();
	return true;
    }
}
