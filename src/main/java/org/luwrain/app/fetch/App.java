/*
   Copyright 2012-2018 Michael Pozhidaev <michael.pozhidaev@gmail.com>

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

package org.luwrain.app.fetch;

import java.util.*;

import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.core.queries.*;
import org.luwrain.controls.*;

public class App implements Application, MonoApp
{
    static private final String STRINGS_NAME = "luwrain.fetch";

    private Luwrain luwrain;
    private Strings strings;
    private final Base base = new Base();
    private ProgressArea area;
    private Set<Base.Type> fetchType;

    public App(Set<Base.Type> fetchType)
    {
	NullCheck.notNull(fetchType, "fetchType");
	this.fetchType = fetchType;
    }

    @Override public InitResult onLaunchApp(Luwrain luwrain)
    {
	NullCheck.notNull(luwrain, "luwrain");
	final Object o = luwrain.i18n().getStrings(STRINGS_NAME);
	if (o == null || !(o instanceof Strings))
	    return new InitResult(InitResult.Type.NO_STRINGS_OBJ, STRINGS_NAME);
	this.luwrain = luwrain;
	strings = (Strings)o;
	if (!base.init(luwrain, strings))
	    return new InitResult(InitResult.Type.FAILURE);
	createArea();
	return new InitResult();
    }

    @Override public MonoApp.Result onMonoAppSecondInstance(Application app)
    {
	NullCheck.notNull(app, "app");
	return MonoApp.Result.BRING_FOREGROUND;
    }

private Action[] getFetchAreaActions()
    {
	if (!(fetchType.contains(Base.Type.NEWS) && fetchType.contains(Base.Type.INCOMING_MAIL) && fetchType.contains(Base.Type.OUTGOING_MAIL)))
	    return new Action[0];
	return new Action[]{
	    new Action("fetch-all", strings.actionFetchAll()),
	    new Action("fetch-mail", strings.actionFetchMail(), new KeyboardEvent(KeyboardEvent.Special.F5)),
	    new Action("fetch-incoming-mail", strings.actionFetchIncomingMail(), new KeyboardEvent(KeyboardEvent.Special.F6)),
	    new Action("fetch-outgoing-mail", strings.actionFetchOutgoingMail(), new KeyboardEvent(KeyboardEvent.Special.F7)),
	    new Action("fetch-news", strings.actionFetchNews(), new KeyboardEvent(KeyboardEvent.Special.F8)),
	};
    }

private boolean onAreaAction(EnvironmentEvent event)
    {
	NullCheck.notNull(event, "event");
	if (ActionEvent.isAction(event, "fetch-all"))
	    return start(fetchType != null?fetchType:EnumSet.of(Base.Type.NEWS, Base.Type.INCOMING_MAIL, Base.Type.OUTGOING_MAIL));
	if (ActionEvent.isAction(event, "fetch-news"))
	    return start(EnumSet.of(Base.Type.NEWS));
	if (ActionEvent.isAction(event, "fetch-mail"))
	    return start(EnumSet.of(Base.Type.INCOMING_MAIL, Base.Type.OUTGOING_MAIL));
	if (ActionEvent.isAction(event, "fetch-incoming-mail"))
	    return start(EnumSet.of(Base.Type.INCOMING_MAIL));
	if (ActionEvent.isAction(event, "fetch-outgoing-mail"))
	    return start(EnumSet.of(Base.Type.OUTGOING_MAIL));
	return false;
    }

boolean start(Set<Base.Type> type)
    {
	NullCheck.notNull(type, "type");
	return base.start(area, type);
    }

boolean interrupt()
    {
	return base.interrupt();
    }

    private void createArea()
    {
area = new ProgressArea(new DefaultControlContext(luwrain), strings.appName(),
			base.prepareIntroduction(fetchType)){

		@Override public boolean onInputEvent(KeyboardEvent event)
		{
		    NullCheck.notNull(event, "event");
		    if (event.isSpecial() && !event.isModified())
			switch(event.getSpecial())
			{
			case ENTER:
			    return start(fetchType);
			case ESCAPE:
			    return interrupt();
			}
		    return super.onInputEvent(event);
		}

		@Override public boolean onSystemEvent(EnvironmentEvent event)
		{
		    NullCheck.notNull(event, "event");
		    if (event.getType() != EnvironmentEvent.Type.REGULAR)
			return super.onSystemEvent(event);
		    switch(event.getCode())
		    {
		    case CLOSE:
			closeApp();
			return true;
		    case ACTION:
			return onAreaAction(event);
		    default:
			return super.onSystemEvent(event);
		    }
		}

	@Override public Action[] getAreaActions()
	{
	    return getFetchAreaActions();
	}

		@Override public boolean onAreaQuery(AreaQuery query)
		{
		    NullCheck.notNull(query, "query");
		    switch(query.getQueryCode())
		    {
		    case AreaQuery.BACKGROUND_SOUND:
if (base.fetchingInProgress())
			{
			    ((BackgroundSoundQuery)query).answer(new BackgroundSoundQuery.Answer(BkgSounds.FETCHING));
			return true;
			}
			return false;
		    default:
			return super.onAreaQuery(query);
		    }
		}

	    };
    }

@Override public void closeApp()
    {
	base.interrupt();
	luwrain.closeApp();
    }

    @Override public AreaLayout getAreaLayout()
    {
	return new AreaLayout(area);
    }

    @Override public String getAppName()
    {
	return strings.appName();
    }
    }
