
//LWR_API 1.0

package org.luwrain.pim.mail;

import java.io.*;
import java.util.*;

import org.luwrain.core.*;
import org.luwrain.pim.*;

public class MailFolder implements Comparable
{
    private String title = "";
    private int orderIndex = 0;
    private final Properties props = new Properties();

    public void setTitle(String title) throws PimException
    {
	NullCheck.notNull(title, "title");
	this.title = title;
    }

    public String getTitle() throws PimException
    {
	return this.title;
    }

    public void setOrderIndex(int orderIndex) throws PimException
    {
	if (orderIndex < 0)
	    throw new IllegalArgumentException("orderIndex (" + String.valueOf(orderIndex) + ") may not be negative");
	this.orderIndex = orderIndex;
    }

    public int getOrderIndex() throws PimException
    {
	return this.orderIndex;
    }

    public Properties getProperties() throws PimException
    {
	return this.props;
    }

    public void saveProperties() throws PimException
    {
    }

    public final String getPropertiesAsString() throws IOException
    {
	final StringWriter writer = new StringWriter();
	props.store(writer, "");
	writer.flush();
	return writer.toString();
    }

    public final void setPropertiesFromString(String text) throws IOException
    {
	NullCheck.notNull(text, "text");
	final StringReader reader = new StringReader(text);
	props.clear();
	props.load(reader);
    }

    @Override public String toString()
    {
	return title != null?title:"";
    }

    @Override public int compareTo(Object o)
    {
	if (o == null || !(o instanceof MailFolder))
	    return 0;
	final MailFolder folder = (MailFolder)o;
	if (orderIndex < folder.orderIndex)
	    return -1;
	if (orderIndex > folder.orderIndex)
	    return 1;
	return 0;
    }
}
