
package org.luwrain.pim.mail2;


import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class MailSessionFactory2
{
    private static SessionFactory sessionFactory;

    //    private HibernateSessionFactoryUtil() {}

    public static SessionFactory getSessionFactory()
    {
        if (sessionFactory == null)
	{
            try {
                final Configuration configuration = new Configuration().configure();
		                configuration.addAnnotatedClass(Account.class);
                final var builder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
                sessionFactory = configuration.buildSessionFactory(builder.build());
            }
	    catch (Exception e)
	    {
		e.printStackTrace();
            }
        }
        return sessionFactory;
    }
}
