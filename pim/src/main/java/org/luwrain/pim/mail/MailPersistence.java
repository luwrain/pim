/*
   Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>
   Copyright 2015 Roman Volovodov <gr.rPman@gmail.com>

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

package org.luwrain.pim.mail;

import org.luwrain.pim.storage.*;

import static java.util.Objects.*;
import static org.luwrain.pim.storage.ExecQueues.*;

public final class MailPersistence
{
    final ExecQueues queues;
    private Priority priority = Priority.MEDIUM;

    MailPersistence(ExecQueues queues)
    {
	this.queues = requireNonNull(queues, "queues can't be null");
    }

    public void setPriority(Priority priority)
    {
	this.priority = requireNonNull(priority, "priority can't be null");
    }
}
