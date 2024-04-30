
package org.luwrain.pim.mail2;

import java.util.*;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import org.hibernate.Session;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;

import org.hibernate.Transaction;


@Disabled
public class HibernateTest
{
    @Test public void main() throws Exception
    {
        final var a = new Account();
	a.setName("Test");
	Transaction transaction = null;
        try (Session session = MailSessionFactory.getSessionFactory().openSession())
	{
            transaction = session.beginTransaction();
            session.save(a);
            transaction.commit();
        }
	catch (Exception e)
	{
	    e.printStackTrace();
            if (transaction != null) 
                transaction.rollback();
	    throw e;
        }

	transaction = null;
        try (Session session = MailSessionFactory.getSessionFactory().openSession()) {
            List <Account> accounts = session.createQuery("FROM Account", Account.class).list();
            accounts.forEach(s -> System.out.println(s.getName()));
        }
	catch (Exception e)
	{
	    	    e.printStackTrace();
            if (transaction != null) 
                transaction.rollback();
	    throw e;
        }
    }
}
