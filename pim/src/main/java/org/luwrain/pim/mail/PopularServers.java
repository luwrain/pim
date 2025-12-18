
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
