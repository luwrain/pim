
package org.luwrain.pim.util;

import java.util.*;
import java.util.concurrent.*;

import org.luwrain.core.*;

public final class ExecQueues implements Runnable
{
    private final LinkedList<FutureTask> lowPriorityQueue = new LinkedList();
    private final LinkedList<FutureTask> highPriorityQueue = new LinkedList();
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
	highPriorityQueue.addLast(task);
	synchronized(syncObj)
	{
	    syncObj.notifyAll();
	}
    }

            public void enqueueLowPriority(FutureTask task)
    {
	NullCheck.notNull(task, "task");
	lowPriorityQueue.addLast(task);
	synchronized(syncObj)
	{
	    syncObj.notifyAll();
	}
    }

    public Object exec(FutureTask task, boolean highPriority) throws Throwable
    {
	NullCheck.notNull(task, "task");
	if (highPriority)
	    return execHighPriority(task);
	    return execLowPriority(task);
    }

	public Object execHighPriority(FutureTask task) throws Throwable
    {
	NullCheck.notNull(task, "task");
	enqueueHighPriority(task);
	try {
	    return task.get();
	}
	catch(ExecutionException e)
	{
	    throw e.getCause();
	}
	catch(InterruptedException e)
	{
	    Thread.currentThread().interrupt();
	    return null;
	}
    }

    public Object execLowPriority(FutureTask task) throws Throwable
    {
	NullCheck.notNull(task, "task");
	enqueueLowPriority(task);
	try {
	    return task.get();
	}
	catch(ExecutionException e)
	{
	    throw e.getCause();
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
	cancelling = true;
	synchronized(syncObj)
	{
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
		    syncObj.wait();
		}
		catch(InterruptedException e)
		{
		    return;
		}
		if (highPriorityQueue.isEmpty() && lowPriorityQueue.isEmpty())
		    continue;
	    }
	    if (cancelling)
		return;
	    while(!highPriorityQueue.isEmpty())
	    {
		final FutureTask task = highPriorityQueue.pollFirst();
		executor.execute(task);
	    }
	    if (cancelling)
		return;
	    while(!lowPriorityQueue.isEmpty())
	    {
		final FutureTask task = lowPriorityQueue.pollFirst();
		executor.execute(task);
	    }
	    if (cancelling)
		return;
	}
    }
}
