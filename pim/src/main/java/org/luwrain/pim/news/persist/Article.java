// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>
// Copyright 2015 Roman Volovodov <gr.rPman@gmail.com>

package org.luwrain.pim.news.persist;

import java.io.*;
import lombok.*;

/**
 * A single news article stored in the persistent news storage.
 *
 * <p>An article belongs to exactly one {@link Group} and carries both the
 * metadata obtained from the feed (title, URI, author, timestamps and so on)
 * and the article body itself. The {@code content} field is intentionally
 * excluded from {@code toString()} because it may be large.</p>
 *
 * <p>Instances are identified by {@link #id}. Two articles are considered
 * equal when their identifiers match, regardless of any other field.</p>
 */
@Data
@NoArgsConstructor
public class Article implements Serializable
{
    /**
     * The lifecycle state of an article.
     */
    public enum Status {
	/** The article has been fetched but not yet read by the user. */
	NEW,
	/** The article has been read by the user. */
	READ,
	/** The article has been explicitly marked by the user. */
	MARKED
    };

    /** The unique identifier of the article. */
    private long id;

    /** The identifier of the {@link Group} this article belongs to. */
    private int groupId;

    /** The current lifecycle state of the article. */
    private Status status;

    /** The URL of the feed this article was fetched from. */
    private String sourceUrl;

    /** The title of the feed this article was fetched from. */
    private String sourceTitle;

    /** The unique URI identifying the article within its feed. */
    private String uri;

    /** The article title. */
    private String title;

    /** An additional (external) title, if any. */
    private String extTitle;

    /** The URL the article points to. */
    private String url;

    /** A short description or summary of the article. */
    private String descr;

    /** The author of the article. */
    private String author;

    /** The categories the article is assigned to. */
    private String categories;

    /** The publication timestamp, in milliseconds since the epoch. */
    private long publishedTimestamp;

    /** The last update timestamp, in milliseconds since the epoch. */
    private long updatedTimestamp;

    /** The full article body. Excluded from {@code toString()} as it may be large. */
    @ToString.Exclude
    private String content;

    @Override public boolean equals(Object o)
    {
	if (o != null && o instanceof Article a)
	    return id == a.id;
	return false;
    }

    @Override public int hashCode()
    {
	return Long.valueOf(id).intValue();
    }
}
