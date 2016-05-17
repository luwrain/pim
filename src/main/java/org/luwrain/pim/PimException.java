
package org.luwrain.pim;

public class PimException extends Exception
{
    public PimException(Exception cause)
    {
	super(cause.getMessage(), cause);
    }

    public PimException(String message, Exception cause)
    {
	super(message, cause);
    }

    public PimException(String message)
    {
	super(message);
    }
} 
