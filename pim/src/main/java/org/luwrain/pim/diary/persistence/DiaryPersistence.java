// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.pim.diary.persistence;

import java.util.*;
import java.util.concurrent.atomic.*;

//import org.luwrain.pim.diary.persistence;
//import org.luwrain.pim.diary.persistence.dao.*;

public final class DiaryPersistence
{
    static public EventDAO getEventDAO()
    {
	return new EventDAO(){
	    @Override public void add(Event event)
	    {

	    }
	    
	        @Override public List<Event> getAll()
	    {
		return null;
	    }
	};
    }
    }
