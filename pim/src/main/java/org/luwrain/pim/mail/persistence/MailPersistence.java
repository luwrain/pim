package org.luwrain.pim.mail.persistence;

import java.util.*;
import java.util.concurrent.atomic.*;

import org.luwrain.pim.mail.persistence.model.*;
import org.luwrain.pim.mail.persistence.dao.*;

import static org.luwrain.core.NullCheck.*;

public final class MailPersistence
{
    static public AccountDAO getAccountDAO()
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

            static public FolderDAO getFolderDAO()
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

        static public MessageDAO getMessageDAO()
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

    static void deleteAllFolders()
    {

    }

        static void deleteAllAccounts()
    {
}
	};
	}
    
}

