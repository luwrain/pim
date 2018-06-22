/*
   Copyright 2012-2018 Michael Pozhidaev <michael.pozhidaev@gmail.com>

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

package org.luwrain.app.fetch;

import java.util.*;
import java.util.concurrent.*;

import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.controls.*;

public class Base
{
    public enum Type {
	NEWS,
	INCOMING_MAIL,
	OUTGOING_MAIL,
};

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private Luwrain luwrain;
    private Strings strings;
    private FutureTask task;

    boolean init(Luwrain luwrain, Strings strings)
    {
	NullCheck.notNull(luwrain, "luwrain");
	NullCheck.notNull(strings, "strings");
	this.luwrain = luwrain;
	this.strings = strings;
	return true;
    }

    String[] prepareIntroduction(Set<Type> fetchType)
    {
	NullCheck.notNull(fetchType, "fetchType");
	final String text;
	if (fetchType.contains(Base.Type.NEWS) && fetchType.contains(Base.Type.INCOMING_MAIL) && fetchType.contains(Base.Type.OUTGOING_MAIL))
	text = strings.introductionAll(); else 
	    if (fetchType.contains(Base.Type.INCOMING_MAIL) && fetchType.contains(Base.Type.OUTGOING_MAIL))
						  text = strings.introductionMail(); else
		if (fetchType.contains(Base.Type.NEWS))
											     text = strings.introductionNews(); else
											     if (fetchType.contains(Base.Type.INCOMING_MAIL))
												 text = strings.introductionIncomingMail(); else
												 if (fetchType.contains(Base.Type.OUTGOING_MAIL))
												 text = strings.introductionOutgoingMail(); else
																		return new String[0]; 
	final LinkedList<String> lines = new LinkedList<String>();
	lines.add("");
	for (String s: text.split("\n", -1))
	    lines.add(s);
	lines.add("");
	return lines.toArray(new String[lines.size()]);
    }

    boolean fetchingInProgress()
    {
	return task != null && !task.isDone();
    }

    boolean start(ProgressArea destArea, Set<Type> fetchType)
    {
	NullCheck.notNull(destArea, "destArea");
	NullCheck.notNull(fetchType, "fetchType");
	if (fetchingInProgress())
	    return false;
destArea.clear();
task = constructTask(destArea, fetchType);
executor.execute(task);
luwrain.onAreaNewBackgroundSound(destArea);
return true;
    }

    boolean interrupt()
    {
	if (task == null)
	    return false;
	task.cancel(true);
	return true;
    }

    private FutureTask constructTask(ProgressArea messageArea, Set<Type> fetchType)
    {
	NullCheck.notNull(messageArea, "messageArea");
	NullCheck.notNull(fetchType, "fetchType");
	return new FutureTask(new WorkerControl(){
		@Override public void run()
		{
		    message(strings.startingNewsFetching());
		    try {
			//			if (fetchType.contains(Type.INCOMING_MAIL) || fetchType.contains(Type.OUTGOING_MAIL))
			    //			    new Mail(luwrain, this, strings).work(fetchType.contains(Type.INCOMING_MAIL), fetchType.contains(Type.OUTGOING_MAIL));
			if (fetchType.contains(Type.NEWS))
			    new News(this, strings).work();
			message(strings.fetchingCompleted());
			luwrain.runUiSafely(()->{
				luwrain.onAreaNewBackgroundSound(messageArea);
				luwrain.message(strings.fetchingCompleted(), Luwrain.MessageType.DONE);
			    });
		    }
		    catch(InterruptedException e)
		    {
			message(strings.interrupted());
			luwrain.runUiSafely(()->luwrain.message(strings.interrupted(), Luwrain.MessageType.DONE));
		    }
		    catch(Exception e)
		    {
			luwrain.crash(e);
		    }
		}
		@Override public void message(String text)
		{
		    if (text != null && !text.trim().isEmpty())
			luwrain.runUiSafely(()->messageArea.addProgressLine(text));
		}
		@Override public Luwrain luwrain()
		{
		    return luwrain;
		}
		@Override public void checkInterrupted() throws InterruptedException
		{
		    if (Thread.currentThread().interrupted())
			throw new InterruptedException();
		}
	    }, null);
    }
}
