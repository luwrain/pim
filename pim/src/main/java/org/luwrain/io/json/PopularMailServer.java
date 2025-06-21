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

package org.luwrain.io.json;

import java.util.*;
import java.io.*;

import java.lang.reflect.*;
import lombok.*;
import com.google.gson.*;
import com.google.gson.reflect.*;

import org.luwrain.core.*;

@Data
@NoArgsConstructor
public final class PopularMailServer
{
        static public final Type LIST_TYPE = new TypeToken<List<PopularMailServer>>(){}.getType();
    static private Gson gson = null;

    @Data
@NoArgsConstructor
    static public final class Service
    {
	private String host;
	private int port;
	private boolean ssl, tls;
    }

    private List<String> suffixes;
    private Service smtp, pop3;

	static public String toJson(List<PopularMailServer> servers)
    {
	if (servers == null || servers.isEmpty())
	    return "[]";
	if (gson == null)
	    gson = new Gson();
	return gson.toJson(servers);
    }

    static public List<PopularMailServer> fromJson(String str)
    {
	if (str == null || str.trim().isEmpty())
	    return Arrays.asList();
	if (gson == null)
	    gson = new Gson();
	return gson.fromJson(str, LIST_TYPE);
    }

        static public List<PopularMailServer> fromJson(Reader r)
    {
	if (gson == null)
	    gson = new Gson();
	return gson.fromJson(r, LIST_TYPE);
    }

}
