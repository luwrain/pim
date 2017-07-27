
package org.luwrain.pim;

import org.luwrain.core.*;

public class SQL
{
    static public String prepareUrl(Luwrain luwrain, String url)
    {
	NullCheck.notEmpty(url, "url");
	NullCheck.notNull(luwrain, "luwrain");
	String userData = "";
	try {
	    userData = luwrain.getFileProperty("luwrain.dir.userdata").toPath().toUri().toURL().toString();
	    if (userData.startsWith("file:"))
		userData = userData.substring("file:".length());
	}
	catch(java.net.MalformedURLException e)
	{
	    userData = luwrain.getFileProperty("luwrain.dir.userdata").toPath().toString();
	}
	if (userData.length() > 1 && userData.endsWith("/"))
	    userData = userData.substring(0, userData.length() - 1);
	String res = url;
	res = res.replaceAll("\\$userdata", userData);
	return res;

    }
}
