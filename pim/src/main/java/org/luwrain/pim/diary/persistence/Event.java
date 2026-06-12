// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.pim.diary.persistence;

import java.util.*;
import java.io.*;
import lombok.*;

@Data
public class Event implements Serializable
{
    private long id;
    private String title, comment;
    private long dateTime;
    private int durationMin;
    private List<String> references;

    @Override public boolean equals(Object o)
    {
	if (o != null && o instanceof Event e)
	    return id == e.id;
	return false;
    }

    @Override public int hashCode()
    {
	return Long.hashCode(id);
    }

    @Override public String toString()
    {
	return title != null?title:"";
    }
}
