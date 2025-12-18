// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>
// Copyright 2015 Roman Volovodov <gr.rPman@gmail.com>

package org.luwrain.pim.news;

import org.luwrain.core.*;

public class MediaResource
{
    private String url = "";

    public String getUrl()
    {
	return this.url;
    }

    public void setUrl(String value)
    {
	NullCheck.notNull(value, "value");
	this.url = value;
    }
}
