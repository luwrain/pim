
//LWR_API 1.0

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
