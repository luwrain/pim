/*
   Copyright 2012-2024 Michael Pozhidaev <msp@luwrain.org>
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

package org.luwrain.pim.mail;

import java.io.*;
import java.util.*;
import java.lang.reflect.*;

import lombok.*;
import com.google.gson.*;
import com.google.gson.reflect.*;

import org.apache.logging.log4j.*;
import javax.mail.*;
import javax.mail.internet.*;

import static javax.mail.internet.MimeUtility.*;

@Data
@NoArgsConstructor
public final class MessageContentItem
{
    static final Type LIST_TYPE = new TypeToken<List<MessageContentItem>>(){}.getType();
    static final Gson gson = new Gson();

    private String contentType, disposition, fileName, text;
    private boolean alternative;

    static public String toJson(List<MessageContentItem> items)
    {
	if (items == null)
	    return "[]";
	return gson.toJson(items);
    }

    static public List<MessageContentItem> fromJson(String s)
    {
	if (s == null || s.isEmpty())
	    return Arrays.asList();
	final List<MessageContentItem> r = gson.fromJson(s, LIST_TYPE);
	if (r == null)
	    return Arrays.asList();
	return new ArrayList<MessageContentItem>(r);
	
    }
}
