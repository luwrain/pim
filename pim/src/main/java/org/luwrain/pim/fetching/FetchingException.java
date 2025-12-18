// Copyright 2015 Roman Volovodov <gr.rPman@gmail.com>

package org.luwrain.pim.fetching;

import org.luwrain.pim.*;

public class FetchingException extends PimException
{
    public FetchingException(String message)
    {
	super(message);
    }

    public FetchingException(Exception e)
    {
	super(e.getMessage(), e);
    }
}
