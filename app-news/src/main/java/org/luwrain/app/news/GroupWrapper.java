// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.news;

import org.luwrain.core.*;
import org.luwrain.pim.news.persist.*;

final class GroupWrapper
{
    final Group group;
    final int newArticleCount;

    GroupWrapper(Group group, int newArticleCount)
    {
	this.group = group;
	this.newArticleCount = newArticleCount;
    }

    @Override public String toString()
    {
	if (group == null)
	    return "";
	if (newArticleCount == 0)
	    return group.getName();
	return group.getName() + " (" + String.valueOf(newArticleCount) + ")";
    }
}
