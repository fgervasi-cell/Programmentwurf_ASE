package dev.fg.dhbw.ase.tasktracker.plugins.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.UUID;

import javax.xml.bind.JAXBException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dev.fg.dhbw.ase.tasktracker.application.TaskListService;
import dev.fg.dhbw.ase.tasktracker.application.TaskService;
import dev.fg.dhbw.ase.tasktracker.domain.task.Task;
import dev.fg.dhbw.ase.tasktracker.domain.task.TaskFactory;
import dev.fg.dhbw.ase.tasktracker.domain.task.TaskList;
import dev.fg.dhbw.ase.tasktracker.domain.vo.Title;

public class FileSystemRepositoryTest
{
    private static final String BASE_PATH = System.getProperty("java.io.tmpdir") + System.getProperty("file.separator")
            + "tasktracker" + System.getProperty("file.separator");
    private static final String TASK_LISTS_FILE_PATH = BASE_PATH + "task-lists.xml";
    private static final String TASKS_FILE_PATH = BASE_PATH + "tasks.xml";
    private TaskListService taskListService;
    private TaskService taskService;

    @Before
    public void init() throws IOException, JAXBException
    {
        Files.createDirectories(Paths.get(BASE_PATH));
        Files.createFile(Paths.get(TASK_LISTS_FILE_PATH));
        Files.createFile(Paths.get(TASKS_FILE_PATH));
        Files.writeString(Paths.get(TASKS_FILE_PATH), "<tasks></tasks>", StandardCharsets.UTF_8);
        Files.writeString(Paths.get(TASK_LISTS_FILE_PATH), "<task-lists></task-lists>", StandardCharsets.UTF_8);
        TaskListFileSystemRepository repository = new TaskListFileSystemRepository(TASK_LISTS_FILE_PATH,
                TASKS_FILE_PATH);
        this.taskListService = new TaskListService(repository);
        this.taskService = new TaskService(repository);
    }

    @After
    public void clean() throws IOException
    {
        Files.deleteIfExists(Paths.get(TASK_LISTS_FILE_PATH));
        Files.deleteIfExists(Paths.get(TASKS_FILE_PATH));
        Files.deleteIfExists(Paths.get(BASE_PATH));
    }

    @Test
    public void testCreateAndGetTaskList()
    {
        Title title = new Title("MyTitle");
        this.taskListService.createTaskList(title, null);
        TaskList list = this.taskListService.getTaskList(title);
        assertEquals(title, list.getTitle());
        assertEquals(null, list.getUserId());
    }

    @Test
    public void testCreateAndDeleteAndGetTaskLists()
    {
        Title title1 = new Title("Title 1");
        Title title2 = new Title("Title 2");
        Title title3 = new Title("Title 3");
        assertTrue(this.taskListService.getTaskLists(null).isEmpty());
        this.taskListService.createTaskList(title1, null);
        assertEquals(1, this.taskListService.getTaskLists(null).size());
        this.taskListService.createTaskList(title2, null);
        assertEquals(2, this.taskListService.getTaskLists(null).size());
        this.taskListService.createTaskList(title3, null);
        assertEquals(3, this.taskListService.getTaskLists(null).size());
        this.taskListService.deleteTaskList(this.taskListService.getTaskList(title1));
        this.taskListService.deleteTaskList(this.taskListService.getTaskList(title2));
        this.taskListService.deleteTaskList(this.taskListService.getTaskList(title3));
        assertEquals(0, this.taskListService.getTaskLists(null).size());
    }

    @Test
    public void testMarkAsDoneAndUndone()
    {
        this.taskListService.createTaskList(new Title("Done"), null);
        Title taskListTitle = new Title("My Task List");
        this.taskListService.createTaskList(taskListTitle, null);
        TaskList list = this.taskListService.getTaskList(taskListTitle);
        Task t = TaskFactory.createTask(list.getId(), "Title 1", "Description 1", new Date(), new Date());
        this.taskService.createTask(t);
        this.taskService.markTaskAsDone(t);
        assertEquals(1, this.taskListService.getNumberOfTasksDone(null));
        this.taskService.markTaskAsUndone(this.taskListService.getTasksFromList(taskListTitle).get(0));
        assertEquals(0, this.taskListService.getNumberOfTasksDone(null));
        assertEquals(1, this.taskListService.getTasksFromList(taskListTitle).size());
    }

    @Test
    public void mockTest()
    {
        TaskListFileSystemRepository mockedRepo = mock(TaskListFileSystemRepository.class);
        Title taskListTitle = new Title("Task list name");
        when(mockedRepo.getTaskListByName(taskListTitle))
                .thenAnswer(invocation -> new TaskList(UUID.randomUUID(), taskListTitle));
        TaskListService service = new TaskListService(mockedRepo);
        service.getTaskList(taskListTitle);
        verify(mockedRepo).getTaskListByName(taskListTitle);
    }
}
