
package org.luwrain.network.fetching;

import org.luwrain.core.Luwrain;

interface WorkerControl extends Runnable
{
    void message(String text);
    Luwrain luwrain();
    void checkInterrupted() throws InterruptedException;
}
