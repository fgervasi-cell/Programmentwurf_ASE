package dev.fg.dhbw.ase.tasktracker.domain.vo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import dev.fg.dhbw.ase.tasktracker.exceptions.InvalidEMailException;

@Embeddable
public class EMail implements Serializable
{
    @Column(name = "mail")
    private final String mailString;

    @SuppressWarnings("unused")
    private EMail()
    {
        this.mailString = null;
    }

    public EMail(final String mailString)
    {
        if (!mailString.contains("@"))
        {
            throw new InvalidEMailException();
        }
        this.mailString = mailString;
    }

    public String getMailAdress()
    {
        return this.mailString;
    }

    @Override
    public int hashCode()
    {
        return this.mailString.hashCode();
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof EMail)
        {
            return this.mailString.equals(((EMail) obj).mailString);
        }
        return false;
    }
}
