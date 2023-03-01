package dev.fg.dhbw.ase.tasktracker.application;

import dev.fg.dhbw.ase.tasktracker.domain.task.Task;
import dev.fg.dhbw.ase.tasktracker.domain.task.TaskList;
import dev.fg.dhbw.ase.tasktracker.domain.task.TaskListRepository;

public class TaskService
{
    private TaskListRepository repository;

    public TaskService(TaskListRepository repository)
    {
        this.repository = repository;
    }

    public void createTask(Task task)
    {
        repository.addTaskToTaskList(task);
    }

    public void deleteTask(Task task)
    {
        repository.removeTask(task);
    }

    public void markTaskAsDone(Task task)
    {
        this.repository.markTaskAsDone(task);
    }

    public void markTaskAsUndone(Task task)
    {
        this.repository.markTaskAsUndone(task);
    }

    public void moveTask(Task task, TaskList list)
    {
        repository.moveTaskToList(task, list);
    }
}
