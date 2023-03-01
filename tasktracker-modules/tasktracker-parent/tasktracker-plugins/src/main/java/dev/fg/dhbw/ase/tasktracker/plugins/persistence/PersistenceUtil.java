package dev.fg.dhbw.ase.tasktracker.plugins.persistence;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import dev.fg.dhbw.ase.tasktracker.domain.task.TaskListRepository;
import dev.fg.dhbw.ase.tasktracker.domain.user.UserRepository;

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

    public static void closeSessionIfPresent()
    {
        if (session != null)
        {
            session.close();
        }
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
