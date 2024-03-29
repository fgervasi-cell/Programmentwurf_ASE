package dev.fg.dhbw.ase.tasktracker.application;

import java.util.List;

import dev.fg.dhbw.ase.tasktracker.domain.task.Task;
import dev.fg.dhbw.ase.tasktracker.domain.task.TaskList;
import dev.fg.dhbw.ase.tasktracker.domain.task.TaskListRepository;
import dev.fg.dhbw.ase.tasktracker.domain.user.User;
import dev.fg.dhbw.ase.tasktracker.domain.vo.Title;

public class TaskListService
{
    private TaskListRepository repository;

    public TaskListService(TaskListRepository repository)
    {
        this.repository = repository;
    }

    public void createTaskList(Title name, User user)
    {
        this.repository.createNewTaskListForUser(name, user);
    }

    public void deleteTaskList(TaskList list)
    {
        this.repository.deleteTaskList(list);
    }

    public void renameTaskList(TaskList list, Title name)
    {
        this.repository.updateTaskList(list.getId(), name);
    }

    public List<TaskList> getTaskLists(User user)
    {
        return this.repository.getTaskListsForUser(user != null ? user.getId() : null);
    }

    public List<Task> getTasksFromList(Title name)
    {
        return this.repository.getTasksOfTaskListByTaskListName(name);
    }

    public TaskList getTaskList(Title name)
    {
        return this.repository.getTaskListByName(name);
    }

    public int getNumberOfTasksDone(User user)
    {
        return this.repository.getNumberOfDoneTasksForUser(user);
    }

    public List<Task> getTasksDone(User user)
    {
        return this.repository.getTasksDoneForUser(user != null ? user.getId() : null);
    }

    public int getTasksOpen(User user)
    {
        return this.repository.getNumberOfOpenTasksForUser(user);
    }

    public List<Task> getDoneTasksOfLastWeek(User user)
    {
        return this.repository.getDoneTasksOfLastWeek(user);
    }
}
