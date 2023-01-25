package dev.fg.dhbw.ase.tasktracker.domain.entities;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import dev.fg.dhbw.ase.tasktracker.domain.vo.TaskTitle;
import dev.fg.dhbw.ase.tasktracker.exceptions.InvalidTitleException;

@Entity
@Table(name = "task_list")
public class TaskList implements Serializable
{
    @Id
    @Column(name = "id", columnDefinition = "BINARY(16)")
    private final UUID id;
    @Column(name = "user_id", columnDefinition = "BINARY(16)")
    private final UUID userId;
    @Id
    @Embedded
    private TaskTitle title;

    @SuppressWarnings("unused")
    private TaskList()
    {
        this.id = null;
        this.userId = null;
    }

    public TaskList(TaskTitle title)
    {
        if (title == null)
        {
            throw new InvalidTitleException();
        }
        this.id = UUID.randomUUID();
        this.userId = null;
        this.title = title;
    }

    public TaskList(UUID userId, TaskTitle title)
    {
        if (title == null)
        {
            throw new InvalidTitleException();
        }
        this.id = UUID.randomUUID();
        this.userId = userId;
        this.title = title;
    }

    public UUID getId()
    {
        return this.id;
    }

    public UUID getUserId()
    {
        return this.userId;
    }

    public TaskTitle getTitle()
    {
        return this.title;
    }

    public void changeTitle(TaskTitle title)
    {
        this.title = title;
    }
}
