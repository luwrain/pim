/*
   Copyright 2012-2022 Michael Pozhidaev <msp@luwrain.org>
   Copyright 2015 Roman Volovodov <gr.rPman@gmail.com>

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

package org.luwrain.io.json;

import java.util.*;

import lombok.*;
import com.google.gson.*;
import com.google.gson.annotations.*;

import org.luwrain.core.*;

@Data
@NoArgsConstructor
public final class Message
{
    static private Gson gson = null;

    private String
	to = null,
	cc = null,
	subject = null;

    private List<String>
	text = null,
	headers = null,
	attachments = null;

    @Override public String toString()
    {
	if (gson == null)
	    gson = new Gson();
	return gson.toJson(this);
    }

    static public Message fromString(String str)
    {
	if (gson == null)
	    gson = new Gson();
	return gson.fromJson(str, Message.class);
    }
}
