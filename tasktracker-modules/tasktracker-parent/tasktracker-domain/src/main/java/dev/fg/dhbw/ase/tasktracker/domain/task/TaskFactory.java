package dev.fg.dhbw.ase.tasktracker.domain.task;

import java.util.Date;
import java.util.UUID;

import dev.fg.dhbw.ase.tasktracker.domain.vo.DateInFuture;
import dev.fg.dhbw.ase.tasktracker.domain.vo.Title;

public class TaskFactory
{
    private TaskFactory()
    {

    }

    public static Task createTask(UUID taskListId, String title, String description, Date dueDate, Date reminder)
    {
        Title taskTitle = new Title(title);
        DateInFuture futureDueDate = new DateInFuture(dueDate);
        DateInFuture futureReminderDate = new DateInFuture(reminder);
        return new Task(taskListId, null, taskTitle, description, futureDueDate, futureReminderDate, false);
    }

    public static Task createSubTask(UUID taskListId, UUID taskId, String title, String description, Date dueDate,
            Date reminder)
    {
        return new Task(taskListId, taskId, new Title(title), description, new DateInFuture(dueDate),
                new DateInFuture(reminder), false);
    }

    public static Task createTaskWithId(final UUID id, final UUID taskListId, String title, String description,
            Date dueDate, Date reminder)
    {
        return new Task(id, taskListId, new Title(title), description, new DateInFuture(dueDate),
                new DateInFuture(reminder));
    }

    public static Task createTaskDone(final Task task, final UUID taskList)
    {
        return new Task(taskList, task.getId(), task.getTitle(), task.getDescription(), task.getDueDate(),
                task.getReminder(), true, new Date(System.currentTimeMillis()));
    }

    public static Task createTaskUndone(final Task task)
    {
        return new Task(task.getTaskListId(), task.getId(), task.getTitle(), task.getDescription(), task.getDueDate(),
                task.getReminder(), false);
    }
}
