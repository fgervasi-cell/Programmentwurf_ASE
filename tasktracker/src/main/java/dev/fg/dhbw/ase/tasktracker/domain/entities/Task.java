package dev.fg.dhbw.ase.tasktracker.domain.entities;

import java.util.UUID;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import dev.fg.dhbw.ase.tasktracker.domain.vo.DateInFuture;
import dev.fg.dhbw.ase.tasktracker.domain.vo.TaskTitle;
import dev.fg.dhbw.ase.tasktracker.exceptions.TaskWithoutTaskListIdException;

@Entity
@Table(name = "task")
public class Task
{
    @Id
    @Column(name = "id", columnDefinition = "BINARY(16)")
    private final UUID id;
    @Column(name = "task_list_id", columnDefinition = "BINARY(16)")
    private final UUID taskListId;
    // TODO
    private final UUID taskId = null;
    @Embedded
    private TaskTitle title;
    @Embedded
    @AttributeOverride(name = "date", column = @Column(name = "due_date"))
    private DateInFuture dueDate;
    @Column(name = "description")
    private String description;
    @Embedded
    @AttributeOverride(name = "date", column = @Column(name = "reminder"))
    private DateInFuture reminder;
    @Column(name = "done")
    private boolean done;

    public Task(final UUID taskListId, TaskTitle title, String description, DateInFuture dueDate, DateInFuture reminder,
            boolean done)
    {
        if (taskListId == null)
        {
            throw new TaskWithoutTaskListIdException();
        }
        this.id = UUID.randomUUID();
        this.taskListId = taskListId;
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

    public UUID getTaskListId()
    {
        return this.taskListId;
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

    public boolean isSubTask()
    {
        return this.taskId != null;
    }
}
