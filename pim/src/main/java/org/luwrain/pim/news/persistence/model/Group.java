/*
   Copyright 2012-2024 Michael Pozhidaev <msp@luwrain.org>

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
import lombok.*;
//import jakarta.persistence.*;

@Data
@NoArgsConstructor
public class Group
{
    private String name, mediaContentType;
    private int orderIndex, expireAfterDays;

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
