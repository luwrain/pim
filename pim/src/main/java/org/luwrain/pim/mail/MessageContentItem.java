// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>
// Copyright 2015 Roman Volovodov <gr.rPman@gmail.com>

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
