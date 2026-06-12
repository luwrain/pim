
package org.luwrain.app.contacts;

import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.controls.*;
import org.luwrain.pim.contacts.persistence.*;
import org.luwrain.app.base.*;

public final class App extends AppBase<Strings> implements MonoApp
{
    private ContactsPersistence persist = null;
    private Conversations conv = null;
    private MainLayout mainLayout = null;

    public App()
    {
	super(Strings.NAME, Strings.class, "luwrain.contacts");
    }

    @Override protected AreaLayout onAppInit() throws Exception
    {
	this.conv = new Conversations(this);
	this.persist = null;//org.luwrain.pim.Connections.getContactsStoring(getLuwrain(), true);
	this.mainLayout = new MainLayout(this);
	setAppName(getStrings().appName());
	return mainLayout.getAreaLayout();
    }

    @Override public boolean onEscape()
    {
	closeApp();
	return true;
    }

    @Override public void closeApp()
    {
	mainLayout.ensureEverythingSaved();
	super.closeApp();
    }

    @Override public MonoApp.Result onMonoAppSecondInstance(Application app)
    {
	NullCheck.notNull(app, "app");
	return MonoApp.Result.BRING_FOREGROUND;
    }

    Conversations getConv()
    {
	return this.conv;
    }
    }
