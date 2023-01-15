package dev.fg.dhbw.ase.tasktracker.domain.vo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import dev.fg.dhbw.ase.tasktracker.exceptions.InvalidTitleException;

@Embeddable
public final class TaskTitle implements Serializable
{
    @Column(name = "title", nullable = false)
    private final String title;

    @SuppressWarnings("unused")
    private TaskTitle()
    {
        this.title = null;
    }

    public TaskTitle(final String title)
    {
        if (title == null || title.isEmpty() || title.isBlank())
        {
            throw new InvalidTitleException();
        }
        this.title = title;
    }

    public String getTitle()
    {
        return this.title;
    }

    public TaskTitle changTitle(String newTitle)
    {
        return new TaskTitle(newTitle);
    }

    @Override
    public int hashCode()
    {
        return this.title.hashCode();
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof TaskTitle)
        {
            return this.title.equals(((TaskTitle)obj).title);
        }
        return false;
    }
}
