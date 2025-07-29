/*
   Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>
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
import static java.util.Objects.*;

public final class ExecQueues implements Runnable, AutoCloseable
{
    public enum Priority {HIGH, MEDIUM};

    private final ConcurrentLinkedQueue<FutureTask>
	mediumPriorityQueue = new ConcurrentLinkedQueue<>(),
	highPriorityQueue = new ConcurrentLinkedQueue<>();
    private final Object syncObj = new Object();
    private final Thread thread;
    private volatile boolean cancelling = false;

    public ExecQueues()
    {
	cancelling = false;
	thread = new Thread(this);
	thread.start();
    }

    public <T> T exec(Priority priority, FutureTask<T> task) throws Exception
    {
	requireNonNull(priority, "priority");
requireNonNull(task, "task can't be null");
		synchronized(syncObj)
	{
	    switch(priority)
	    {
		case MEDIUM:
	    mediumPriorityQueue.add(task);
	    break;
	    case HIGH:
			    highPriorityQueue.add(task);
			    break;
	    }
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

    @Override public void close()
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
		    while (highPriorityQueue.isEmpty() && mediumPriorityQueue.isEmpty() && !cancelling)
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
	    while (!highPriorityQueue.isEmpty() || !mediumPriorityQueue.isEmpty())
	    {
		if (cancelling)
		    return;
		while(!highPriorityQueue.isEmpty())
		{
		    final var task = highPriorityQueue.poll();
		    task.run();
		}
		if(!mediumPriorityQueue.isEmpty())
		{
		    final var task = mediumPriorityQueue.poll();
		    task.run();
		}
	    }
	}
    }

    static public final class Runner
    {
	final ExecQueues queues;
	final Priority priority;

	public Runner(ExecQueues queues, Priority priority)
	{
	    this.queues = requireNonNull(queues, "queues can't be null");
	    this.priority = requireNonNull(priority, "[priority can't be null");
	}

	public <T> T run(FutureTask<T> task)
	{
	    requireNonNull(task, "task can't be null");
	    try {
		return queues.exec(priority, task);
	    }
	    catch(Exception ex)
	    {
		throw new RuntimeException(ex);
	    }
	}
    }
}
