

package org.luwrain.pim.diary.persistence;

import java.util.*;
import java.util.concurrent.atomic.*;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import org.luwrain.pim.diary.persistence.model.*;
import org.luwrain.pim.diary.persistence.dao.*;

public final class DiaryPersistence
{
    static private StandardServiceRegistry registry;
    static private SessionFactory sessionFactory;

    public interface Operation
    {
	void doOperation(Session session);
    }

    static public EventDAO getEventDAO()
    {
	return new EventDAO(){
	    @Override public void add(Event event)
	    {
		trans(s -> s.save(event));
	    }
	        @Override public List<Event> getAll()
	    {
		final var res = new AtomicReference<List<Event>>();
		trans(s -> res.set(s.createQuery("FROM Event", Event.class).list()));
		return res.get();
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
	    registry = new StandardServiceRegistryBuilder().configure("diary.conf").build();
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
