package dev.fg.dhbw.ase.tasktracker.exceptions;

public class InvalidEMailException extends RuntimeException
{
    public InvalidEMailException()
    {
        super("The e-mail address that was provided is invalid.");
    }
}
