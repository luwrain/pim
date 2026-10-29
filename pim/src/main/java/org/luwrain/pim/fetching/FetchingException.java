// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>
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
