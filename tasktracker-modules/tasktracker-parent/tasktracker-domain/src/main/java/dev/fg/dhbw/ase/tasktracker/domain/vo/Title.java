package dev.fg.dhbw.ase.tasktracker.domain.vo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

import dev.fg.dhbw.ase.tasktracker.domain.exceptions.InvalidTitleException;

@Embeddable
@XmlRootElement(name = "title")
public final class Title implements Serializable
{
    @Column(name = "title", nullable = false)
    @XmlValue
    private final String titleString;

    @SuppressWarnings("unused")
    private Title()
    {
        this.titleString = null;
    }

    public Title(final String title)
    {
        if (title == null || title.isEmpty() || title.isBlank())
        {
            throw new InvalidTitleException();
        }
        this.titleString = title;
    }

    public String getTitleString()
    {
        return this.titleString;
    }

    public Title changeTitle(String newTitle)
    {
        return new Title(newTitle);
    }

    @Override
    public int hashCode()
    {
        return this.titleString.hashCode();
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof Title)
        {
            return this.titleString.equals(((Title)obj).titleString);
        }
        return false;
    }
}
