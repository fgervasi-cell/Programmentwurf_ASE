package dev.fg.dhbw.ase.tasktracker.plugins.persistence;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import dev.fg.dhbw.ase.tasktracker.domain.task.Task;
import dev.fg.dhbw.ase.tasktracker.domain.task.TaskFactory;
import dev.fg.dhbw.ase.tasktracker.domain.task.TaskList;
import dev.fg.dhbw.ase.tasktracker.domain.task.TaskListRepository;
import dev.fg.dhbw.ase.tasktracker.domain.task.TaskListXmlWrapper;
import dev.fg.dhbw.ase.tasktracker.domain.task.TaskXmlWrapper;
import dev.fg.dhbw.ase.tasktracker.domain.user.User;
import dev.fg.dhbw.ase.tasktracker.domain.vo.Title;

class TaskListFileSystemRepository implements TaskListRepository
{
    private List<Task> tasks;
    private List<TaskList> lists;
    private final Marshaller marshaller;
    private final Unmarshaller unmarshaller;
    private final String taskListsFilePath;
    private final String tasksFilePath;

    public TaskListFileSystemRepository(String tasksListsFilePath, String tasksFilePath) throws JAXBException
    {
        this.taskListsFilePath = tasksListsFilePath;
        this.tasksFilePath = tasksFilePath;
        JAXBContext context = JAXBContext.newInstance(TaskListXmlWrapper.class, TaskXmlWrapper.class);
        marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        unmarshaller = context.createUnmarshaller();
        loadTaskListsFromFile();
        loadTasksFromFile();
    }

    @Override
    public TaskList getTaskList(UUID taskListId)
    {
        loadTaskListsFromFile();
        return this.lists.stream().filter(list -> list.getId().equals(taskListId)).toList().get(0);
    }

    @Override
    public List<TaskList> getTaskListsForUser(UUID user)
    {
        loadTaskListsFromFile();
        return this.lists;
    }

    @Override
    public void createNewTaskList(Title name)
    {
        loadTaskListsFromFile();
        this.lists.add(new TaskList(name));
        writeTaskListsBackToFile();
    }

    @Override
    public void deleteTaskList(TaskList taskList)
    {
        loadTaskListsFromFile();
        this.lists.removeIf(list -> list.getId().equals(taskList.getId()));
        this.tasks.removeAll(this.tasks.stream().filter(t -> t.getTaskListId().equals(taskList.getId())).toList());
        writeTaskListsBackToFile();
        writeTasksBackToFile();
    }

    @Override
    public void updateTaskList(UUID taskListId, Title name)
    {
        loadTaskListsFromFile();
        this.lists.removeIf(list -> list.getId().equals(taskListId));
        this.lists.add(new TaskList(name));
        writeTaskListsBackToFile();
    }

    @Override
    public void createNewTaskListForUser(Title name, User user)
    {
        createNewTaskList(name);
    }

    @Override
    public List<Task> getTasksOfTaskListByTaskListName(Title name)
    {
        loadTaskListsFromFile();
        UUID taskListId = this.lists.stream().filter(list -> list.getTitle().equals(name)).toList().get(0).getId();
        loadTasksFromFile();
        return this.tasks.stream().filter(task -> task.getTaskListId().equals(taskListId)).toList();
    }

    @Override
    public void addTaskToTaskList(Task t)
    {
        loadTasksFromFile();
        this.tasks.add(t);
        writeTasksBackToFile();
    }

    @Override
    public TaskList getTaskListByName(Title name)
    {
        loadTaskListsFromFile();
        return this.lists.stream().filter(list -> list.getTitle().equals(name)).toList().get(0);
    }

    @Override
    public void removeTask(Task task)
    {
        loadTasksFromFile();
        this.tasks.removeIf(t -> t.getId().equals(task.getId()));
        writeTasksBackToFile();
    }

    @Override
    public List<Task> getTasksDoneForUser(UUID user)
    {
        loadTasksFromFile();
        return this.tasks.stream().filter(Task::isDone).toList();
    }

    @Override
    public void markTaskAsDone(Task task)
    {
        loadTasksFromFile();
        this.tasks.removeIf(t -> t.getId().equals(task.getId()));
        this.tasks.add(TaskFactory.createTaskDone(task, task.getTaskListId()));
        this.writeTasksBackToFile();
    }

    @Override
    public void markTaskAsUndone(Task task)
    {
        loadTasksFromFile();
        this.tasks.removeIf(t -> t.getId().equals(task.getId()));
        this.tasks.add(TaskFactory.createTaskUndone(task));
        this.writeTasksBackToFile();
    }

    @Override
    public int getNumberOfDoneTasksForUser(User user)
    {
        loadTasksFromFile();
        return this.tasks.size();
    }

    @Override
    public int getNumberOfOpenTasksForUser(User user)
    {
        loadTasksFromFile();
        return this.tasks.stream().filter(Task::isDone).toList().size();
    }

    @Override
    public List<Task> getDoneTasksOfLastWeek(User user)
    {
        loadTasksFromFile();
        loadTaskListsFromFile();
        Calendar today = Calendar.getInstance();
        today.add(Calendar.DAY_OF_MONTH, -5);
        List<Task> doneTasksOfLastWeek = new ArrayList<>();
        for (TaskList list : this.lists)
        {
            doneTasksOfLastWeek.addAll(this.tasks.stream()
                    .filter(t -> t.getTaskListId().equals(list.getId()) && t.isDone()
                            && t.getCompletionDate().getTime() > today.getTimeInMillis()
                            && t.getCompletionDate().getTime() < System.currentTimeMillis())
                    .toList());
        }
        return doneTasksOfLastWeek;
    }

    private void writeTaskListsBackToFile()
    {
        try
        {
            this.marshaller.marshal(new TaskListXmlWrapper(this.lists), new File(this.taskListsFilePath));
        }
        catch (JAXBException e)
        {
            e.printStackTrace();
        }
    }

    private void writeTasksBackToFile()
    {
        try
        {
            this.marshaller.marshal(new TaskXmlWrapper(this.tasks), new File(this.tasksFilePath));
        }
        catch (JAXBException e)
        {
            e.printStackTrace();
        }
    }

    private void loadTasksFromFile()
    {
        try
        {
            TaskXmlWrapper taskXmlWrapper = (TaskXmlWrapper) this.unmarshaller
                    .unmarshal(new FileReader(this.tasksFilePath));
            this.tasks = taskXmlWrapper.getTasks() != null ? taskXmlWrapper.getTasks() : new ArrayList<>();
        }
        catch (FileNotFoundException | JAXBException e)
        {
            e.printStackTrace();
        }
    }

    private void loadTaskListsFromFile()
    {
        try
        {
            TaskListXmlWrapper taskListXmlWrapper = (TaskListXmlWrapper) this.unmarshaller
                    .unmarshal(new FileReader(this.taskListsFilePath));
            this.lists = taskListXmlWrapper.getTaskLists() != null ? taskListXmlWrapper.getTaskLists()
                    : new ArrayList<>();
        }
        catch (FileNotFoundException | JAXBException e)
        {
            e.printStackTrace();
        }
    }
}
