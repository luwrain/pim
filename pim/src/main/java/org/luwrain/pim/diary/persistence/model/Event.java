// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.pim.diary.persistence.model;

import java.util.*;
import lombok.*;
//import jakarta.persistence.*;

@Data
public class Event
{
    private String title, comment;
    private long dateTime;
    private int durationMin;

    @Override public boolean equals(Object o)
    {
	/*
	if (o != null && o instanceof Event e)
	    return id == e.id;
	*/
	return false;
    }

    @Override public int hashCode()
    {
	return 0;//id;
    }

    @Override public String toString()
    {
	return title != null?title:"";
    }
}
