package org.luwrain.pim.mail2.persistence;

import java.util.*;
import java.util.concurrent.atomic.*;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import org.luwrain.pim.mail2.persistence.model.*;
import org.luwrain.pim.mail2.persistence.dao.*;

public final class MailPersistence
{
    static private StandardServiceRegistry registry;
    static private SessionFactory sessionFactory;

    public interface Operation
    {
	void doOperation(Session session);
    }

    static public AccountDAO getAccountDAO()
    {
	return new AccountDAO(){
	    @Override public void add(Account account)
	    {
		trans(s -> s.save(account));
	    }
	        @Override public List<Account> getAll()
	    {
		final var res = new AtomicReference<List<Account>>();
		trans(s -> res.set(s.createQuery("FROM Account", Account.class).list()));
		return res.get();
	    }
	};
    }

        static public MessageDAO getMessageDAO()
    {
	return new MessageDAO(){
	    @Override public void add(MessageMetadata message)
	    {
		trans(s -> s.save(message));
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
