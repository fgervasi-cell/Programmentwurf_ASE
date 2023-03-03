package dev.fg.dhbw.ase.tasktracker.plugins.persistence;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
    private static final String BASE_PATH = System.getProperty("user.home") + System.getProperty("file.separator")
            + "tasktracker" + System.getProperty("file.separator");
    private static final String TASK_LISTS_FILE_PATH = BASE_PATH + "task-lists.xml";
    private static final String TASKS_FILE_PATH = BASE_PATH + "tasks.xml";
    private static Session session;

    private PersistenceUtil()
    {
    }

    public static TaskListRepository obtainTaskListRepository(User user)
    {
        if (user == null)
            try
            {
                createDataFiles();
                return new TaskListFileSystemRepository(TASK_LISTS_FILE_PATH, TASKS_FILE_PATH);
            }
            catch (JAXBException e)
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

    private static void createDataFiles()
    {
        try
        {
            Files.createDirectories(Paths.get(PersistenceUtil.BASE_PATH));
            Path taskListsFilePath = Paths.get(PersistenceUtil.TASK_LISTS_FILE_PATH);
            if (!Files.exists(taskListsFilePath))
            {
                Files.createFile(taskListsFilePath);
                Files.writeString(taskListsFilePath, "<task-lists></task-lists>", StandardCharsets.UTF_8);
            }

            Path tasksFilePath = Paths.get(PersistenceUtil.TASKS_FILE_PATH);
            if (!Files.exists(tasksFilePath))
            {
                Files.createFile(tasksFilePath);
                Files.writeString(tasksFilePath, "<tasks></tasks>", StandardCharsets.UTF_8);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
