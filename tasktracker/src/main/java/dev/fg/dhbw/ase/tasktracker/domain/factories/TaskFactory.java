package dev.fg.dhbw.ase.tasktracker.domain.factories;

import java.util.Date;
import java.util.UUID;

import dev.fg.dhbw.ase.tasktracker.domain.entities.Task;
import dev.fg.dhbw.ase.tasktracker.domain.vo.DateInFuture;
import dev.fg.dhbw.ase.tasktracker.domain.vo.Title;

public class TaskFactory
{
    private TaskFactory()
    {

    }

    public static Task createTask(final String title, final UUID taskListId)
    {
        Title taskTitle = new Title(title);
        return new Task(taskListId, null, taskTitle, title, null, null, false);
    }

    public static Task createTask(final String title, final Date dueDate, final Date reminder)
    {
        Title taskTitle = new Title(title);
        DateInFuture futureDueDate = new DateInFuture(dueDate);
        DateInFuture futureReminder = new DateInFuture(reminder);
        return new Task(null, null, taskTitle, title, futureDueDate, futureReminder, false);
    }

    public static Task createSubTask(final String title, final UUID taskId)
    {
        Title taskTitle = new Title(title);
        return new Task(null, taskId, taskTitle, title, null, null, false);
    }
}
