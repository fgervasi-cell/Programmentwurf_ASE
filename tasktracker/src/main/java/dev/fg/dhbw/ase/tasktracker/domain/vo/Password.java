package dev.fg.dhbw.ase.tasktracker.domain.vo;

import dev.fg.dhbw.ase.tasktracker.exceptions.InvalidPasswordException;

public class Password
{
    private final String passwordString;

    public Password(final String passwordString)
    {
        if (!passwordIsValid(passwordString))
        {
            throw new InvalidPasswordException();
        }
        this.passwordString = passwordString;
    }

    public String getPassword()
    {
        return this.passwordString;
    }

    private boolean passwordIsValid(final String passwordString)
    {
        return passwordString.length() >= 8 && passwordString.matches("[A-Za-z0-9]");
    }
}
