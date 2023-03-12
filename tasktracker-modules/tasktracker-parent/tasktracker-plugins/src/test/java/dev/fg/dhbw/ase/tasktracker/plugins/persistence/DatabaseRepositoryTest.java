package dev.fg.dhbw.ase.tasktracker.plugins.persistence;

import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import dev.fg.dhbw.ase.tasktracker.application.TaskListService;
import dev.fg.dhbw.ase.tasktracker.application.UserService;
import dev.fg.dhbw.ase.tasktracker.domain.task.TaskList;
import dev.fg.dhbw.ase.tasktracker.domain.user.User;
import dev.fg.dhbw.ase.tasktracker.domain.user.UserRepository;
import dev.fg.dhbw.ase.tasktracker.domain.vo.EMail;
import dev.fg.dhbw.ase.tasktracker.domain.vo.Password;
import dev.fg.dhbw.ase.tasktracker.domain.vo.Title;

public class DatabaseRepositoryTest
{
    private TaskListService taskListService;
    private UserService userService;

    @Before
    public void init()
    {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure(getClass().getResource("hibernate.cfg.xml")).build();
        SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        Session session = sf.openSession();
        TaskListDatabaseRepository taskListDBRepository = new TaskListDatabaseRepository(session);
        UserRepository userRepository = new UserDatabaseRepository(session);
        this.taskListService = new TaskListService(taskListDBRepository);
        this.userService = new UserService(userRepository);
    }

    @After
    public void clean()
    {

    }

    @Test
    public void testRegisterUserAndCreateAndGetTaskList()
    {
        User user = new User(new EMail("some@mail.com"), new Password("SafePassword"));
        this.userService.registerUser(user);
        this.taskListService.createTaskList(new Title("My List"), user);
        TaskList list = this.taskListService.getTaskList(new Title("My List"));
        assertEquals(new Title("My List"), list.getTitle());
        assertEquals(user.getId(), list.getUserId());
    }
}
