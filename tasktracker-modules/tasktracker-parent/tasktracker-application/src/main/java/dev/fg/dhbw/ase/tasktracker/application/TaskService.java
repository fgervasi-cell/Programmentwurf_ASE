package dev.fg.dhbw.ase.tasktracker.application;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    public List<Task> loadSubTasksForTask(Task task, UUID userId)
    {
        List<TaskList> lists = this.repository.getTaskListsForUser(userId);
        List<Task> subTasks = new ArrayList<>();
        for (TaskList list : lists)
        {
            subTasks.addAll(this.repository.getTasksOfTaskListByTaskListName(list.getTitle()));
        }
        return subTasks.stream().filter(t -> t.getTaskId() != null && t.getTaskId().equals(task.getId())).toList();
    }
}
