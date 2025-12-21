// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.news.layouts;

import java.util.*;

import org.luwrain.core.*;
import org.luwrain.controls.*;
import org.luwrain.pim.news.persist.*;
import org.luwrain.app.base.*;
import org.luwrain.app.news.*;

import static java.util.Objects.*;

public final class GroupPropertiesLayout extends LayoutBase
{
    static private final String
	NAME = "name",
	ORDER_INDEX = "order-index",
	EXPIRE_AFTER_DAYS = "expire-after-days",
    ALWAYS_SHOW = "always-show";

    private final App app;
    final FormArea formArea;
    
    public GroupPropertiesLayout(App app, Group group, ActionHandler closing)
    {
	super(app);
	this.app = app;
	final var s = app.getStrings();
	this.formArea = new FormArea(getControlContext(), s.groupPropertiesAreaName(group.getName())) ;
	List<String> urls = requireNonNullElse(group.getUrls(), Arrays.asList());
	urls = new ArrayList<>(urls.stream()
			       .map(e -> e.trim())
			       .filter(e -> !e.isEmpty())
			       .toList());
	urls.add("");
	formArea.addEdit(NAME, s.groupPropertiesName(), group.getName());
	formArea.addEdit(ORDER_INDEX, s.groupPropertiesOrderIndex(), "" + group.getOrderIndex());
	formArea.addMultilineEdit(s.groupPropertiesUrls(), urls, true);
	setCloseHandler(closing);
	setOkHandler(() -> {
		if (!save(group))
		    return true;
		return closing.onAction();
	    });
	setAreaLayout(formArea, actions());
    }

    private boolean save(Group group)
    {
	final String
	name = formArea.getEnteredText(NAME),
	orderIndex = formArea.getEnteredText(ORDER_INDEX);
	if (name.trim().isEmpty())
	{
	    app.message(app.getStrings().groupPropertiesNameCannotBeEmpty(), Luwrain.MessageType.ERROR);
	    return false;
	}
	group.setName(name);
	if (orderIndex.trim().isEmpty())
	{
	    app.message(app.getStrings().groupPropertiesInvalidOrderIndex(), Luwrain.MessageType.ERROR);
	    return false;
	}
	final int orderIndexValue;
	try {
	    orderIndexValue = Integer.parseInt(orderIndex.trim());
	}
	catch(NumberFormatException e)
	{
	    app.message(app.getStrings().groupPropertiesInvalidOrderIndex(), Luwrain.MessageType.ERROR);
	    return false;
	}
	if (orderIndexValue < 0)
	{
	    app.message(app.getStrings().groupPropertiesInvalidOrderIndex(), Luwrain.MessageType.ERROR);
	    return false;
	}
	group.setOrderIndex(orderIndexValue);
	final var urls = Arrays.asList(formArea.getMultilineEditText());
	group.setUrls(urls.stream()
		      .map(e -> e.trim())
		      .filter(e -> !e.isEmpty())
		      .toList());
	app.persist.getGroupDAO().update(group);
	return true;
    }
}
