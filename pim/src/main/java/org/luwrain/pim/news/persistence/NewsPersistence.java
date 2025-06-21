
package org.luwrain.pim.news.persistence;

import java.util.*;
import java.util.concurrent.atomic.*;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import org.luwrain.pim.news.persistence.model.*;
import org.luwrain.pim.news.persistence.dao.*;

import static org.luwrain.core.NullCheck.*;

public final class NewsPersistence
{
    static private StandardServiceRegistry registry;
    static private SessionFactory sessionFactory;

    public interface Operation
    {
	void doOperation(Session session);
    }

    static public GroupDAO getGroupDAO()
    {
	return new GroupDAO(){
	    @Override public void add(Group group)
	    {
		trans(s -> s.save(group));
	    }
	        @Override public List<Group> getAll()
	    {
		final var res = new AtomicReference<List<Group>>();
		trans(s -> res.set(s.createQuery("FROM Group", Group.class).list()));
		return res.get();
	    }
	    	        @Override public void update(Group group)
	    {
		notNull(group, "group");
		trans(s -> s.merge(group));
	    }
	};
    }

    static void trans(Operation operation)
    {
	Transaction transaction = null;
        try (final var session = getSessionFactory().openSession())
	{
            transaction = session.beginTransaction();
	    operation.doOperation(session);
            transaction.commit();
        }
	catch (Exception e)
	{
	    e.printStackTrace();
            if (transaction != null) 
                transaction.rollback();
	    throw new RuntimeException(e);
        }
    }

    public static SessionFactory getSessionFactory() throws Exception
    {
        if (sessionFactory != null)
	    return sessionFactory;
	try {
	    registry = new StandardServiceRegistryBuilder().configure("mail.conf").build();
	    final MetadataSources sources = new MetadataSources(registry);
	    final Metadata metadata = sources.getMetadataBuilder().build();
	    sessionFactory = metadata.getSessionFactoryBuilder().build();
	    return sessionFactory;
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	    shutdown();
	    throw e;
	}
    }

    static public void shutdown()
    {
        if (registry == null)
	    return;
	StandardServiceRegistryBuilder.destroy(registry);
	registry = null;
    }
}
