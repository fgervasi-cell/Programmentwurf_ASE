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

    public static Task createTask(UUID taskListId, String title, String description, Date dueDate, Date reminder)
    {
        Title taskTitle = new Title(title);
        DateInFuture futureDueDate = new DateInFuture(dueDate);
        DateInFuture futureReminderDate = new DateInFuture(reminder);
        return new Task(taskListId, null, taskTitle, description, futureDueDate, futureReminderDate, false);
    }
}
