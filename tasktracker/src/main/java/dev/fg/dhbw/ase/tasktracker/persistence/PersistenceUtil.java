package dev.fg.dhbw.ase.tasktracker.persistence;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class PersistenceUtil
{
    private static Session session;

    private PersistenceUtil()
    {
    }

    public static TaskListRepository obtainTaskListRepository()
    {
        return new TaskListDatabaseRepository(createSessionIfNotPresent());
    }

    public static UserRepository obtainUserRepository()
    {
        return new UserDatabaseRepository(createSessionIfNotPresent());
    }

    private static Session createSessionIfNotPresent()
    {
        if (session != null)
        {
            return session;
        }
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        SessionFactory sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        session = sessionFactory.openSession();
        return session;
    }
}
