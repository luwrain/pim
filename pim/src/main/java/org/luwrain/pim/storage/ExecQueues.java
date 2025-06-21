/*
   Copyright 2012-2024 Michael Pozhidaev <msp@luwrain.org>
   Copyright 2015 Roman Volovodov <gr.rPman@gmail.com>

   This file is part of LUWRAIN.

   LUWRAIN is free software; you can redistribute it and/or
   modify it under the terms of the GNU General Public
   License as published by the Free Software Foundation; either
   version 3 of the License, or (at your option) any later version.

   LUWRAIN is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
   General Public License for more details.
*/

package org.luwrain.pim.storage;

import java.util.*;
import java.util.concurrent.*;

import org.luwrain.core.*;

import static org.luwrain.core.NullCheck.*;

final class ExecQueues implements Runnable
{
    private final ConcurrentLinkedQueue<FutureTask> lowPriorityQueue = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<FutureTask> highPriorityQueue = new ConcurrentLinkedQueue<>();
    private final Object syncObj = new Object();
    private final Thread thread;
    private volatile boolean cancelling = false;

    public ExecQueues()
    {
	cancelling = false;
	thread = new Thread(this);
	thread.start();
    }

    public <T> T execHighPriority(FutureTask<T> task) throws Exception
    {
notNull(task, "task");
		synchronized(syncObj)
	{
	    lowPriorityQueue.add(task);
	    syncObj.notifyAll();
	}
	try {
	    return task.get();
	}
	catch(ExecutionException e)
	{
	    if (e.getCause() instanceof Exception)
		throw (Exception)e.getCause();
	    throw e;
	}
	catch(InterruptedException e)
	{
	    Thread.currentThread().interrupt();
	    return null;
	}
    }


    public void cancel()
    {
	synchronized(syncObj)
	{
	    cancelling = true;
	    syncObj.notifyAll();
	}
    }

    @Override public void run()
    {
	while(true)
	{
	    synchronized(syncObj)
	    {
		try {
		    while (highPriorityQueue.isEmpty() && lowPriorityQueue.isEmpty() && !cancelling)
			syncObj.wait();
		}
		catch(InterruptedException e)
		{
		    Thread.currentThread().interrupt();
		    return;
		}
	    }
	    if (cancelling)
		return;
	    while (!highPriorityQueue.isEmpty() || !lowPriorityQueue.isEmpty())
	    {
		if (cancelling)
		    return;
		while(!highPriorityQueue.isEmpty())
		{
		    final FutureTask task = highPriorityQueue.poll();
		    task.run();
		}
		if(!lowPriorityQueue.isEmpty())
		{
		    final FutureTask task = lowPriorityQueue.poll();
		    task.run();
		}
	    }
	}
    }
}
