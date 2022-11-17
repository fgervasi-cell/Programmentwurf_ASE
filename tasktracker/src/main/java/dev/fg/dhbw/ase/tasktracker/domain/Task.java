package dev.fg.dhbw.ase.tasktracker.domain;

import java.util.List;
import java.util.UUID;

public class Task 
{
    private final UUID id;
    private TaskTitle title;
    private DateInFuture dueDate;
    private List<Task> subTasks;
    private String description;
    private DateInFuture reminder;
    private boolean done;

    public Task(TaskTitle title, String description, DateInFuture dueDate, DateInFuture reminder, boolean done)
    {
        this.id = UUID.randomUUID();
        this.title = title;
        this.dueDate = dueDate;
        this.description = description;
        this.reminder = reminder;
        this.done = done;
    }

    public UUID getId()
    {
        return this.id;
    }

    public TaskTitle getTitle()
    {
        return this.title;
    }

    public DateInFuture getDueDate()
    {
        return this.dueDate;
    }

    public String getDescription()
    {
        return this.description;
    }

    public DateInFuture getReminder()
    {
        return this.reminder;
    }

    public boolean isDone()
    {
        return this.done;
    }
}
