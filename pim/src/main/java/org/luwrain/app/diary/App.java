/*
   Copyright 2012-2024 Michael Pozhidaev <msp@luwrain.org>

   This file is part of LUWRAIN.

   LUWRAIN is free software; you can redistribute it and/or
   modify it under the terms of the GNU General Public
   License as published by the Free Software Foundation; either
   version 3 of the License, or (at your option) any later version.

   LUWRAIN is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
   General Public License for more details.
*/

package org.luwrain.app.diary;

import java.util.*;
import java.util.concurrent.*;
import java.io.*;

import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.controls.*;
import org.luwrain.controls.edit.*;
import org.luwrain.speech.*;
import org.luwrain.app.base.*;

import org.luwrain.pim.diary.persistence.*;
import org.luwrain.pim.diary.persistence.model.*;
import org.luwrain.pim.diary.persistence.dao.*;


public final class App extends AppBase<Strings>
{
    Settings sett = null;
    private Conversations conv = null;
    private MainLayout mainLayout = null;

    private EventDAO eventDAO = null;

    public App()
    {
	super(Strings.class, "luwrain.diary");
    }

    @Override protected AreaLayout onAppInit() throws IOException
    {
	this.conv = new Conversations(this);
	this.mainLayout = new MainLayout(this);
	this.eventDAO = DiaryPersistence.getEventDAO();
	setAppName(getStrings().appName());
	return mainLayout.getAreaLayout();
    }

    @Override public boolean onEscape()
    {
	closeApp();
	return true;
    }

            Conversations getConv() { return this.conv; }
    Settings getSett() { return this.sett; }
    EventDAO getEventDAO() { return eventDAO; }
}
