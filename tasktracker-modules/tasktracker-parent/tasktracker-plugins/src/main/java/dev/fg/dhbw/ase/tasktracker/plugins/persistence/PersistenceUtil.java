package dev.fg.dhbw.ase.tasktracker.plugins.persistence;

import java.io.FileNotFoundException;

import javax.xml.bind.JAXBException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import dev.fg.dhbw.ase.tasktracker.domain.task.TaskListRepository;
import dev.fg.dhbw.ase.tasktracker.domain.user.User;
import dev.fg.dhbw.ase.tasktracker.domain.user.UserRepository;

public class PersistenceUtil
{
    public static final String BASE_PATH = System.getProperty("user.home") + System.getProperty("file.separator")
            + "tasktracker" + System.getProperty("file.separator");
    public static final String TASK_LISTS_FILE_PATH = BASE_PATH + "task-lists.xml";
    public static final String TASKS_FILE_PATH = BASE_PATH + "tasks.xml";
    private static Session session;

    private PersistenceUtil()
    {
    }

    public static TaskListRepository obtainTaskListRepository(User user)
    {
        if (user == null)
            try
            {
                return new TaskListFileSystemRepository();
            }
            catch (FileNotFoundException | JAXBException e)
            {
                e.printStackTrace();
            }

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
