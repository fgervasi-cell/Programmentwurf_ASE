package dev.fg.dhbw.ase.tasktracker.domain.vo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import dev.fg.dhbw.ase.tasktracker.exceptions.InvalidPasswordException;

@Embeddable
public class Password implements Serializable
{
    @Column(name = "password")
    private final String passwordString;

    @SuppressWarnings("unused")
    private Password()
    {
        this.passwordString = null;
    }

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
        return passwordString.length() >= 8 && passwordString.matches("[A-Za-z0-9]*");
    }

    @Override
    public int hashCode()
    {
        return this.passwordString.hashCode();
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof Password)
        {
            return this.passwordString.equals(((Password) obj).passwordString);
        }
        return false;
    }
}
