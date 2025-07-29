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

package org.luwrain.pim.mail.persistence;

import java.util.*;

import org.luwrain.pim.storage.*;
import org.luwrain.pim.mail.persistence.dao.*;
import org.luwrain.pim.mail.persistence.model.*;

import static java.util.Objects.*;
import static org.luwrain.pim.storage.ExecQueues.*;

public final class MailPersistence
{
    final ExecQueues queues;
    private Priority priority = Priority.MEDIUM;

    public MailPersistence(ExecQueues queues)
    {
	this.queues = requireNonNull(queues, "queues can't be null");
    }

public AccountDAO getAccountDAO()
    {
	return new AccountDAO(){
	    @Override public Account getById(int id)
	    {
		return null;
					    }
	    
	    	    	    @Override public void delete(Account account)
	    {
	    }
	    
	    @Override public void add(Account account)
	    {
	    }
	        @Override public List<Account> getAll()
	    {
		return null;
			    }
	    
	    	        @Override public void update(Account account)
	    {
			    }
	};
    }

public FolderDAO getFolderDAO()
    {
	return new FolderDAO(){
	    @Override public void add(Folder folder)
	    {
			    }
	    
	    	        @Override public List<Folder> getAll()
	    {
		return null;
			    }
	    
	    	    	        @Override public List<Folder> getChildFolders(Folder folder)
	    {
		return null;
	    }
	    
	    	    @Override public void update(Folder folder)
	    {
	    }
	    
	    @Override public Folder getRoot()
	    {
		return null;
			    }
	    
	    	    	    	        @Override public void setRoot(Folder folder)
	    {
	    }
	    
	    @Override public Folder findFirstByProperty(String propName, String propValue)
	    {
		return null;
	    }
	};
    }

public MessageDAO getMessageDAO()
    {
	return new MessageDAO(){
	    @Override public void add(MessageMetadata message)
	    {
			    }
	    
	    	    @Override public void delete(MessageMetadata message)
	    {
	    }
	    
	    	        @Override public List<MessageMetadata> getAll()
	    {
		return null;
			    }
	    	    	        @Override public List<MessageMetadata> getByFolderId(int folderId)
	    {
		return null;
			    }
	    
	    @Override public void update(MessageMetadata message)
	    {
    }

void deleteAllFolders()
    {

    }

void deleteAllAccounts()
    {
}
	};
	}
    


    

    public void setPriority(Priority priority)
    {
	this.priority = requireNonNull(priority, "priority can't be null");
    }
}
