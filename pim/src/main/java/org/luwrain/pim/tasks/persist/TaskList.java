// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.pim.tasks.persist;

import java.util.*;
import java.io.*;
import lombok.*;

/**
 * A list of tasks stored in the persistent task storage.
 *
 * <p>A task list aggregates a set of related tasks and holds the settings
 * that control how those tasks are presented and retained. Each task list
 * is identified by {@link #id}; two task lists are equal when their
 * identifiers match.</p>
 */
@Data
@NoArgsConstructor
public class TaskList implements Serializable
{
    /** The unique identifier of the task list. */
    private int id;

    /** The human-readable name of the task list. */
    private String name;

    /** The position of the task list when it is displayed in an ordered list. */
    private int orderIndex;

    /** The color associated with the task list, if any. */
    private String color;

    /** Whether the task list should always be shown even when it has no tasks. */
    private boolean alwaysShow = false;

    @Override public boolean equals(Object o)
    {
	if (o != null && o instanceof TaskList t)
	    return id == t.id;
	return false;
    }

    @Override public int hashCode()
    {
	return id;
    }
}
