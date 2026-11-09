// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.pim.tasks.persist;

import java.io.*;
import java.util.*;
import lombok.*;

/**
 * A single task stored in the persistent task storage.
 *
 * <p>A task belongs to exactly one {@link TaskList} and carries both the
 * metadata describing the task (title, description, priority, due date and
 * so on) and the task body itself. The {@code content} field is
 * intentionally excluded from {@code toString()} because it may be large.</p>
 *
 * <p>Instances are identified by {@link #id}. Two tasks are considered
 * equal when their identifiers match, regardless of any other field.</p>
 */
@Data
@NoArgsConstructor
public class Task implements Serializable
{
    /**
     * The lifecycle state of a task.
     */
    public enum Status {
	/** The task has been created but not yet started. */
	NEW,
	/** The task is currently being worked on. */
	IN_PROGRESS,
	/** The task has been completed. */
	DONE,
	/** The task has been cancelled. */
	CANCELLED
    };

    /**
     * The priority of a task.
     */
    public enum Priority {
	/** The lowest priority. */
	LOW,
	/** The normal priority. */
	NORMAL,
	/** The high priority. */
	HIGH,
	/** The urgent priority. */
	URGENT
    };

    /** The unique identifier of the task. */
    private long id;

    /** The identifier of the {@link TaskList} this task belongs to. */
    private int listId;

    /** The current lifecycle state of the task. */
    private Status status;

    /** The priority of the task. */
    private Priority priority;

    /** The task title. */
    private String title;

    /** A short description or summary of the task. */
    private String descr;

    /** The full task body. Excluded from {@code toString()} as it may be large. */
    @ToString.Exclude
    private String content;

    /** The due date timestamp, in milliseconds since the epoch, or -1 if unset. */
    private long dueTimestamp;

    /** The creation timestamp, in milliseconds since the epoch. */
    private long createdTimestamp;

    /** The completion timestamp, in milliseconds since the epoch, or -1 if unset. */
    private long completedTimestamp;

    /** The tags assigned to the task. */
    private List<String> tags;

    @Override public boolean equals(Object o)
    {
	if (o != null && o instanceof Task t)
	    return id == t.id;
	return false;
    }

    @Override public int hashCode()
    {
	return Long.valueOf(id).intValue();
    }
}
