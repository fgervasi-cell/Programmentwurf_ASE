package dev.fg.dhbw.ase.tasktracker.exceptions;

public class InvalidPasswordException extends RuntimeException
{
    public InvalidPasswordException()
    {
        super("The password that was entered is invalid.");
    }
}
