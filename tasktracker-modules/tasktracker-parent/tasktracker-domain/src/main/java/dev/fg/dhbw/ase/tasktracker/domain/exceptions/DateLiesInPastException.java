package dev.fg.dhbw.ase.tasktracker.domain.exceptions;

public class DateLiesInPastException extends RuntimeException
{
    public DateLiesInPastException()
    {
        super("The date must be in the future.");
    }
}
