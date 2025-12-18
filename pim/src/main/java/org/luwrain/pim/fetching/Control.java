// Copyright 2015 Roman Volovodov <gr.rPman@gmail.com>

package org.luwrain.pim.fetching;

import org.luwrain.core.Luwrain;

public interface Control
{
    void message(String text);
    Luwrain luwrain();
    void checkInterrupted() throws InterruptedException;
}
