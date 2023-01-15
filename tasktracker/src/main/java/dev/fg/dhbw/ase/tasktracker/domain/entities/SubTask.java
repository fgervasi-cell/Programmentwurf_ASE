package dev.fg.dhbw.ase.tasktracker.domain.entities;

import java.util.UUID;

import dev.fg.dhbw.ase.tasktracker.domain.vo.DateInFuture;
import dev.fg.dhbw.ase.tasktracker.domain.vo.TaskTitle;
import dev.fg.dhbw.ase.tasktracker.exceptions.SubTaskWithoutTaskIdException;

// Not needed :(
public class SubTask
{
    private final UUID id;
    private final UUID taskId;
    private TaskTitle title;
    private DateInFuture dueDate;
    private String description;
    private DateInFuture reminder;
    private boolean done;

    public SubTask(final UUID taskId, TaskTitle title, String description, DateInFuture dueDate, DateInFuture reminder, boolean done)
    {
        if (taskId == null)
        {
            throw new SubTaskWithoutTaskIdException();
        }
        this.id = UUID.randomUUID();
        this.taskId = taskId;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.reminder = reminder;
        this.done = done;
    }

    public UUID getId()
    {
        return this.id;
    }

    public UUID getTaskId()
    {
        return this.taskId;
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
