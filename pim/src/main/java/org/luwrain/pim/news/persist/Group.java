// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>
// Copyright 2015 Roman Volovodov <gr.rPman@gmail.com>

package org.luwrain.pim.news.persist;

import java.util.*;
import java.io.*;
import lombok.*;

/**
 * A group of news feeds stored in the persistent news storage.
 *
 * <p>A group aggregates one or more feed URLs and holds the settings that
 * control how the articles fetched from those feeds are presented and
 * retained. Each group is identified by {@link #id}; two groups are equal
 * when their identifiers match.</p>
 */
@Data
@NoArgsConstructor
public class Group implements Serializable
{
    /** The unique identifier of the group. */
    private int id;

    /** The human-readable name of the group. */
    private String name;

    /** The media content type associated with the group, if any. */
    private String mediaContentType;

    /** The position of the group when it is displayed in an ordered list. */
    private int orderIndex;

    /** The number of days after which fetched articles should be expired. */
    private int expireAfterDays;

    /** The list of feed URLs belonging to this group. */
    private List<String> urls;

    /** Whether the group should always be shown even when it has no articles. */
    private boolean alwaysShow = false;

    @Override public boolean equals(Object o)
    {
	if (o != null && o instanceof Group g)
	    return id == g.id;
	return false;
    }

    @Override public int hashCode()
    {
	return id;
    }
}
