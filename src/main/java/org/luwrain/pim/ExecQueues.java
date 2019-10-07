/*
   Copyright 2012-2019 Michael Pozhidaev <msp@luwrain.org>
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

package org.luwrain.pim;

import java.util.*;
import java.util.concurrent.*;

import org.luwrain.core.*;

public final class ExecQueues implements Runnable
{
    private final ConcurrentLinkedQueue<FutureTask> lowPriorityQueue = new ConcurrentLinkedQueue();
    private final ConcurrentLinkedQueue<FutureTask> highPriorityQueue = new ConcurrentLinkedQueue();
    private final Object syncObj = new Object();
    private final Executor executor = Executors.newSingleThreadExecutor();
    private Thread thread = null;
    private volatile boolean cancelling = false;

    public void enqueue(FutureTask task, boolean highPriority)
    {
	NullCheck.notNull(task, "task");
	if (highPriority)
	    enqueueHighPriority(task); else
	    enqueueLowPriority(task);
    }

    public void enqueueHighPriority(FutureTask task)
    {
	NullCheck.notNull(task, "task");
	synchronized(syncObj)
	{
	    highPriorityQueue.add(task);
	    syncObj.notifyAll();
	}
    }

    public void enqueueLowPriority(FutureTask task)
    {
	NullCheck.notNull(task, "task");
	synchronized(syncObj)
	{
	    lowPriorityQueue.add(task);
	    syncObj.notifyAll();
	}
    }

    public Object exec(FutureTask task, boolean highPriority) throws Exception
    {
	NullCheck.notNull(task, "task");
	if (highPriority)
	    return execHighPriority(task);
	return execLowPriority(task);
    }

    public Object execHighPriority(FutureTask task) throws Exception
    {
	NullCheck.notNull(task, "task");
	enqueueHighPriority(task);
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

    public Object execLowPriority(FutureTask task) throws Exception
    {
	NullCheck.notNull(task, "task");
	enqueueLowPriority(task);
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

    public void start()
    {
	if (thread != null)
	    throw new RuntimeException("Thread is already started");
	cancelling = false;
	thread = new Thread(this);
	thread.start();
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
		    return;
		}
	    }
	    if (cancelling)
		return;
	    if (cancelling)
		return;
	    while (!highPriorityQueue.isEmpty() || !lowPriorityQueue.isEmpty())
	    {
		if (cancelling)
		    return;
		while(!highPriorityQueue.isEmpty())
		{
		    final FutureTask task = highPriorityQueue.poll();
		    executor.execute(task);
		}
		if(!lowPriorityQueue.isEmpty())
		{
		    final FutureTask task = lowPriorityQueue.poll();
		    executor.execute(task);
		}
	    }
	}
    }
}
