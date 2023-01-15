package dev.fg.dhbw.ase.tasktracker.domain.vo;

import dev.fg.dhbw.ase.tasktracker.exceptions.InvalidEMailException;

public class EMail
{
    private final String mailString;

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
}
