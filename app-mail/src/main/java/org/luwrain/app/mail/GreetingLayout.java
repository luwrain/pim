/*
   Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>
   Copyright 2015-2016 Roman Volovodov <gr.rPman@gmail.com>

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
