// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.diary;

import java.util.*;

import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.app.base.*;
import org.luwrain.core.annotations.*;

@AppNoArgs(
	   name = "diary",
	   title = { "en=Diary", "ru=Дневник" },
	   category = StarterCategory.PIM)
public final class App extends AppBase<Strings>
{
    private MainLayout mainLayout = null;
    private RegularEventsLayout regularEventsLayout = null;

    public App() { super(Strings.class, "luwrain.commander"); }

    @Override public AreaLayout onAppInit()
    {
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

    MainLayout getMainLayout() { return mainLayout; }
    RegularEventsLayout getRegularEventsLayout() { return regularEventsLayout; }
}
