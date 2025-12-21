// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.mail;

import java.io.*;
import org.apache.logging.log4j.*;
import groovy.util.*;

import org.luwrain.controls.*;
import org.luwrain.controls.wizard.*;
import org.luwrain.app.base.*;

import static org.luwrain.util.ResourceUtils.*;

final class GreetingLayout extends LayoutBase
{
    static private final Logger log = LogManager.getLogger();

    final App app;
    final WizardArea wizardArea;
    final WizardGroovyController controller;
    private String mail = "", passwd = "";

    GreetingLayout(App app) throws IOException
    {
	super(app);
	this.app = app;
	wizardArea = new WizardArea(getControlContext()) ;
	controller = new WizardGroovyController(getLuwrain(), wizardArea){
		public Strings getStrings() { return app.getStrings(); }
		public void skip() {app.layouts().main(); }
	    };
	Eval.me("wizard", controller, getStringResource(this.getClass(), "greeting.groovy"));
	setAreaLayout(wizardArea, null);
    }
}
