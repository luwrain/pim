// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.mail;

import org.luwrain.pim.mail.*;

final class SummaryItem
{
    enum Type {SECTION, MESSAGE};

    final Type type;
    final String title;
    final Message message;

    SummaryItem(String sectName)
    {
	this.type = Type.SECTION;
	this.title = sectName;
	this.message = null;
    }

    SummaryItem(Message message)
    {
	this.type = Type.MESSAGE;
	this.title = message.getMetadata().getFromAddr();
	this.message = message;
    }

    @Override public String toString()
    {
	return title != null?title:"";
    }
}
