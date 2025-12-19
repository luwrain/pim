// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.pim;

public class PimException extends RuntimeException
{
    public PimException(Throwable cause)
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
