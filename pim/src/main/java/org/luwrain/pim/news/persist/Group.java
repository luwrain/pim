// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>
// Copyright 2015 Roman Volovodov <gr.rPman@gmail.com>

package org.luwrain.pim.news.persist;

import java.util.*;
import java.io.*;
import lombok.*;

@Data
@NoArgsConstructor
public class Group implements Serializable
{
    private int id;
    private String name, mediaContentType;
    private int orderIndex, expireAfterDays;
    private List<String> urls;

    @Override public boolean equals(Object o)
    {
	/*
	if (o != null && o instanceof Group g)
	    return id == g.id;
	*/
	return false;
    }

    @Override public int hashCode()
    {
	return 0;//id;
    }

    @Override public String toString()
    {
	return name != null?name:"";
    }
}
