
package org.luwrain.pim.news.persistence;

import java.util.*;
import java.util.concurrent.atomic.*;

//import org.hibernate.Session;
//import org.hibernate.SessionFactory;
//import org.hibernate.Transaction;
//import org.hibernate.boot.Metadata;
//import org.hibernate.boot.MetadataSources;
//import org.hibernate.boot.registry.StandardServiceRegistry;
//import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import org.luwrain.pim.news.persistence.model.*;
import org.luwrain.pim.news.persistence.dao.*;

import static org.luwrain.core.NullCheck.*;

public final class NewsPersistence
{
    static public GroupDAO getGroupDAO()
    {
	return new GroupDAO(){
	    @Override public void add(Group group)
	    {

	    }
	    
	        @Override public List<Group> getAll()
	    {
		return null;
	    }
	    
	    	        @Override public void update(Group group)
	    {
	    }
	};
    }

}
