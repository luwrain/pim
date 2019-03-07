
//LWR_API 1.0

package org.luwrain.pim.fetching;

import org.luwrain.core.Luwrain;

public interface Control
{
    void message(String text);
    Luwrain luwrain();
    void checkInterrupted() throws InterruptedException;
}
