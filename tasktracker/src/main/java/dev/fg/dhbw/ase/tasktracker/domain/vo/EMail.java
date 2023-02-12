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
        if (!this.isValid(mailString))
        {
            throw new InvalidEMailException();
        }
        this.mailString = mailString;
    }

    public String getMailAdress()
    {
        return this.mailString;
    }

    private boolean isValid(String mail)
    {
        if (!mail.contains("@"))
        {
            return false;
        }

        String[] split = mail.split("@");

        if (split.length != 2)
        {
            return false;
        }

        String user = split[0];
        String domain = split[1];

        if (!user.matches("[A-Za-z0-9]*"))
        {
            return false;
        }

        return domain.split(".").length == 2;
    }

    @Override
    public int hashCode()
    {
        return this.mailString.toLowerCase().hashCode();
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof EMail)
        {
            return this.mailString.equalsIgnoreCase(((EMail) obj).mailString);
        }
        return false;
    }
}
