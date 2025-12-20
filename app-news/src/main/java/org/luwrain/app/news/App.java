// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.news;

import java.util.*;

import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.pim.*;
import org.luwrain.pim.news.persist.*;
import org.luwrain.app.base.*;
import org.luwrain.core.annotations.*;

@AppNoArgs(
	   name = "news",
	   title = { "en=News", "ru=Новости" })
public final class App extends AppBase<Strings> implements MonoApp
{
    public NewsPersistence persist = null;
    private MainLayout mainLayout = null;
    Conv conv = null;
    private Group group = null;
    boolean showAllGroups = false;

    final List<GroupWrapper> groups = new ArrayList<>();
    final List<Article>  articles = new ArrayList<Article>();

    public App() { super(Strings.class, "luwrain.news"); }

    @Override public AreaLayout onAppInit() throws Exception
    {
	this.persist = getLuwrain().createInstance(NewsPersistence.class);
	if (persist == null)
	    throw new Exception("No news persistence");
		this.conv = new Conv(this);
	this.mainLayout = new MainLayout(this );
	setAppName(getStrings().appName());
	loadGroups();
	runFetching();
	return this.mainLayout.getAreaLayout();
    }

    boolean openGroup(Group newGroup)
    {
	this.group = newGroup;
	loadArticles();
	return true;
    }

    void loadGroups()
    {
	//	final List<GroupWrapper> w = new ArrayList<>();
	final var g = persist.getGroupDAO().load();
	/*
			Collections.sort(g);
	final var newCounts = persist.getArticleDAO().countNewInGroups(g);
	final var markedCounts = persist.getArticleDAO().countMarkedInGroups(g);
	for(int i = 0;i < g.length;++i)
	{
	    final int newCount = i < newCounts.length?newCounts[i]:0;
	    final int markedCount = i < markedCounts.length?markedCounts[i]:0;
	    if (showAllGroups || newCount > 0 || markedCount > 0)
		w.add(new GroupWrapper(g[i], newCount));
	}
	*/
groups.clear();
groups.addAll(g.stream().map(e -> new GroupWrapper(e, 0)).toList());
    }

    void loadArticles()
    {
	if (group == null)
	{
	    this.articles.clear();
	    return;
	}
	this.articles.clear();
	this.articles.addAll(persist.getArticleDAO().loadWithoutRead(group));
	if (articles.isEmpty())
	    this.articles.addAll(persist.getArticleDAO().load(group));
	/*FIXME:
	if (articles != null)
	    Collections.sort(articles);
	*/
    }

    @Override public boolean onEscape()
    {
	closeApp();
	return true;
    }

    @Override public boolean onSystemEvent(Area area, SystemEvent event)
    {
	if (event.getType() != SystemEvent.Type.REGULAR)
	    return super.onSystemEvent(area, event);
	switch(event.getCode())
	{
	case REFRESH:
	    runFetching();
	    return true;
	}
	return super.onSystemEvent(area, event);
    }

    void runFetching()
    {
	getLuwrain().runWorker(org.luwrain.pim.workers.News.NAME);
    }

    @Override public MonoApp.Result onMonoAppSecondInstance(Application app)
    {
	runFetching();
	return MonoApp.Result.BRING_FOREGROUND;
    }
}
