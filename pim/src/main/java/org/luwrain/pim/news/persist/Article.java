// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>
// Copyright 2015 Roman Volovodov <gr.rPman@gmail.com>

package org.luwrain.pim.news.persist;

import java.io.*;
import lombok.*;

@Data
@NoArgsConstructor
public class Article implements Serializable
{
    public enum Status { NEW, READ, MARKED };

    private long id;
    private int groupId;
    private Status status;
    private String sourceUrl, sourceTitle, uri, title, extTitle, url, descr, author, categories;
    private long publishedTimestamp, updatedTimestamp;

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
