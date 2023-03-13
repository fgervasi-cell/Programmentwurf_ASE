package dev.fg.dhbw.ase.tasktracker.domain.task;

import java.util.Date;
import java.util.UUID;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import dev.fg.dhbw.ase.tasktracker.domain.vo.DateInFuture;
import dev.fg.dhbw.ase.tasktracker.domain.vo.Title;
import dev.fg.dhbw.ase.tasktracker.domain.exceptions.TaskWithoutTaskListIdException;

@Entity
@Table(name = "task")
@XmlRootElement(name = "task")
public class Task
{
    @Id
    @Column(name = "id", columnDefinition = "BINARY(16)")
    @XmlElement(name = "id")
    private final UUID id;
    @Column(name = "task_list_id", columnDefinition = "BINARY(16)")
    @XmlElement(name = "task-list-id")
    private final UUID taskListId;
    @Column(name = "task_id", columnDefinition = "BINARY(16)")
    @XmlElement(name = "task-id")
    private final UUID taskId;
    @Embedded
    @XmlElement(name = "title")
    private Title title;
    @Embedded
    @AttributeOverride(name = "date", column = @Column(name = "due_date"))
    @XmlElement(name = "due-date")
    private DateInFuture dueDate;
    @Column(name = "description")
    @XmlElement(name = "description")
    private String description;
    @Embedded
    @AttributeOverride(name = "date", column = @Column(name = "reminder"))
    @XmlElement(name = "reminder")
    private DateInFuture reminder;
    @Column(name = "done")
    @XmlElement(name = "done")
    private boolean done;
    @Column(name = "creation_date")
    @XmlElement(name = "creation-date")
    private final Date creationDate;
    @Column(name = "completion_date")
    @XmlElement(name = "completion-date")
    private Date completionDate;

    @SuppressWarnings("unused")
    private Task()
    {
        this.id = null;
        this.taskListId = null;
        this.taskId = null;
        this.creationDate = null;
    }

    public Task(final UUID taskListId, UUID taskId, Title title, String description, DateInFuture dueDate,
            DateInFuture reminder, boolean done)
    {
        if (taskListId == null)
        {
            throw new TaskWithoutTaskListIdException();
        }
        this.id = UUID.randomUUID();
        this.taskListId = taskListId;
        this.taskId = taskId;
        this.title = title;
        this.dueDate = dueDate;
        this.description = description;
        this.reminder = reminder;
        this.done = done;
        this.creationDate = new Date(System.currentTimeMillis());
    }

    public Task(final UUID id, final UUID taskListId, Title title, String description, DateInFuture dueDate,
            DateInFuture reminder)
    {
        if (taskListId == null)
        {
            throw new TaskWithoutTaskListIdException();
        }
        this.id = id;
        this.taskListId = taskListId;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.reminder = reminder;
        this.taskId = null;
        this.done = false;
        this.creationDate = new Date(System.currentTimeMillis());
    }

    public Task(final UUID taskListId, UUID taskId, Title title, String description, DateInFuture dueDate,
            DateInFuture reminder, boolean done, Date completionDate)
    {
        this(taskListId, taskId, title, description, dueDate, reminder, done);
        this.completionDate = completionDate;
    }

    public UUID getId()
    {
        return this.id;
    }

    public UUID getTaskListId()
    {
        return this.taskListId;
    }

    public UUID getTaskId()
    {
        return this.taskId;
    }

    public Title getTitle()
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

    public Date getCreationDate()
    {
        return this.creationDate;
    }

    public Date getCompletionDate()
    {
        return this.completionDate;
    }

    public void markTaskAsDone()
    {
        this.done = true;
    }

    public void markTaskAsUndone()
    {
        this.done = false;
    }
}
