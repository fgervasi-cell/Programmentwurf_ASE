package dev.fg.dhbw.ase.tasktracker.domain.exceptions;

public class InvalidTitleException extends RuntimeException
{
    public InvalidTitleException()
    {
        super("The title must not be null or empty.");
    }
}
