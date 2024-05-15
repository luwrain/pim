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

import java.util.*;
import java.io.*;
import org.apache.logging.log4j.*;

import org.luwrain.io.json.PopularMailServer;

public final class PopularServers
{
    static private final Logger log = LogManager.getLogger();

    static public List<PopularMailServer> getPopularMailServers()
    {
	try {
	    try(final var r = new BufferedReader(new InputStreamReader(PopularServers.class.getResourceAsStream("popular-servers.json"), "UTF-8"))){
		return PopularMailServer.fromJson(r);
	    }
	}
	catch(IOException ex)
	{
	    log.catching(ex);
	    throw new RuntimeException(ex);
	}
    }
}
