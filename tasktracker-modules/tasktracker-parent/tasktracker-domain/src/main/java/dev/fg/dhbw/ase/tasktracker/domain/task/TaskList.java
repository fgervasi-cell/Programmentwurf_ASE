package dev.fg.dhbw.ase.tasktracker.domain.task;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import dev.fg.dhbw.ase.tasktracker.domain.vo.Title;
import dev.fg.dhbw.ase.tasktracker.domain.exceptions.InvalidTitleException;

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
    private Title title;

    @SuppressWarnings("unused")
    private TaskList()
    {
        this.id = null;
        this.userId = null;
    }

    public TaskList(Title title)
    {
        if (title == null)
        {
            throw new InvalidTitleException();
        }
        this.id = UUID.randomUUID();
        this.userId = null;
        this.title = title;
    }

    public TaskList(UUID userId, Title title)
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

    public Title getTitle()
    {
        return this.title;
    }

    public void changeTitle(Title title)
    {
        this.title = title;
    }
}
