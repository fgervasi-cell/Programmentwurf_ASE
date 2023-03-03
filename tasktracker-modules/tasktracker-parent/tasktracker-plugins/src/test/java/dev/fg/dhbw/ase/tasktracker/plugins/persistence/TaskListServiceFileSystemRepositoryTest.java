package dev.fg.dhbw.ase.tasktracker.plugins.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.xml.bind.JAXBException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dev.fg.dhbw.ase.tasktracker.application.TaskListService;
import dev.fg.dhbw.ase.tasktracker.domain.task.TaskList;
import dev.fg.dhbw.ase.tasktracker.domain.vo.Title;

public class TaskListServiceFileSystemRepositoryTest
{
    private static final String BASE_PATH = System.getProperty("java.io.tmpdir") + System.getProperty("file.separator")
            + "tasktracker" + System.getProperty("file.separator");
    private static final String TASK_LISTS_FILE_PATH = BASE_PATH + "task-lists.xml";
    private static final String TASKS_FILE_PATH = BASE_PATH + "tasks.xml";
    private TaskListService service;

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
        this.service = new TaskListService(repository);
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
        this.service.createTaskList(title, null);
        TaskList list = this.service.getTaskList(title);
        assertEquals(title, list.getTitle());
        assertEquals(null, list.getUserId());
    }

    @Test
    public void testCreateAndDeleteAndGetTaskLists()
    {
        Title title1 = new Title("Title 1");
        Title title2 = new Title("Title 2");
        Title title3 = new Title("Title 3");
        assertTrue(this.service.getTaskLists(null).isEmpty());
        this.service.createTaskList(title1, null);
        assertEquals(1, this.service.getTaskLists(null).size());
        this.service.createTaskList(title2, null);
        assertEquals(2, this.service.getTaskLists(null).size());
        this.service.createTaskList(title3, null);
        assertEquals(3, this.service.getTaskLists(null).size());
        this.service.deleteTaskList(this.service.getTaskList(title1));
        this.service.deleteTaskList(this.service.getTaskList(title2));
        this.service.deleteTaskList(this.service.getTaskList(title3));
        assertEquals(0, this.service.getTaskLists(null).size());
    }
}
