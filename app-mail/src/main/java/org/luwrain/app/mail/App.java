// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.mail;

import java.io.*;
import org.apache.logging.log4j.*;

import org.luwrain.core.*;
import org.luwrain.core.annotations.*;
import org.luwrain.app.base.*;

@AppNoArgs(name = "mail",
	   title = {"en=Mail", "ru=Почта"} ,
	   category = StarterCategory.COMMUNICATIONS)
public final class App extends AppBase<Strings> implements MonoApp
{
    static final Logger log = LogManager.getLogger();

    private Hooks hooks = null;
    private Data data = null;
    public Conv conv = null;
    private MainLayout mainLayout = null;
    private GreetingLayout greetingLayout = null;

    public App()
    {
	super(Strings.class, "luwrain.mail");
    }

    @Override protected AreaLayout onAppInit() throws Exception
    {
hooks = new Hooks(getLuwrain());
data = new Data(getLuwrain(), getStrings(), new File(getLuwrain().getFileProperty(Luwrain.PROP_DIR_USERHOME), ".luwrain-defaults.conf"));
conv = new Conv(this);
mainLayout = new MainLayout(this, data);

	setAppName(getStrings().appName());
	if (data.accountDAO.getAll().isEmpty())
	{
	    	this.greetingLayout = new GreetingLayout(this);
	    return greetingLayout.getAreaLayout();
	}
	return mainLayout.getAreaLayout();
    }

    Layouts layouts()
    {
	return new Layouts(){
	    @Override public void main()
	    {
		setAreaLayout(mainLayout);
		getLuwrain().announceActiveArea();
	    }
	    @Override public void messageMode()
	    {
	    }
	};
    }

    @Override public boolean onEscape()
    {
	closeApp();
	return true;
    }

    @Override public MonoApp.Result onMonoAppSecondInstance(Application app)
    {
	return MonoApp.Result.BRING_FOREGROUND;
    }

//    MailStoring getStoring() { return this.storing; }
    Hooks getHooks() { return this.hooks; }
    Conv getConv() { return conv; }
    public Data getData() { return data; }

    interface Layouts
    {
	void main();
	void messageMode();
    }
}
