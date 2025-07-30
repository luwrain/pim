/*
   Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

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

package org.luwrain.pim.news.persistence.model;

import java.util.*;
import java.io.*;
import lombok.*;

@Data
@NoArgsConstructor
public class Article implements Serializable
{
    public enum Status { NEW, READ, MARKED };

    private long id;
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
