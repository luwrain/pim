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

import static org.luwrain.core.NullCheck.*;

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

            static public FolderDAO getFolderDAO()
    {
	return new FolderDAO(){
	    @Override public void add(Folder folder)
	    {
		trans(s -> s.save(folder));
	    }
	    	        @Override public List<Folder> getAll()
	    {
		final var res = new AtomicReference<List<Folder>>();
		trans(s -> res.set(s.createQuery("FROM Folder", Folder.class).list()));
		return res.get();
	    }
	    	    	        @Override public List<Folder> getChildFolders(Folder folder)
	    {
		notNull(folder, "folder");
		final var res = new AtomicReference<List<Folder>>();
		trans(s -> res.set(s.createQuery("FROM Folder WHERE parentFolderId = :parent_id AND parentFolderId <> id", Folder.class).setParameter("parent_id", folder.getId()).list()));
		return res.get();
	    }

	    @Override public Folder getRoot()
	    {
		final var res = new AtomicReference<List<Folder>>();
		trans(s -> res.set(s.createQuery("FROM Folder WHERE id = parentFolderId", Folder.class).list()));
		return (res.get().size() == 1)?res.get().get(0):null;
	    }

	    	    	    	        @Override public void makeRoot(Folder folder)
	    {
		notNull(folder, "folder");
		trans(s -> s.createQuery("UPDATE Folder SET parentFolderId = :parent_id WHERE id = :id")
				   .setParameter("parent_id", folder.getId())
				   .setParameter("id", folder.getId())
				   .executeUpdate());
		folder.setParentFolderId(folder.getId());
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
	    	        @Override public List<MessageMetadata> getAll()
	    {
		final var res = new AtomicReference<List<MessageMetadata>>();
		trans(s -> res.set(s.createQuery("FROM MessageMetadata", MessageMetadata.class).list()));
		return res.get();
	    }
	};
    }

    static void deleteAllFolders()
    {
	trans(s -> s.createQuery("DELETE Folder").executeUpdate());
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
